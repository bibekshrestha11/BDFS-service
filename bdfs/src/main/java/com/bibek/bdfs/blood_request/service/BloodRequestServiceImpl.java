package com.bibek.bdfs.blood_request.service;

import com.bibek.bdfs.blood_request.dto.BloodRequest;
import com.bibek.bdfs.blood_request.dto.BloodResponse;
import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.blood_request.entity.BloodRequestStatus;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.blood_request.repository.BloodRequestRepository;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import com.bibek.bdfs.donor_matches.repository.DonorMatchRepository;
import com.bibek.bdfs.knn.KnnService;
import com.bibek.bdfs.notification.service.NotificationService;
import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.util.logged_in_user.LoggedInUserUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BloodRequestServiceImpl implements BloodRequestService{
    private final BloodRequestRepository bloodRequestRepository;
    private final LoggedInUserUtil loggedInUserUtil;
    private final KnnService knnService;
    private final DonorMatchRepository donorMatchRepository;
    private final NotificationService notificationService;

    private static final String BLOOD_REQUEST_NOT_FOUND = "Blood request not found with ID: ";
    @Override
    @Transactional
    public BloodResponse createBloodRequest(BloodRequest bloodRequest) {
        log.info("Creating blood request: {}", bloodRequest);

        User loggedInUser = loggedInUserUtil.getLoggedInUser();
        BloodRequestEntity bloodRequestEntity = new BloodRequestEntity();
        bloodRequestEntity.setUser(loggedInUser);

        mapFieldsFromDto(bloodRequest, bloodRequestEntity);

        bloodRequestEntity.setStatus(BloodRequestStatus.OPEN);

        log.info("Searching for nearest donors for the blood request: {}", bloodRequestEntity);

        List<User> nearestDonors = knnService.findNearestDonors(bloodRequestEntity);

        log.info("Found {} nearest donors for the blood request", nearestDonors.size());

        List<RequestDonorMatch> matches = nearestDonors.stream()
                .map(donor -> createMatch(donor, bloodRequestEntity))
                .toList();
        donorMatchRepository.saveAll(matches);

        bloodRequestEntity.setMatches(matches);
        bloodRequestRepository.save(bloodRequestEntity);

        matches.forEach(notificationService::notifyDonor);

        return new BloodResponse(bloodRequestEntity);
    }

    private RequestDonorMatch createMatch(User donor, BloodRequestEntity request) {
        return RequestDonorMatch.builder()
                .bloodRequest(request)
                .donor(donor)
                .notifiedAt(LocalDateTime.now())
                .responseStatus(ResponseStatus.PENDING)
                .build();
    }

    @Override
    public BloodResponse updateBloodRequest(Long id, BloodRequest bloodRequest) {
        log.info("Updating blood request with ID: {}", id);

        BloodRequestEntity bloodRequestEntity = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BLOOD_REQUEST_NOT_FOUND + id));

        mapFieldsFromDto(bloodRequest, bloodRequestEntity);

        bloodRequestRepository.save(bloodRequestEntity);
        return new BloodResponse(bloodRequestEntity);
    }

    private void mapFieldsFromDto(BloodRequest source, BloodRequestEntity target) {
        target.setBloodTypeNeeded(source.getBloodTypeNeeded());
        target.setQuantity(source.getQuantity());
        target.setUrgencyLevel(source.getUrgencyLevel());
        target.setLatitude(source.getLatitude());
        target.setLongitude(source.getLongitude());
        target.setHospitalName(source.getHospitalName());
        target.setHospitalAddress(source.getHospitalAddress());
        target.setAdditionalNotes(source.getAdditionalNotes());
        target.setExpiresAt(source.getExpiresAt());
    }

    @Override
    public BloodResponse getBloodRequestById(Long id) {
        log.info("Fetching blood request by ID: {}", id);
        return bloodRequestRepository.findById(id)
                .map(BloodResponse::new)
                .orElseThrow(() -> new EntityNotFoundException(BLOOD_REQUEST_NOT_FOUND + id));
    }

    @Override
    public void deleteBloodRequest(Long id) {
        log.info("Deleting blood request with ID: {}", id);
        if (!bloodRequestRepository.existsById(id)) {
            throw new EntityNotFoundException(BLOOD_REQUEST_NOT_FOUND + id);
        }
        bloodRequestRepository.deleteById(id);
        log.info("Blood request with ID: {} deleted successfully", id);
    }

    @Override
    public Page<BloodResponse> getAllBloodRequests(Pageable pageable) {
        log.info("Fetching all blood requests");
        Page<BloodRequestEntity> bloodRequestPage = bloodRequestRepository.findAll(pageable);
        return bloodRequestPage.map(BloodResponse::new);
    }

    @Override
    public Page<BloodResponse> getBloodRequestsByLoggedInUser(Pageable pageable) {
        log.info("Fetching blood requests by logged-in user");
        User loggedInUser = loggedInUserUtil.getLoggedInUser();
        Page<BloodRequestEntity> bloodRequestPage = bloodRequestRepository.findAllByUser(loggedInUser, pageable).orElseThrow(
                () -> new EntityNotFoundException("No blood requests found for the logged-in user.")
        );
        return bloodRequestPage.map(BloodResponse::new);
    }

    @Override
    public Page<BloodResponse> getBloodRequestsByBloodGroup(BloodGroup bloodGroup, UrgencyLevel urgencyLevel, Pageable pageable) {
        log.info("Fetching blood requests by blood group: {} and urgency level: {}", bloodGroup, urgencyLevel);
        Page<BloodRequestEntity> bloodRequestPage = bloodRequestRepository.findAllByBloodTypeNeededAndUrgencyLevel(bloodGroup, urgencyLevel, pageable).orElseThrow(
                () -> new EntityNotFoundException("No blood requests found for the specified blood group and urgency level.")
        );
        if (bloodRequestPage.isEmpty()) {
            throw new EntityNotFoundException("No blood requests found for the specified criteria.");
        }
        return bloodRequestPage.map(BloodResponse::new);
    }
}

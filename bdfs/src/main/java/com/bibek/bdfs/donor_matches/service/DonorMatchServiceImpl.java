package com.bibek.bdfs.donor_matches.service;

import com.bibek.bdfs.donor_matches.dto.DonorMatchResponse;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import com.bibek.bdfs.donor_matches.repository.DonorMatchRepository;
import com.bibek.bdfs.util.logged_in_user.LoggedInUserUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonorMatchServiceImpl implements DonorMatchService{
    private final DonorMatchRepository donorMatchRepository;
    private final LoggedInUserUtil loggedInUserUtil;
    @Override
    public DonorMatchResponse updateDonorMatchResponse(Long id, ResponseStatus responseStatus) {
        log.info("Updating donor match response for ID: {}, Status: {}", id, responseStatus);
        // Fetch the match by ID
        RequestDonorMatch match = donorMatchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Donor match not found with ID: " + id));
        // Update the response status
        match.setResponseStatus(responseStatus);
        // Save the updated match
        donorMatchRepository.save(match);
        return new DonorMatchResponse(match);
    }

    @Override
    public Page<DonorMatchResponse> getDonorMatchesByLoginUser(Pageable pageable) {
        log.info("Fetching donor matches for logged-in user");

        Page<RequestDonorMatch> matches = donorMatchRepository.findAllByDonor(
                loggedInUserUtil.getLoggedInUser(), pageable).orElseThrow(
                () -> new EntityNotFoundException("No donor matches found for the logged-in user")
        );

        return matches.map(DonorMatchResponse::new);
    }
}

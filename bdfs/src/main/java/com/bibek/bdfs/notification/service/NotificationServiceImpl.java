package com.bibek.bdfs.notification.service;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.mail.MailService;
import com.bibek.bdfs.notification.dto.NotificationResponse;
import com.bibek.bdfs.notification.entity.Notification;
import com.bibek.bdfs.notification.entity.NotificationType;
import com.bibek.bdfs.notification.repository.NotificationRepository;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.util.logged_in_user.LoggedInUserUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    private final LoggedInUserUtil loggedInUserUtil;
    private final MailService mailService;
    @Override
    public void notifyDonor(RequestDonorMatch match) {
        User donor = match.getDonor();
        BloodRequestEntity request = match.getBloodRequest();
        String message = String.format(
                "New blood request matched for you! " +
                "Blood Type: %s, Hospital: %s, Address: %s, " +
                "Longitude: %s, Latitude: %s, Additional Notes: %s, " +
                "Quantity: %s, Expires At: %s, Status: %s, Urgency Level: %s",

                request.getBloodTypeNeeded().getValue(),
                request.getHospitalName(),
                request.getHospitalAddress(),
                request.getLongitude(),
                request.getLatitude(),
                request.getAdditionalNotes(),
                request.getQuantity(),
                request.getExpiresAt(),
                request.getStatus(),
                request.getUrgencyLevel().toString()
        );

        Notification notification = Notification.builder()
                .user(donor)
                .message(message)
                .relatedRequest(request)
                .notificationType(NotificationType.REQUEST_MATCH)
                .build();

        notificationRepository.save(notification);

        log.info("Notification created for donor: {} with message: {}", donor.getId(), message);
        mailService.bloodRequestNotificationMail(
                donor,
                message,
                request.getExpiresAt()
        );
    }

    @Override
    public Page<NotificationResponse> getNotificationsByLoginUser(Pageable pageable) {
        log.info("Fetching notifications for logged-in user");
        User loggedInUser = loggedInUserUtil.getLoggedInUser();
        Page<Notification> notifications = notificationRepository.findAllByUser(loggedInUser, pageable)
                .orElseThrow(() -> new EntityNotFoundException("No notifications found for the logged-in user"));

        return notifications.map(NotificationResponse::new);
    }
}

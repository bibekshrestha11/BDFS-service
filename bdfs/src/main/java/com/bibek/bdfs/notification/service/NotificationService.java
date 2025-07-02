package com.bibek.bdfs.notification.service;

import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.notification.dto.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void notifyDonor(RequestDonorMatch match);
    Page<NotificationResponse> getNotificationsByLoginUser(Pageable pageable);
}

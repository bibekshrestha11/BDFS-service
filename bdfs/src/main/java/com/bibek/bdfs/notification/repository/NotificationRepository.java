package com.bibek.bdfs.notification.repository;

import com.bibek.bdfs.notification.entity.Notification;
import com.bibek.bdfs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Page<Notification>> findAllByUser(User user, Pageable pageable);
}

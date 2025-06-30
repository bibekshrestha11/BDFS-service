package com.bibek.bdfs.blood_request.repository;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequestEntity, Long> {
    Optional<Page<BloodRequestEntity>> findAllByUser(User user, Pageable pageable);
    Optional<Page<BloodRequestEntity>> findAllByBloodTypeNeededAndUrgencyLevel(BloodGroup bloodTypeNeeded, UrgencyLevel urgencyLevel, Pageable pageable);
}

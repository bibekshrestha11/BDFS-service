package com.bibek.bdfs.donor_matches.repository;

import com.bibek.bdfs.blood_request.entity.BloodRequestStatus;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorMatchRepository extends JpaRepository<RequestDonorMatch, Long> {

    @Query("SELECT match FROM RequestDonorMatch match " +
            "WHERE match.donor = :donor " +
            "AND match.bloodRequest.status IN :bloodRequestStatus")
    Page<RequestDonorMatch> findAllByDonorAndBloodRequestStatus(User donor, List<BloodRequestStatus> bloodRequestStatus, Pageable pageable);
}

package com.bibek.bdfs.donor_matches.repository;

import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonorMatchRepository extends JpaRepository<RequestDonorMatch, Long> {
    Optional<Page<RequestDonorMatch>> findAllByDonor(User donor, Pageable pageable);
}

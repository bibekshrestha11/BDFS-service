package com.bibek.bdfs.user.repository;

import com.bibek.bdfs.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailId(String emailId);
}

package com.bibek.bdfs.user.service;

import com.bibek.bdfs.user.dto.request.UserUpdateRequest;
import com.bibek.bdfs.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse updateUser(UserUpdateRequest updateRequest);

    UserResponse getLoggedInUser();
}

package com.bibek.bdfs.user.service.implementation;

import com.bibek.bdfs.user.dto.request.UserUpdateRequest;
import com.bibek.bdfs.user.dto.response.UserResponse;
import com.bibek.bdfs.user.entity.User;
import com.bibek.bdfs.user.repository.UserRepository;
import com.bibek.bdfs.user.service.UserService;
import com.bibek.bdfs.util.file.FileHandlerUtil;
import com.bibek.bdfs.util.file.FileType;
import com.bibek.bdfs.util.logged_in_user.LoggedInUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final LoggedInUserUtil loggedInUserUtil;
    private final FileHandlerUtil fileHandlerUtil;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Fetching all users with pagination: {}", pageable);
        Page<User> users = userRepository.findAllByIsVerifiedTrue(pageable);
        return users.map(UserResponse::new);
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest updateRequest) {
        User user = loggedInUserUtil.getLoggedInUser();

        user.setFullName(updateRequest.getFullName());
        user.setPhoneNumber(updateRequest.getPhone());
        user.setBirthDate(updateRequest.getBirthDate());
        user.setBloodGroup(updateRequest.getBloodGroup());
        user.setLocation(updateRequest.getLocation());
        user.setLatitude(updateRequest.getLatitude() != null ? updateRequest.getLatitude() : 0.0);
        user.setLongitude(updateRequest.getLongitude() != null ? updateRequest.getLongitude() : 0.0);

        // Handle profile image if provided
        if (updateRequest.getProfileImage() != null && !updateRequest.getProfileImage().isEmpty()) {
            FileType fileType = fileHandlerUtil.determineFileType(Objects.requireNonNull(updateRequest.getProfileImage().getOriginalFilename()));
            if (fileType == FileType.IMAGE) {
                String fileName = fileHandlerUtil.saveFile(updateRequest.getProfileImage(), user.getEmailId()).getFileDownloadUri();
                user.setProfileImage(fileName);
            }
        }
        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }

    @Override
    public UserResponse getLoggedInUser() {
        User user = loggedInUserUtil.getLoggedInUser();
        log.info("Fetching logged-in user: {}", user.getEmailId());
        return new UserResponse(user);
    }
}

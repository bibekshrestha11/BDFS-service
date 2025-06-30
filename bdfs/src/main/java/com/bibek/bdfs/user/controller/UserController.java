package com.bibek.bdfs.user.controller;

import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.response.ApiResponse;
import com.bibek.bdfs.user.dto.request.UserUpdateRequest;
import com.bibek.bdfs.user.dto.response.UserResponse;
import com.bibek.bdfs.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bibek.bdfs.user.messages.UserAPIConstants.API_USER;

@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management APIs")
public class UserController extends BaseController {

    private final UserService userService;

    @Operation(
            summary = "Update logged-in user profile",
            description = "Updates the profile of the currently logged-in user."
    )
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@ModelAttribute UserUpdateRequest updateRequest) {
        UserResponse updatedUser = userService.updateUser(updateRequest);
        return successResponse(updatedUser, "User profile updated successfully");
    }

    @Operation(
            summary = "Get all verified users",
            description = "Returns a paginated list of all verified users."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable) {
        return successResponse(userService.getAllUsers(pageable), "Fetched all verified users successfully");
    }

    @Operation(
            summary = "Get logged-in user",
            description = "Returns the profile of the currently logged-in user."
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getLoggedInUser() {
        UserResponse loggedInUser = userService.getLoggedInUser();
        return successResponse(loggedInUser, "Fetched logged-in user profile successfully");
    }
}
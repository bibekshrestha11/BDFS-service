package com.bibek.bdfs.user.controller;

import com.bibek.bdfs.auth.dto.request.UserRegistrationRequest;
import com.bibek.bdfs.auth.dto.response.UserRegistrationResponse;
import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.response.ApiResponse;
import com.bibek.bdfs.user.messages.UserSwaggerDocumentationMessage;
import com.bibek.bdfs.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bibek.bdfs.user.messages.UserAPIConstants.API_USER;


@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
@Slf4j
public class UserController extends BaseController {

    private static final String USER = "User";
    private final UserService userService;

    @Operation(
            summary = UserSwaggerDocumentationMessage.ADD_USER_SUMMARY,
            description = UserSwaggerDocumentationMessage.ADD_USER_DESCRIPTION
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<UserRegistrationResponse>> addUser(@RequestBody UserRegistrationRequest userRequest){
//        return successResponse(userService.registerSchoolUser(userRequest), ResponseMessageUtil.createdSuccessfully(USER));
        return null;
    }

}
package com.bibek.bdfs.blood_request.controller;

import com.bibek.bdfs.blood_request.dto.BloodRequest;
import com.bibek.bdfs.blood_request.dto.BloodResponse;
import com.bibek.bdfs.blood_request.service.BloodRequestService;
import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blood-requests")
public class BloodRequestController extends BaseController {
    private final BloodRequestService bloodRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<BloodResponse>> createBloodRequest(@RequestBody  BloodRequest bloodRequest) {
        return successResponse(bloodRequestService.createBloodRequest(bloodRequest), "Blood request created successfully");
    }
}

package com.bibek.bdfs.blood_request.controller;

import com.bibek.bdfs.blood_request.dto.BloodRequest;
import com.bibek.bdfs.blood_request.dto.BloodResponse;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.blood_request.service.BloodRequestService;
import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.response.ApiResponse;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blood-requests")
public class BloodRequestController extends BaseController {
    private final BloodRequestService bloodRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<BloodResponse>> createBloodRequest(@RequestBody BloodRequest bloodRequest) {
        return successResponse(bloodRequestService.createBloodRequest(bloodRequest), "Blood request created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BloodResponse>>> getAllBloodRequests(Pageable pageable) {
        return successResponse(bloodRequestService.getAllBloodRequests(pageable), "Fetched all blood requests successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BloodResponse>> getBloodRequestById(@PathVariable Long id) {
        return successResponse(bloodRequestService.getBloodRequestById(id), "Fetched blood request successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BloodResponse>> updateBloodRequest(@PathVariable Long id, @RequestBody BloodRequest bloodRequest) {
        return successResponse(bloodRequestService.updateBloodRequest(id, bloodRequest), "Blood request updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBloodRequest(@PathVariable Long id) {
        bloodRequestService.deleteBloodRequest(id);
        return successResponse(null, "Blood request deleted successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Page<BloodResponse>>> getBloodRequestsByLoggedInUser(Pageable pageable) {
        return successResponse(bloodRequestService.getBloodRequestsByLoggedInUser(pageable), "Fetched blood requests by logged-in user successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BloodResponse>>> getBloodRequestsByBloodGroup(
            @RequestParam("bloodGroup") String bloodGroup,
            @RequestParam("urgencyLevel") String urgencyLevel,
            Pageable pageable) {
        return successResponse(
                bloodRequestService.getBloodRequestsByBloodGroup(
                        BloodGroup.valueOf(bloodGroup.toUpperCase()),
                        UrgencyLevel.valueOf(urgencyLevel.toUpperCase()),
                        pageable),
                "Fetched blood requests by blood group and urgency level successfully");
    }




}

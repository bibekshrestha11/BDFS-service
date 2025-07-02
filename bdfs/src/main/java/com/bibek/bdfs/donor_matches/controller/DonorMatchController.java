package com.bibek.bdfs.donor_matches.controller;

import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.donor_matches.dto.DonorMatchResponse;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import com.bibek.bdfs.donor_matches.service.DonorMatchService;
import com.bibek.bdfs.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/donor-matches")
public class DonorMatchController extends BaseController {
    private final DonorMatchService donorMatchService;

    @PutMapping("/status/{id}")
    public ResponseEntity<ApiResponse<DonorMatchResponse>> updateDonorMatchStatus(@PathVariable Long id,
                                                                                  @RequestParam (defaultValue = "ACCEPTED") ResponseStatus responseStatus) {
        return successResponse(donorMatchService.updateDonorMatchResponse(
                id, responseStatus
        ), "Donor match response updated successfully");
    }

    @GetMapping("/my-matches")
    public ResponseEntity<ApiResponse<Page<DonorMatchResponse>>> getMyDonorMatches(Pageable pageable) {
        return successResponse(donorMatchService.getDonorMatchesByLoginUser(pageable),
                "Fetched donor matches successfully");
    }
}

package com.bibek.bdfs.donor_matches.dto;

import com.bibek.bdfs.blood_request.dto.BloodResponse;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DonorMatchResponse {
    private Long id;
    private BloodResponse bloodRequest;
    private String donorName;
    private LocalDateTime notifiedAt;
    private ResponseStatus responseStatus;

    public DonorMatchResponse(RequestDonorMatch requestDonorMatch){
        this.id = requestDonorMatch.getId();
        bloodRequest = new BloodResponse(requestDonorMatch.getBloodRequest());
        this.donorName = requestDonorMatch.getDonor().getFullName();
        this.notifiedAt = requestDonorMatch.getNotifiedAt();
        this.responseStatus = requestDonorMatch.getResponseStatus();
    }
}

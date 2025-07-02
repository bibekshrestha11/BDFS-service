package com.bibek.bdfs.blood_request.dto;

import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.donor_matches.entity.ResponseStatus;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RequestDonorMatchResponse {
    private Long id;
    private Long bloodRequestId;
    private String donorName;
    private String donorPhoneNumber;
    private BloodGroup donorBloodGroup;
    private LocalDateTime notifiedAt;
    private ResponseStatus responseStatus;

    public RequestDonorMatchResponse(RequestDonorMatch requestDonorMatch){
        this.id = requestDonorMatch.getId();
        bloodRequestId = requestDonorMatch.getBloodRequest().getId();
        this.donorName = requestDonorMatch.getDonor().getFullName();
        this.donorPhoneNumber = requestDonorMatch.getDonor().getPhoneNumber();
        this.donorBloodGroup = requestDonorMatch.getDonor().getBloodGroup();
        this.notifiedAt = requestDonorMatch.getNotifiedAt();
        this.responseStatus = requestDonorMatch.getResponseStatus();
    }
}

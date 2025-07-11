package com.bibek.bdfs.blood_request.dto;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.blood_request.entity.BloodRequestStatus;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BloodResponse {
    private BloodRequestUserResponse user;
    private BloodGroup bloodTypeNeeded;
    private int quantity;
    private UrgencyLevel urgencyLevel;
    private double latitude;
    private double longitude;
    private String hospitalName;
    private String hospitalAddress;
    private String additionalNotes;
    private BloodRequestStatus status;
    private LocalDateTime expiresAt;

    private List<RequestDonorMatchResponse> matches;
    private List<DonationResponse> donations;

    public BloodResponse(BloodRequestEntity bloodRequestEntity){
        this.bloodTypeNeeded = bloodRequestEntity.getBloodTypeNeeded();
        this.quantity = bloodRequestEntity.getQuantity();
        this.urgencyLevel = bloodRequestEntity.getUrgencyLevel();
        this.latitude = bloodRequestEntity.getLatitude();
        this.longitude = bloodRequestEntity.getLongitude();
        this.hospitalName = bloodRequestEntity.getHospitalName();
        this.hospitalAddress = bloodRequestEntity.getHospitalAddress();
        this.additionalNotes = bloodRequestEntity.getAdditionalNotes();
        this.status = bloodRequestEntity.getStatus();
        this.expiresAt = bloodRequestEntity.getExpiresAt();
        this.user = new BloodRequestUserResponse(bloodRequestEntity.getUser());
        if (bloodRequestEntity.getMatches() != null) {
            this.matches = bloodRequestEntity.getMatches().stream()
                    .map(RequestDonorMatchResponse::new)
                    .toList();
        }
        if (bloodRequestEntity.getDonations() != null) {
            this.donations = bloodRequestEntity.getDonations().stream()
                    .map(DonationResponse::new)
                    .toList();
        }
    }
}

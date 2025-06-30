package com.bibek.bdfs.blood_request.dto;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.blood_request.entity.BloodRequestStatus;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String expiresAt;

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
    }
}

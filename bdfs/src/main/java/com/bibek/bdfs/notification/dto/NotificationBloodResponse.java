package com.bibek.bdfs.notification.dto;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.blood_request.entity.BloodRequestStatus;
import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationBloodResponse {
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

    public NotificationBloodResponse(BloodRequestEntity bloodRequestEntity) {
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

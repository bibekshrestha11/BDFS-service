package com.bibek.bdfs.blood_request.dto;

import com.bibek.bdfs.blood_request.entity.UrgencyLevel;
import com.bibek.bdfs.user.entity.BloodGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequest {
    private BloodGroup bloodTypeNeeded;
    private int quantity;
    private UrgencyLevel urgencyLevel;
    private double latitude;
    private double longitude;
    private String hospitalName;
    private String hospitalAddress;
    private String additionalNotes;
}

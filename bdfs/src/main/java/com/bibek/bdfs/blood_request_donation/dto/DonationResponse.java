package com.bibek.bdfs.blood_request_donation.dto;

import com.bibek.bdfs.blood_request_donation.entity.Donation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DonationResponse {
    private Long id;
    private Long bloodRequestId;
    private String donorName;
    private LocalDateTime donationDate;
    private int quantity;

    public DonationResponse(Donation donation){
        this.id = donation.getId();
        this.bloodRequestId = donation.getRequest().getId();
        this.donorName = donation.getDonor().getFullName();
        this.donationDate = donation.getDonationDate();
        this.quantity = donation.getQuantity();
    }
}

package com.bibek.bdfs.blood_request_donation.entity;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BloodRequestEntity request;

    @ManyToOne(fetch = FetchType.LAZY)
    private User donor;

    private LocalDateTime donationDate;

    @Column(nullable = false)
    private int quantity;
}

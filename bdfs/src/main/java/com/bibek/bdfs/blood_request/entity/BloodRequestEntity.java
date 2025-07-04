package com.bibek.bdfs.blood_request.entity;

import com.bibek.bdfs.blood_request_donation.entity.Donation;
import com.bibek.bdfs.common.Auditable;
import com.bibek.bdfs.donor_matches.entity.RequestDonorMatch;
import com.bibek.bdfs.user.entity.BloodGroup;
import com.bibek.bdfs.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BloodRequestEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodTypeNeeded;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgencyLevel;
    private double latitude;
    private double longitude;
    private String hospitalName;
    private String hospitalAddress;
    private String additionalNotes;
    private BloodRequestStatus status;
    private LocalDateTime expiresAt;

    // Relationships
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestDonorMatch> matches;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donation> donations;
}

package com.bibek.bdfs.donor_matches.entity;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDonorMatch {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private BloodRequestEntity bloodRequest;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

    @Column(nullable = false)
    private double matchScore;

    private Instant notifiedAt;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus = ResponseStatus.PENDING;
}
package com.bibek.bdfs.donor_matches.entity;

import com.bibek.bdfs.blood_request.entity.BloodRequestEntity;
import com.bibek.bdfs.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDonorMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BloodRequestEntity bloodRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    private User donor;

    private LocalDateTime notifiedAt;

    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus = ResponseStatus.PENDING;
}
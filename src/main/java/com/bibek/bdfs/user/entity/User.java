package com.bibek.bdfs.user.entity;

import com.bibek.bdfs.common.Auditable;
import com.bibek.bdfs.user.role.entity.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name="users")
public class User extends Auditable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "email_id", unique = true)
    private String emailId;

    private String profileImage;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "location", unique  = true)
    private String location;

    @Column(name = "blood_type",unique  = true)  // make nullable
    private String bloodType;

    @Column(name = "age", unique  = true)  // make nullable
    private String age;


    @Column(nullable = false, name = "is_verified")
    private boolean isVerified = false;

    @Column(nullable = false, name = "is_active")
    private boolean isActive = true;
}
package com.bibek.bdfs.user.dto.response;

import com.bibek.bdfs.user.role.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesResponse {
    private String role;
    private String description;

    public RolesResponse(Roles role) {
        this.role = role.getName();
        this.description = role.getDescription();
    }
}

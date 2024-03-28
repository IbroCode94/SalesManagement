package org.sales.salesmanagement.utils;

import lombok.Builder;
import lombok.Data;

import org.sales.salesmanagement.enums.RegistrationState;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Data
@Builder
public class UserDetailsDto {

    private String privilege;
    private String mobile;
    private String password;
    private String email;
    private String firstName;
    private boolean isAccountNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonLocked;
    private String username;
}

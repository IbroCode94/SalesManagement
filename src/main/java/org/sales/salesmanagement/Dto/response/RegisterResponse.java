package org.sales.salesmanagement.Dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sales.salesmanagement.Dto.dtorequest.CustomerRegisterDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String message;
    private String email;
    private String firstName;
    private String role;
}

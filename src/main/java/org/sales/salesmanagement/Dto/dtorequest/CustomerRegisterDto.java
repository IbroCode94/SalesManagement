package org.sales.salesmanagement.Dto.dtorequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\+234[0-9]{8,10}", message = "Phone number must start with '+234' and have 8 to 10 digits")
    private String mobile;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "Password must be 8 to 12 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]).*$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;
    private String address;
    private String roles;
}

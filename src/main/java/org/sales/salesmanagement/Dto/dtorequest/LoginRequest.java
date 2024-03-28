package org.sales.salesmanagement.Dto.dtorequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8,message = "Password must be 8 to 12 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?]).*$", message = "Password must contain at least one uppercase letter and one special character")
    private String password;
    private String email;
}
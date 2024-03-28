package org.sales.salesmanagement.service;

import org.sales.salesmanagement.Dto.dtorequest.LoginRequest;
import org.sales.salesmanagement.Dto.response.LoginResponse;

public interface AuthService {
    public LoginResponse authenticateUser(LoginRequest loginRequest);
}

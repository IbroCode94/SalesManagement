package org.sales.salesmanagement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.sales.salesmanagement.Dto.dtorequest.LoginRequest;
import org.sales.salesmanagement.Dto.response.LoginResponse;
import org.sales.salesmanagement.Exceptions.CustomerNotFoundException;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.AuthService;
import org.sales.salesmanagement.utils.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Customers customer = customerRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with email: " + loginRequest.getEmail() + " not found"));
        if (customer.isDeleted()) {
            throw new CustomerNotFoundException("Customer with email: " + loginRequest.getEmail() + " is deleted and cannot log in.");
        }
        customerRepository.save(customer);
        authenticateSpringSecurity(loginRequest.getEmail(), loginRequest.getPassword());

        String jwtToken = jwtService.generateToken(customer);

        return createLoginResponse(customer, jwtToken);
    }
    private void authenticateSpringSecurity(String email, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authenticated = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }
    private LoginResponse createLoginResponse(Customers customer, String jwtToken) {
        return LoginResponse.builder()
                .userID(Objects.requireNonNull(customer.getId()).toString())
                .accessToken(jwtToken)
                .userType(customer.getRoles().toString())
                .build();
    }
}

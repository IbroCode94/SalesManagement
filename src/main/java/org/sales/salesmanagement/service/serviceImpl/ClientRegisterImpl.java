package org.sales.salesmanagement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.sales.salesmanagement.Dto.dtorequest.CustomerRegisterDto;
import org.sales.salesmanagement.Dto.response.ApiResponse;
import org.sales.salesmanagement.Dto.response.RegisterResponse;
import org.sales.salesmanagement.Exceptions.ResourceNotFoundException;

import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.AuditTrailService;
import org.sales.salesmanagement.service.ClientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientRegisterImpl implements ClientService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditTrailService auditTrailService;

    @Override
    public List<RegisterResponse> getAllClients() {
        List<Customers> users = customerRepository.findAll();
        return users.stream()
                .map(client -> modelMapper.map(client, RegisterResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<RegisterResponse> createClient(CustomerRegisterDto request) {
        Optional<Customers> client = customerRepository.findByEmail(request.getEmail());
        if (client.isPresent()) {
            throw new ResourceNotFoundException( "Client with email " + request.getEmail() + " already exists");
        }

        Customers newCustomers = modelMapper.map(request, Customers.class);
        newCustomers.setCreatedAt(LocalDateTime.now());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newCustomers.setPassword(encodedPassword);
        newCustomers.setWalletBal(100000.0);

        try {
            Roles role = Enum.valueOf(Roles.class, request.getRole());
            newCustomers.setRoles(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role: " + request.getRole());
        }

        Customers savedCustomers = customerRepository.save(newCustomers);
        // TODO:
        auditTrailService.saveAuditTrail(savedCustomers, UserAction.USER_CREATED, savedCustomers.getEmail());
        RegisterResponse registerResponse = modelMapper.map(savedCustomers, RegisterResponse.class);
        registerResponse.setRole(savedCustomers.getRoles().toString());

        return new ApiResponse<>(200, "Client created successfully", registerResponse);
    }


    @Override
    public RegisterResponse updateClient(Long clientId, CustomerRegisterDto request) {
        Customers existingCustomers = getClientById(clientId);
        modelMapper.map(request, existingCustomers);
        existingCustomers.setRoles(request.getRole() != null ? Enum.valueOf(Roles.class, request.getRole()) : Roles.USER);
        Customers updatedCustomers = customerRepository.save(existingCustomers);
        auditTrailService.saveAuditTrail(updatedCustomers, UserAction.USER_UPDATED, updatedCustomers.getEmail());
        return modelMapper.map(updatedCustomers, RegisterResponse.class);
    }

    @Override
    public void deleteClient(Long clientId) {
        Customers clientToDelete = customerRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        customerRepository.deleteById(clientId);
        auditTrailService.saveAuditTrail(clientToDelete, UserAction.USER_DELETED, clientToDelete.getEmail());
    }

    private Customers getClientById(Long clientId) {
        return customerRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
    }
}

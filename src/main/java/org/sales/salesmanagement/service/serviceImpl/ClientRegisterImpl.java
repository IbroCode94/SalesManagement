package org.sales.salesmanagement.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.sales.salesmanagement.Dto.dtorequest.CustomerRegisterDto;
import org.sales.salesmanagement.Dto.response.ApiResponse;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.RegisterResponse;
import org.sales.salesmanagement.Dto.response.SalesResponse;
import org.sales.salesmanagement.Exceptions.ResourceNotFoundException;

import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.Repository.ProductRepository;
import org.sales.salesmanagement.Repository.SalesRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.enums.UserAction;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.models.Product;
import org.sales.salesmanagement.models.Sales;
import org.sales.salesmanagement.service.AuditTrailService;
import org.sales.salesmanagement.service.ClientService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegisterImpl implements ClientService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditTrailService auditTrailService;

    @Override
    public List<GenericResponse> getAllClients() {
        List<Customers> users = customerRepository.findByIsDeletedFalse();
        if (users.isEmpty()) {
            return Collections.singletonList(new GenericResponse("400","No clients found.", null));
        }

        return users.stream()
                .map(client -> modelMapper.map(client, GenericResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApiResponse<RegisterResponse> createClient(CustomerRegisterDto request) {
        Optional<Customers> client = customerRepository.findByEmail(request.getEmail());
        log.info("Client email===========" + request.getEmail());
        if (client.isPresent()) {
            throw new ResourceNotFoundException( "Client with email " + request.getEmail() + " already exists");
        }

        Customers newCustomers = modelMapper.map(request, Customers.class);
        newCustomers.setCreatedAt(LocalDateTime.now());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newCustomers.setPassword(encodedPassword);
        try {
            log.info("Role from request: " + request.getRoles());
            Roles role = Enum.valueOf(Roles.class, request.getRoles());
            if (role == Roles.USER) {
                newCustomers.setWalletBal(BigDecimal.valueOf(100000.0));
            } else if (role == Roles.SELLER) {
                newCustomers.setWalletBal(BigDecimal.ZERO);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role: " + request.getRoles());
        }


        Customers savedCustomers = customerRepository.save(newCustomers);
        auditTrailService.saveAuditTrail(savedCustomers, UserAction.USER_CREATED, savedCustomers.getEmail());
        RegisterResponse registerResponse = modelMapper.map(savedCustomers, RegisterResponse.class);
        registerResponse.setRole(savedCustomers.getRoles().toString());

        return new ApiResponse<>(200, "Client created successfully", registerResponse);
    }


    @Override
    public RegisterResponse updateClient(Long clientId, CustomerRegisterDto request) {
        Customers existingCustomers = getClientById(clientId);
        if (existingCustomers.isDeleted()) {
            throw new ResourceNotFoundException("Client with ID " + clientId + " is deleted and cannot be updated.");
        }

        modelMapper.map(request, existingCustomers);
        existingCustomers.setRoles(request.getRoles() != null ? Enum.valueOf(Roles.class, request.getRoles()) : Roles.USER);
        Customers updatedCustomers = customerRepository.save(existingCustomers);
        auditTrailService.saveAuditTrail(updatedCustomers, UserAction.USER_UPDATED, updatedCustomers.getEmail());
        return modelMapper.map(updatedCustomers, RegisterResponse.class);
    }

    @Override
    public GenericResponse deleteClient(Long clientId) {
        log.info("CLIENT id===============" + clientId);
            log.info("Deleting client with id: " + clientId);
            Optional<Customers> optionalCustomer = customerRepository.findById(clientId);
            if (optionalCustomer.isPresent()) {
                Customers customer = optionalCustomer.get();
                // TODO Set isDeleted to true and save the customer
                customer.setDeleted(true);
                customerRepository.save(customer);

                auditTrailService.saveAuditTrail(customer, UserAction.USER_DELETED, customer.getEmail());

                log.info("Client successfully deleted with id: " + clientId);

                return new GenericResponse("200", "Client with ID " + clientId + " deleted successfully.", null);
            } else {
                log.error("Client not found with id: " + clientId);
                return new GenericResponse("404", "Client with ID " + clientId + " not found.", null);
            }

    }


    private Customers getClientById(Long clientId) {
        return customerRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));
    }
}

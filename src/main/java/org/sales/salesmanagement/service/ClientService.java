package org.sales.salesmanagement.service;

import org.sales.salesmanagement.Dto.dtorequest.CustomerRegisterDto;
import org.sales.salesmanagement.Dto.response.ApiResponse;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.RegisterResponse;

import java.util.List;

public interface ClientService {
    List<GenericResponse> getAllClients();
   ApiResponse<RegisterResponse> createClient(CustomerRegisterDto request);
    RegisterResponse updateClient(Long clientId, CustomerRegisterDto request);
    GenericResponse deleteClient(Long clientId);
}

package org.sales.salesmanagement.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sales.salesmanagement.Dto.dtorequest.CustomerRegisterDto;
import org.sales.salesmanagement.Dto.response.ApiResponse;
import org.sales.salesmanagement.Dto.response.GenericResponse;
import org.sales.salesmanagement.Dto.response.RegisterResponse;
import org.sales.salesmanagement.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/new")
@RequiredArgsConstructor
public class RegistrationController {

    private final ClientService clientService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>>createClient(@Valid @RequestBody CustomerRegisterDto request) {
        ApiResponse<RegisterResponse> client  = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<GenericResponse>> getAllClients() {
        List<GenericResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
    @PutMapping("/{clientId}")
    public ResponseEntity<RegisterResponse> updateClient(@PathVariable Long clientId, @Valid @RequestBody CustomerRegisterDto request) {
        RegisterResponse updatedClient = clientService.updateClient(clientId, request);
        return ResponseEntity.ok(updatedClient);
    }
    @DeleteMapping("/{clientId}")
    public ResponseEntity<GenericResponse> deleteClient(@PathVariable Long clientId) {
        GenericResponse response= clientService.deleteClient(clientId);
        return new  ResponseEntity<>(response,HttpStatus.OK);
    }
}

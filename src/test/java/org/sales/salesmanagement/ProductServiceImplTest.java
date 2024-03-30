package org.sales.salesmanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sales.salesmanagement.Dto.dtorequest.ProductDto;
import org.sales.salesmanagement.Dto.response.ProductResponse;
import org.sales.salesmanagement.Repository.CategoryRepository;
import org.sales.salesmanagement.Repository.ProductRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.service.AuditTrailService;
import org.sales.salesmanagement.service.serviceImpl.ProductServiceImpl;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuditTrailService auditTrailService;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        // Mocking authentication for tests
        Customers currentUser = new Customers();
        currentUser.setId(1L);
        currentUser.setEmail("test@example.com");
        currentUser.setRoles(Roles.SELLER);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateProduct_Success() {

        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setQuantity(10);
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setCategoryName("Test Category");


        when(productRepository.findByName(productDto.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse productResponse = productService.createProduct(productDto);
        assertNotNull(productResponse);
        assertEquals("Test Product", productResponse.getName());
        assertEquals(10, productResponse.getQuantity());
        assertEquals(BigDecimal.valueOf(100.0), productResponse.getPrice());

        // Verify interactions with mock objects
        verify(productRepository, times(1)).findByName("Test Product");
        verify(categoryRepository, times(2)).save(any());
        verify(auditTrailService, times(1)).saveProductAuditTrail(any(), any(), any());
    }
}

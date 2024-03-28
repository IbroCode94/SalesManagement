package org.sales.salesmanagement.service.serviceImpl;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sales.salesmanagement.Dto.dtorequest.ProductDto;
import org.sales.salesmanagement.Dto.response.ProductResponse;
import org.sales.salesmanagement.Exceptions.ResourceNotFoundException;
import org.sales.salesmanagement.Repository.CategoryRepository;
import org.sales.salesmanagement.Repository.CustomerRepository;
import org.sales.salesmanagement.Repository.ProductRepository;
import org.sales.salesmanagement.enums.Roles;
import org.sales.salesmanagement.models.Category;
import org.sales.salesmanagement.models.Customers;
import org.sales.salesmanagement.models.Product;
import org.sales.salesmanagement.service.ProductService;
import org.sales.salesmanagement.utils.UserDetailsDto;
import org.sales.salesmanagement.utils.UserUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse createProduct(ProductDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Current Authentication: {}", authentication);
        if (authentication != null && authentication.getPrincipal() instanceof Customers) {
            Customers currentUser = (Customers) authentication.getPrincipal();
            log.info("Current User: {}", currentUser);
            if (currentUser != null && hasRole(currentUser, Roles.CLIENT)) {

                Category category = new Category();
                category.setName(request.getCategoryName());
                categoryRepository.save(category);

                Product product = new Product();
                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setQuantity(request.getQuantity());
                product.setPrice(request.getPrice());
                product.setCategoryId(category);
               Product savedProduct = productRepository.save(product);

                Customers currentUsers = getCurrentUser();
                product.setCreatedBy(currentUsers);

                category.getProducts().add(product); // Add the product to the category's products list
                categoryRepository.save(category);

                ProductResponse productResponse = new ProductResponse();
                productResponse.setName(savedProduct.getName());
                productResponse.setPrice(savedProduct.getPrice());
                productResponse.setQuantity(savedProduct.getQuantity());
                return productResponse;
            } else {
                throw new ResourceNotFoundException("Access denied. User must be logged in as a client.");
            }
        } else {
            throw new ResourceNotFoundException("Access denied. User must be authenticated as a client.");
        }
    }

    private boolean hasRole(Customers user, Roles role) {
        return user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.name()));
    }


    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductDto request) {
        Product existingProduct = getProductById(productId);
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryName()));
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setCategoryId(category);
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setPrice(request.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToProductResponse(updatedProduct);

    }

    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setPrice(product.getPrice());
        // Set other fields as needed
        return productResponse;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private Customers getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Customers) {
            return (Customers) authentication.getPrincipal();
        }
        // Handle case when user is not authenticated or is not an instance of Customers
        return null; // Or throw an exception
    }
}
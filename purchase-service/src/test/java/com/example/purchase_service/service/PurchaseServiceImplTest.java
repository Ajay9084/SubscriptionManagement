package com.example.purchase_service.service;

import com.example.purchase_service.client.SubscriptionClient;
import com.example.purchase_service.dto.request.PurchaseRequest;
import com.example.purchase_service.dto.request.SubscriptionCreationRequest;
import com.example.purchase_service.dto.response.PurchaseResponse;
import com.example.purchase_service.entity.Product;
import com.example.purchase_service.entity.Purchase;
import com.example.purchase_service.exception.DuplicatePurchaseException;
import com.example.purchase_service.exception.ProductNotFoundException;
import com.example.purchase_service.mapper.PurchaseMapper;
import com.example.purchase_service.repository.ProductRepository;
import com.example.purchase_service.repository.PurchaseRepository;
import com.example.purchase_service.service.impl.PurchaseServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @Mock
    private SubscriptionClient subscriptionClient;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    private Product buildProduct(Long id, BigDecimal price) {
        return Product.builder()
                .id(id)
                .name("Test Product")
                .description("Description")
                .price(price)
                .category("SOFTWARE")
                .build();
    }

    private PurchaseRequest buildRequest(Long customerId, Long productId) {
        return new PurchaseRequest(customerId, productId, "CREDIT_CARD");
    }

    @Test
    void createPurchase_success() {
        Product product = buildProduct(101L, new BigDecimal("99.99"));
        PurchaseRequest request = buildRequest(1L, 101L);

        Purchase savedPurchase = Purchase.builder()
                .id(1L)
                .customerId(1L)
                .product(product)
                .totalAmount(product.getPrice())
                .paymentMethod("CREDIT_CARD")
                .purchaseDate(LocalDateTime.now())
                .build();

        PurchaseResponse expectedResponse = PurchaseResponse.builder()
                .purchaseId(1L)
                .customerId(1L)
                .productId(101L)
                .totalAmount(product.getPrice())
                .build();

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(purchaseRepository.existsByCustomerIdAndProduct_Id(1L, 101L)).thenReturn(false);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase);
        when(purchaseMapper.toResponse(savedPurchase)).thenReturn(expectedResponse);

        PurchaseResponse result = purchaseService.createPurchase(request);

        assertNotNull(result);
        assertEquals(1L, result.getPurchaseId());
        verify(subscriptionClient).createSubscription(any(SubscriptionCreationRequest.class));
        verify(purchaseRepository).save(any(Purchase.class));
    }

    @Test
    void createPurchase_productNotFound_throwsProductNotFoundException() {
        PurchaseRequest request = buildRequest(1L, 999L);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> purchaseService.createPurchase(request));

        verifyNoInteractions(purchaseRepository, subscriptionClient);
    }

    @Test
    void createPurchase_duplicatePurchase_throwsDuplicatePurchaseException() {
        Product product = buildProduct(101L, new BigDecimal("49.99"));
        PurchaseRequest request = buildRequest(1L, 101L);

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(purchaseRepository.existsByCustomerIdAndProduct_Id(1L, 101L)).thenReturn(true);

        assertThrows(DuplicatePurchaseException.class,
                () -> purchaseService.createPurchase(request));

        verify(purchaseRepository, never()).save(any());
        verifyNoInteractions(subscriptionClient);
    }

    @Test
    void createPurchase_subscriptionConflict_throwsDuplicatePurchaseException() {
        Product product = buildProduct(101L, new BigDecimal("29.99"));
        PurchaseRequest request = buildRequest(1L, 101L);

        Purchase savedPurchase = Purchase.builder()
                .id(1L)
                .customerId(1L)
                .product(product)
                .totalAmount(product.getPrice())
                .paymentMethod("CREDIT_CARD")
                .purchaseDate(LocalDateTime.now())
                .build();

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(purchaseRepository.existsByCustomerIdAndProduct_Id(1L, 101L)).thenReturn(false);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase);
        doThrow(FeignException.Conflict.class)
                .when(subscriptionClient).createSubscription(any(SubscriptionCreationRequest.class));

        assertThrows(DuplicatePurchaseException.class,
                () -> purchaseService.createPurchase(request));
    }

    @Test
    void createPurchase_totalAmountEqualsProductPrice() {
        BigDecimal price = new BigDecimal("199.00");
        Product product = buildProduct(101L, price);
        PurchaseRequest request = buildRequest(2L, 101L);

        Purchase savedPurchase = Purchase.builder()
                .id(10L)
                .customerId(2L)
                .product(product)
                .totalAmount(price)
                .paymentMethod("CREDIT_CARD")
                .purchaseDate(LocalDateTime.now())
                .build();

        PurchaseResponse expectedResponse = PurchaseResponse.builder()
                .purchaseId(10L)
                .totalAmount(price)
                .build();

        when(productRepository.findById(101L)).thenReturn(Optional.of(product));
        when(purchaseRepository.existsByCustomerIdAndProduct_Id(2L, 101L)).thenReturn(false);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(savedPurchase);
        when(purchaseMapper.toResponse(savedPurchase)).thenReturn(expectedResponse);

        PurchaseResponse result = purchaseService.createPurchase(request);

        assertEquals(price, result.getTotalAmount());
    }
}

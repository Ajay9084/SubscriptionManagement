package com.example.purchase_service.service;


import com.example.purchase_service.dto.request.PurchaseRequest;
import com.example.purchase_service.dto.response.PurchaseResponse;

public interface PurchaseService {
	PurchaseResponse purchaseResponse(PurchaseRequest request);
}

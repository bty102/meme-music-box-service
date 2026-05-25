package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import com.bty.karaoke.mememusicboxservice.entity.ProductOfInvoice;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.ProductOfInvoiceMapper;
import com.bty.karaoke.mememusicboxservice.repository.InvoiceRepository;
import com.bty.karaoke.mememusicboxservice.repository.ProductOfInvoiceRepository;
import com.bty.karaoke.mememusicboxservice.repository.ProductRepository;
import com.bty.karaoke.mememusicboxservice.service.ProductOfInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Validated
public class ProductOfInvoiceServiceImpl implements ProductOfInvoiceService {

    private final ProductOfInvoiceRepository productOfInvoiceRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final ProductOfInvoiceMapper productOfInvoiceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductOfInvoiceResponse createProductOfInvoice(@Valid ProductOfInvoiceCreationRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_CREATE_PRODUCT_OF_INVOICE);
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (!product.getIsActive()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE_TO_CREATE_PRODUCT_OF_INVOICE);
        }

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new AppException(ErrorCode.PRODUCT_STOCK_QUANTITY_NOT_ENOUGH_TO_CREATE_PRODUCT_OF_INVOICE);
        }

        if(productOfInvoiceRepository.existsByProduct_IdAndInvoice_Id(request.getProductId(), request.getInvoiceId())) {
            throw new AppException(ErrorCode.PRODUCT_OF_INVOICE_EXISTED);
        }

        ProductOfInvoice productOfInvoice = productOfInvoiceMapper.toProductOfInvoice(request);
        productOfInvoice.setInvoice(invoice);
        productOfInvoice.setProduct(product);
        BigDecimal lineTotal = product.getUnitPrice().multiply(new BigDecimal(request.getQuantity()));
        productOfInvoice.setLineTotal(lineTotal);
        productOfInvoice = productOfInvoiceRepository.save(productOfInvoice);

        product.setStockQuantity(product.getStockQuantity() - request.getQuantity());
        productRepository.save(product);

        return productOfInvoiceMapper.toProductOfInvoiceResponse(productOfInvoice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductOfInvoiceResponse updateProductOfInvoice(Long id, @Valid ProductOfInvoiceUpdateRequest request) {
        if (id == null) {
            throw new AppException(ErrorCode.PRODUCT_OF_INVOICE_NOT_EXISTED);
        }
        ProductOfInvoice productOfInvoice = productOfInvoiceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_OF_INVOICE_NOT_EXISTED));

        if(!productOfInvoice.getInvoice().getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_UPDATE_PRODUCT_OF_INVOICE);
        }

        Integer currentQuantity = productOfInvoice.getQuantity();

        Product product = productOfInvoice.getProduct();

        if(!product.getIsActive()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE_TO_UPDATE_PRODUCT_OF_INVOICE);
        }

        if(currentQuantity < request.getQuantity()) {
            Integer additionalQuantity = request.getQuantity() - currentQuantity;
            if(product.getStockQuantity() < additionalQuantity) {
                throw new AppException(ErrorCode.PRODUCT_STOCK_QUANTITY_NOT_ENOUGH_TO_UPDATE_PRODUCT_OF_INVOICE);
            }
            product.setStockQuantity(product.getStockQuantity() - additionalQuantity);
        }

        if(currentQuantity > request.getQuantity()) {
            Integer reducedQuantity = currentQuantity - request.getQuantity();
            product.setStockQuantity(product.getStockQuantity() + reducedQuantity);
        }

        productRepository.save(product);

        productOfInvoiceMapper.updateProductOfInvoice(productOfInvoice, request);
        BigDecimal lineTotal = product.getUnitPrice().multiply(new BigDecimal(request.getQuantity()));
        productOfInvoice.setLineTotal(lineTotal);
        productOfInvoice = productOfInvoiceRepository.save(productOfInvoice);
        return productOfInvoiceMapper.toProductOfInvoiceResponse(productOfInvoice);
    }
}

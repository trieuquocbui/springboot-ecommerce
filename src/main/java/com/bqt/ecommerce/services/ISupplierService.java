package com.bqt.ecommerce.services;

import com.bqt.ecommerce.payloads.request.SupplierRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.SupplierResponse;

import java.util.List;

public interface ISupplierService {
    SupplierResponse createSupplier(SupplierRequest supplier);

    SupplierResponse updateSupplier(String idSupplier, SupplierRequest supplierRequest);

    void removeSupplier(String idSupplier);

    PaginationResponse getSupplierList(int page, int limit,String sortDir, String sortBy);

    SupplierResponse findById(String idSupplier);

    List<SupplierResponse> getAllSupplier();

    List<SupplierResponse> getAllActiveSupplier(boolean b);

    PaginationResponse getFindSupplierList(int page, int limit, String sortDir, String sortBy, String search);
}

package com.bqt.ecommerce.services;

import com.bqt.ecommerce.payloads.request.BrandRequest;
import com.bqt.ecommerce.payloads.response.BrandResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

import java.util.List;


public interface IBrandService {
    List<BrandResponse> getAllActiveBrand(boolean b);

    BrandResponse createBrand(String fileName, BrandRequest brandRequest);

    BrandResponse updateBrand(Long idBrand, String fileName, BrandRequest brandRequest);

    void removeBrand(Long idBrand);

    PaginationResponse<BrandResponse> getBrandList(int page, int limit,String sortDir, String sortBy);

    BrandResponse getBrandById(Long idBrand);

    PaginationResponse<BrandResponse> findBrandList(int page, int limit, String sortDir, String sortBy, String search);
}

package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.ProductRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.PriceResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    PaginationResponse getListProduct(int page, int limit, Optional<Long> brandId,String ram,String cpu,
                                      String displaySize,String graphicCard,String operatingSystem,String weight,
                                      String hardDrive,String size,Integer minPrice,Integer maxPrice, String label);

    ProductResponse[] getProductsForCompare(String nameP, String nameP2);

    List<ProductResponse> getProductsNameContaining(String name);

    ProductResponse getProductByName(String name);

    void removeProduct(Long idProduct);

    ProductResponse updateProduct(Long idProduct, String fileName, ProductRequest productRequest);

    ProductResponse createProduct(String fileName, ProductRequest productRequest, Account account);

    PaginationResponse<ProductResponse> getProductList(int page, int limit,String sortDir, String sortBy);

    PaginationResponse getListProductByLabel(String label,int page, int limit);

    ProductResponse getProductById(Long idProduct);

    List<ProductResponse> getAllProduct();

    List<PriceResponse> getAllPriceByProductId(Long productId);

    void deletePrice(Long productId, String date) throws ParseException;

    PaginationResponse getFindProductList(int page, int limit, String sortDir, String sortBy, String search);
}

package com.bqt.ecommerce.services;

import com.bqt.ecommerce.payloads.request.ConfigRequest;
import com.bqt.ecommerce.payloads.response.ConfigResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;

import java.util.List;

public interface IConfigService {
    ConfigResponse createConfig(ConfigRequest configRequest);

    ConfigResponse updateConfig(Long idSupplier, ConfigRequest configRequest);

    void removeConfig(Long idConfig);

    PaginationResponse<ConfigResponse> getConfigList(int page, int limit,String sortDir,String sortBy);

    ConfigResponse findById(Long idConfig);

    List<ConfigResponse> getAllConfig();

    PaginationResponse<ConfigResponse> findConfigList(int page, int limit, String sortDir, String sortBy, String search);
}

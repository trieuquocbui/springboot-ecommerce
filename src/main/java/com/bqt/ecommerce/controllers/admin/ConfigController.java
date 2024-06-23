package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Config;
import com.bqt.ecommerce.payloads.request.ConfigRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.ConfigResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.services.IConfigService;
import com.bqt.ecommerce.constants.PaginationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class ConfigController {
    @Autowired
    private IConfigService configService;

    @PostMapping("config")
    public ResponseEntity<ConfigResponse> createConfig(@RequestBody ConfigRequest configRequest) {

        ConfigResponse configResponse = this.configService.createConfig(configRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(configResponse);
    }

    @PutMapping("config/{configId}")
    public ResponseEntity<ConfigResponse> updateConfig(
            @PathVariable(name = "configId") Long configId,
            @RequestBody ConfigRequest configRequest) {

        ConfigResponse configResponse = this.configService.updateConfig(configId, configRequest);

        return ResponseEntity.status(HttpStatus.OK).body(configResponse);
    }

    @DeleteMapping("config/{configId}")
    public ResponseEntity<ApiResponse> removeConfig(@PathVariable(name = "configId") Long configId) {

        this.configService.removeConfig(configId);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("config")
    public ResponseEntity<PaginationResponse<ConfigResponse>> getConfigList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy) {

        PaginationResponse listSupplier = this.configService.getConfigList(page, limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(listSupplier);
    }

    @GetMapping("config/find")
    public ResponseEntity<PaginationResponse<ConfigResponse>> findConfigList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search) {

        PaginationResponse listSupplier = this.configService.findConfigList(page, limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(listSupplier);
    }

    @GetMapping("config/{configId}")
    public ResponseEntity<ConfigResponse> findConfig(@PathVariable(name = "configId") Long configId) {
        ConfigResponse configResponse = this.configService.findById(configId);
        return ResponseEntity.status(HttpStatus.OK).body(configResponse);
    }

    @GetMapping("config/all")
    public ResponseEntity<List<ConfigResponse>> getAllConfig() {
        List<ConfigResponse> listConfig = this.configService.getAllConfig();
        return ResponseEntity.status(HttpStatus.OK).body(listConfig);
    }
}

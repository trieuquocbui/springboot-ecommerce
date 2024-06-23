package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.payloads.request.BrandRequest;
import com.bqt.ecommerce.payloads.response.BrandResponse;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.services.IBrandService;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.constants.PaginationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController("Controller-Admin")
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class BrandController {

    @Autowired
    private IBrandService IBrandService;

    @Autowired
    private IFileService IFileService;

    @Value("${spring.resources.static-locations}")
    private String path;

    @GetMapping("brand/all")
    public ResponseEntity<List<BrandResponse>> showAllActiveBrand() {
        List<BrandResponse> brandList = this.IBrandService.getAllActiveBrand(true);
        return ResponseEntity.status(HttpStatus.OK).body(brandList);
    }

    @GetMapping("brand")
    public ResponseEntity<PaginationResponse<BrandResponse>> showBrandList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy) {

        PaginationResponse brandList = this.IBrandService.getBrandList(page, limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(brandList);
    }

    @GetMapping("brand/find")
    public ResponseEntity<PaginationResponse<BrandResponse>> findBrandList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search) {

        PaginationResponse brandList = this.IBrandService.findBrandList(page, limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(brandList);
    }

    @PostMapping("brand")
    public ResponseEntity<Object> createBrand(
            @RequestPart("image") MultipartFile file,
            @RequestPart("json") BrandRequest brandRequest) throws IOException {
        if (file != null) {
            String fileName = this.IFileService.uploadImage(path, file);
            BrandResponse brandResponse = this.IBrandService.createBrand(fileName, brandRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(ApiConstant.STATUS_FAIL,ApiConstant.FAIL_CREATION,HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("brand/{brandId}")
    public ResponseEntity<BrandResponse> findBrand(@PathVariable("brandId") Long brandId) throws IOException {
        BrandResponse brandResponse = this.IBrandService.getBrandById(brandId);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
    }

    @PutMapping("brand/{brandId}/upload")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable("brandId") Long brandId,
            @RequestParam("image") MultipartFile file) throws IOException {
        String fileName = this.IFileService.uploadImage(path,file);
        BrandResponse brandResponse = this.IBrandService.updateBrand(brandId,fileName, null);
        return ResponseEntity.status(HttpStatus.OK).body(brandResponse);
    }

    @PutMapping("brand/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable("brandId") Long brandId,
            @RequestBody BrandRequest brandRequest) throws IOException {
        BrandResponse brandResponse = this.IBrandService.updateBrand(brandId, null, brandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(brandResponse);
    }

    @DeleteMapping("brand/{brandId}")
    public ResponseEntity<ApiResponse> deleteBrand(@PathVariable("brandId") Long brandId) {
        this.IBrandService.removeBrand(brandId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK));
    }
}

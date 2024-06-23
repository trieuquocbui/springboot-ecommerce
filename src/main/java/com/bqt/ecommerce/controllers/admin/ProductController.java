package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.PriceRequest;
import com.bqt.ecommerce.payloads.request.ProductRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.PriceResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.services.IPriceService;
import com.bqt.ecommerce.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController("Product-Admin")
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {

    @Autowired
    private IProductService IProductService;

    @Autowired
    private IPriceService priceService;

    @Autowired
    private IFileService IFileService;

    @Value("${spring.resources.static-locations}")
    private String path;

    @GetMapping("product")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy) {

        PaginationResponse pageProduct = this.IProductService.getProductList(page, limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(pageProduct);
    }

    @GetMapping("product/find")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search) {

        PaginationResponse pageProduct = this.IProductService.getFindProductList(page, limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(pageProduct);
    }

    @PostMapping("product")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart("image") MultipartFile file,
            @RequestPart("json") ProductRequest productRequest,
            @CurrentAccount Account account) throws IOException {

        String fileName = this.IFileService.uploadImage(path, file);

        ProductResponse productResponse = this.IProductService.createProduct(fileName, productRequest,account);

        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @PostMapping("product/{productId}/price")
    public ResponseEntity<PriceResponse> createPrice(
            @PathVariable long productId,
            @RequestBody PriceRequest priceRequest,
            @CurrentAccount Account account){

        PriceResponse priceResponse = this.priceService.createPriceForProduct(priceRequest,productId, account);

        return ResponseEntity.status(HttpStatus.CREATED).body(priceResponse);
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable("productId") Long idProduct) {

        ProductResponse productResponse = this.IProductService.getProductById(idProduct);

        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @PutMapping("product/{productId}/upload")
    public ResponseEntity<ProductResponse> updateImageProduct(
            @PathVariable("productId") Long productId,
            @RequestParam("image") MultipartFile file) throws IOException {

        String fileName = this.IFileService.uploadImage(path,file);

        ProductResponse productResponse = this.IProductService.updateProduct(productId,fileName, null);

        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @PutMapping("product/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductRequest productRequest) throws IOException {

        ProductResponse productResponse = this.IProductService.updateProduct(productId, null, productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<ApiResponse> removeProduct(@PathVariable("productId") Long productId) {
        this.IProductService.removeProduct(productId);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("product/all")
    public ResponseEntity<List<ProductResponse>> getAllProduct(){

        List<ProductResponse> productResponses = this.IProductService.getAllProduct();

        return ResponseEntity.status(HttpStatus.OK).body(productResponses);
    }

    @GetMapping("product/{productId}/prices")
    public ResponseEntity<List<PriceResponse>> getAllPrice(@PathVariable("productId") Long productId){
        List<PriceResponse> priceResponses = this.IProductService.getAllPriceByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(priceResponses);
    }

    @DeleteMapping("product/{productId}/price/{appliedDate}")
    public ResponseEntity<ApiResponse> deletePrice(
            @PathVariable("productId") Long productId,
            @PathVariable("appliedDate") String date) throws ParseException {

        this.IProductService.deletePrice(productId,date);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

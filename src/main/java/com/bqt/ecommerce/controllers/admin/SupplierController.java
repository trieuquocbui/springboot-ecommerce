package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.payloads.request.SupplierRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.SupplierResponse;
import com.bqt.ecommerce.services.ISupplierService;
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
public class SupplierController {

    @Autowired
    private ISupplierService supplierService;

    @PostMapping("supplier")
    public ResponseEntity<SupplierResponse> createSupplier(@RequestBody SupplierRequest supplier) {

        SupplierResponse supplierResponse = this.supplierService.createSupplier(supplier);

        return ResponseEntity.status(HttpStatus.CREATED).body(supplierResponse);
    }

    @GetMapping("supplier/{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable(name = "supplierId") String supplierId) {

        SupplierResponse supplierResponse = this.supplierService.findById(supplierId);

        return ResponseEntity.status(HttpStatus.OK).body(supplierResponse);
    }

    @PutMapping("supplier/{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable(name = "supplierId") String supplierId,
            @RequestBody SupplierRequest supplierRequest) {

        SupplierResponse supplierResponse = this.supplierService.updateSupplier(supplierId, supplierRequest);

        return ResponseEntity.status(HttpStatus.OK).body(supplierResponse);
    }

    @DeleteMapping("supplier/{idSupplier}")
    public ResponseEntity<ApiResponse> removeSupplier(@PathVariable(name = "idSupplier") String idSupplier) {
        this.supplierService.removeSupplier(idSupplier);

        ApiResponse apiResponse = new ApiResponse(ApiConstant.STATUS_SUCCESS,ApiConstant.SUCCESS_DELETE,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("supplier")
    public ResponseEntity<PaginationResponse<SupplierResponse>> getSuppliers(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy) {

        PaginationResponse listSupplier = this.supplierService.getSupplierList(page, limit,sortDir,sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(listSupplier);
    }

    @GetMapping("supplier/find")
    public ResponseEntity<PaginationResponse<SupplierResponse>> getSuppliers(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @RequestParam(name = "search") String search) {

        PaginationResponse listSupplier = this.supplierService.getFindSupplierList(page, limit,sortDir,sortBy,search);

        return ResponseEntity.status(HttpStatus.OK).body(listSupplier);
    }

    @GetMapping("supplier/all")
    public ResponseEntity<List<SupplierResponse>> getAllActiveSupplier() {
        List<SupplierResponse> listSupplier = this.supplierService.getAllActiveSupplier(true);
        return ResponseEntity.status(HttpStatus.OK).body(listSupplier);
    }
}

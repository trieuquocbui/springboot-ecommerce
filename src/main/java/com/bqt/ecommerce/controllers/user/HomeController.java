package com.bqt.ecommerce.controllers.user;

import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class HomeController {

    // @Autowired say spring inject an instance of object into this property when it is init
    @Autowired
    private IProductService IProductService;

    @GetMapping("home")
    public ResponseEntity<PaginationResponse> showHome(
            @RequestParam(name = "brand", required = false) Long brandId,
            @RequestParam(name = "ram", required = false) String ram,
            @RequestParam(name = "cpu", required = false) String cpu,
            @RequestParam(name = "displaySize", required = false) String displaySize,
            @RequestParam(name = "graphicCard", required = false) String graphicCard,
            @RequestParam(name = "operatingSystem", required = false) String operatingSystem,
            @RequestParam(name = "weight", required = false) String weight,
            @RequestParam(name = "hardDrive", required = false) String hardDrive,
            @RequestParam(name = "size", required = false) String size,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "label", required = false) String label,
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit) {
        PaginationResponse productList = IProductService.getListProduct(page, limit, Optional.ofNullable(brandId),ram,cpu,displaySize,graphicCard,
                operatingSystem,weight,hardDrive,size,minPrice,maxPrice,label);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }
}

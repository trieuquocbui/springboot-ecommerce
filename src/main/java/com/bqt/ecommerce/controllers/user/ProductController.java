package com.bqt.ecommerce.controllers.user;

import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import com.bqt.ecommerce.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class ProductController {
    @Autowired
    private IProductService IProductService;

    @GetMapping("product/{productId}")
    public ResponseEntity<ProductResponse> showDetailsProduct(@PathVariable("productId") Long productId){
        ProductResponse product = IProductService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("compare")
    public ResponseEntity<ProductResponse[]> showCompareProducts(@RequestParam(name = "pc",required = false) String nameP,@RequestParam(name = "p",required = false) String nameP2){
        ProductResponse[] productList = IProductService.getProductsForCompare(nameP,nameP2);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping("product/search/{name}")
    public ResponseEntity<ProductResponse> showProduct(@PathVariable(name = "name") String name){
        ProductResponse product= IProductService.getProductByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("product/search")
    public ResponseEntity<List<ProductResponse>> showSuggestProduct(@RequestParam(name = "name") String name){
        List<ProductResponse> products= IProductService.getProductsNameContaining(name);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("predict")
    public ResponseEntity<PaginationResponse> predict(
            @RequestParam(name = "description") String description,
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
            System.out.print(description);
        if(label != "" ){

            PaginationResponse paginationResponse = IProductService.getListProductByLabel(label,page,limit);

            return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
        } else {
            try {
//            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/DataPreprocessing/main.py", predictRequest.getDescription());
//            String pythonScriptPath = "src/main/resources/DataPreprocessing/main.py";

                String cmd = "python src/main/resources/python/RecommendationSystem/main.py " + description;
                Process process = Runtime.getRuntime().exec(cmd);

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(",");
                }
                int exitCode = process.waitFor();

                if (exitCode == 0) {

                    String str = result.toString().replace(",","");

                    PaginationResponse paginationResponse = this.IProductService.getListProductByLabel(str,page,limit);

                    return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
                } else {
                    throw new BadRequestException( "error " + exitCode + "");
                }

            } catch (IOException | InterruptedException  e) {
                throw new BadRequestException("Error occurred " + e);
            }
        }
    }
}

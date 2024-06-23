package com.bqt.ecommerce.services.impl;


import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.*;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.ProductRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.PriceResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import com.bqt.ecommerce.repositories.ConfigRepository;
import com.bqt.ecommerce.repositories.PriceRepository;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.specifications.ProductSpecification;
import com.bqt.ecommerce.repositories.BrandRepository;
import com.bqt.ecommerce.repositories.ProductRepository;
import com.bqt.ecommerce.services.IProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private IFileService IFileService;

    @Value("${spring.resources.static-locations}")
    private String path;


    @Override
    public PaginationResponse getListProduct(int page, int limit, Optional<Long> brandId, String ram, String cpu, String displaySize, String graphicCard, String operatingSystem, String weight, String hardDrive, String size,Integer minPrice,Integer maxPrice,String label) {

        Pageable pageable = PageRequest.of(page - 1, limit);

        Brand brand = brandId.isPresent() ? brandRepository.findById(brandId.get()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND)) : null;

        Config config = new Config();
        config.setRam(ram);
        config.setCpu(cpu);
        config.setDisplaySize(displaySize);
        config.setGraphicCard(graphicCard);
        config.setHardDrive(hardDrive);
        config.setOperatingSystem(operatingSystem);
        config.setWeight(weight);
        config.setSize(size);

        ProductSpecification configSpecification = new ProductSpecification(config, brand,minPrice,maxPrice,label);

        Page productPage = productRepository.findAll(configSpecification, pageable);

        List<Product> products = productPage.getContent();

        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                            item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }
            System.out.print(findPrice.get().getNewPrice());
            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(productResponses);
        paginationResponse.setLastPage(productPage.isLast());
        paginationResponse.setPageNumber(productPage.getNumber());
        paginationResponse.setPageSize(productPage.getSize());
        paginationResponse.setTotalElements(productPage.getTotalElements());
        paginationResponse.setTotalPages(productPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public ProductResponse[] getProductsForCompare(String nameP, String nameP2) {
        ProductResponse[] productList = new ProductResponse[2];

        if (nameP != null) {
            ProductResponse product1 = this.getProductByName(nameP);
            productList[0] = product1;
        }

        if (nameP2 != null) {
            ProductResponse product2 = this.getProductByName(nameP2);
            productList[1] = product2;
        }
        return productList;
    }

    @Override
    public List<ProductResponse> getProductsNameContaining(String name) {
        List<Product> products =productRepository.findByNameContaining(name);
        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                            item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }
        return productResponses;
    }

    @Override
    public ProductResponse getProductByName(String name) {
        Product product = this.productRepository.findByName(name).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        double price = 0;
        int rate = 0;

        Optional<DiscountDetails> discountDetails = product.getDiscountDetails().stream().filter(item ->
                item.getDiscount().getStartDay().before(new Date()) &&
                        item.getDiscount().getEndDay().after(new Date())).findFirst();

        Optional<Price> findPrice = product.getPrices().stream().filter(
                        item -> !item.getPricePk().getAppliedDate().after(new Date()))
                .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

        if (!discountDetails.isEmpty()){
            rate = discountDetails.get().getDiscount().getDiscountPercent();
        }

        if (!findPrice.isEmpty()){
            price = findPrice.get().getNewPrice();
        }

        ProductResponse productResponse = this.modelMapper.map(product, ProductResponse.class);
        productResponse.setRate(rate);
        productResponse.setPrice(price);
        return productResponse;
    }

    @Override
    public void removeProduct(Long idProduct) {
        Product findProduct = this.productRepository.findById(idProduct).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        if (findProduct.getOrderDetails().isEmpty() && findProduct.getBillDetails().isEmpty() && findProduct.getDiscountDetails().isEmpty() && findProduct.getBillDetails().isEmpty()) {
            for (Price price: findProduct.getPrices()){
                this.priceRepository.deletePriceByDateAndProductId(price.getPricePk().getAppliedDate(), price.getProduct().getId());
            }
            this.IFileService.deleteImage(path,findProduct.getImg());
            this.productRepository.deleteByProductId(findProduct.getId());
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }
    }

    @Override
    public ProductResponse updateProduct(Long idProduct, String fileName, ProductRequest productRequest) {
        Product findProduct = this.productRepository.findById(idProduct).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

        if (productRequest != null) {
            String file = findProduct.getImg();
            productRequest.setImg(file);
            this.modelMapper.map(productRequest, findProduct);
            findProduct.setId(idProduct);
        } else {
            this.IFileService.deleteImage(path, findProduct.getImg());
            findProduct.setImg(fileName);
        }
        Product product = this.productRepository.save(findProduct);
        return  this.modelMapper.map(product,ProductResponse.class);
    }

    @Override
    public ProductResponse createProduct(String fileName, ProductRequest productRequest, Account account) {
        Product product = this.modelMapper.map(productRequest, Product.class);
        Brand brand = this.brandRepository.findById(productRequest.getBrand().getId()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        Config config =  this.configRepository.findById(productRequest.getConfig().getId()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        product.setImg(fileName);
        brand.getProducts().add(product);
        config.getProducts().add(product);
        Product saveProduct = this.productRepository.save(product);
        this.brandRepository.save(brand);
        this.configRepository.save(config);

        Price price = new Price();
        PricePk pricePk = new PricePk();
        pricePk.setProduct(saveProduct.getId());
        pricePk.setStaff(account.getStaff().getId());
        pricePk.setAppliedDate(new Date());
        price.setNewPrice(productRequest.getPrice());
        price.setStaff(account.getStaff());
        price.setProduct(saveProduct);
        price.setPricePk(pricePk);

        this.priceRepository.save(price);

        return this.modelMapper.map(saveProduct,ProductResponse.class);
    }

    @Override
    public PaginationResponse getProductList(int page, int limit, String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page productPage = this.productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();

        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                    item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));


            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(productResponses);
        paginationResponse.setLastPage(productPage.isLast());
        paginationResponse.setPageNumber(productPage.getNumber());
        paginationResponse.setPageSize(productPage.getSize());
        paginationResponse.setTotalElements(productPage.getTotalElements());
        paginationResponse.setTotalPages(productPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public PaginationResponse getListProductByLabel(String label, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page productPage = this.productRepository.findAllByLabel(label,pageable);

        List<Product> products = productPage.getContent();

        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                            item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(productResponses);
        paginationResponse.setLastPage(productPage.isLast());
        paginationResponse.setPageNumber(productPage.getNumber());
        paginationResponse.setPageSize(productPage.getSize());
        paginationResponse.setTotalElements(productPage.getTotalElements());
        paginationResponse.setTotalPages(productPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public ProductResponse getProductById(Long idProduct) {
        Product product = this.productRepository.findById(idProduct).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        double price = 0;
        int rate = 0;

        Optional<DiscountDetails> discountDetails = product.getDiscountDetails().stream().filter(item ->
                item.getDiscount().getStartDay().before(new Date()) &&
                item.getDiscount().getEndDay().after(new Date())).findFirst();

        Optional<Price> findPrice = product.getPrices().stream().filter(
                        item -> !item.getPricePk().getAppliedDate().after(new Date()))
                .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

        if (!discountDetails.isEmpty()){
            rate = discountDetails.get().getDiscount().getDiscountPercent();
        }

        if (!findPrice.isEmpty()){
            price = findPrice.get().getNewPrice();
        }

        ProductResponse productResponse = this.modelMapper.map(product, ProductResponse.class);
        productResponse.setRate(rate);
        productResponse.setPrice(price);
        return productResponse;
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> products = this.productRepository.findAll();

        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                            item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }

        return productResponses;
    }

    @Override
    public List<PriceResponse> getAllPriceByProductId(Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(AppConstant.NOT_FOUND));

        List<Price> prices = product.getPrices();

        List<PriceResponse> priceResponses = new ArrayList<>();

        for (Price price: prices){
            PriceResponse priceResponse = new PriceResponse();
            priceResponse.setNewPrice(price.getNewPrice());
            priceResponse.setAppliedDate(price.getPricePk().getAppliedDate());
            priceResponses.add(priceResponse);
        }

        return priceResponses;
    }

    @Override
    public void deletePrice(Long productId, String date)  {
        String dateString = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertDate;
        try{
            convertDate = dateFormat.parse(dateString);
        } catch (ParseException e){
            throw new BadRequestException("Định dạng ngày không đúng");
        }
        Date currentDate = new Date();

        if (!currentDate.after(convertDate)){
            Product product = this.productRepository.findById(productId).orElseThrow(
                    () -> new NotFoundException(AppConstant.NOT_FOUND));

            this.priceRepository.deletePriceByDateAndProductId(convertDate, product.getId());

        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }
    }

    @Override
    public PaginationResponse getFindProductList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page productPage = this.productRepository.findByNameContaining(pageable,search);

        List<Product> products = productPage.getContent();

        Type listType = new TypeToken<List<ProductResponse>>() {
        }.getType();

        List<ProductResponse> productResponses = this.modelMapper.map(products, listType);

        for(int i = 0 ; i < products.size(); i++){
            double price = 0;
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    item.getDiscount().getStartDay().before(new Date()) &&
                            item.getDiscount().getEndDay().after(new Date())).findFirst();

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(
                            item -> !item.getPricePk().getAppliedDate().after(new Date()))
                    .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));


            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }

            productResponses.get(i).setRate(rate);
            productResponses.get(i).setPrice(price);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(productResponses);
        paginationResponse.setLastPage(productPage.isLast());
        paginationResponse.setPageNumber(productPage.getNumber());
        paginationResponse.setPageSize(productPage.getSize());
        paginationResponse.setTotalElements(productPage.getTotalElements());
        paginationResponse.setTotalPages(productPage.getTotalPages());

        return paginationResponse;
    }
}

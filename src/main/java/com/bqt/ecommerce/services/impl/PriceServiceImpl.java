package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Price;
import com.bqt.ecommerce.entities.PricePk;
import com.bqt.ecommerce.entities.Product;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.PriceRequest;
import com.bqt.ecommerce.payloads.response.PriceResponse;
import com.bqt.ecommerce.repositories.PriceRepository;
import com.bqt.ecommerce.repositories.ProductRepository;
import com.bqt.ecommerce.services.IPriceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements IPriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PriceResponse createPriceForProduct(PriceRequest priceRequest, long productId, Account account) {
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        Price createdPrice = new Price();
        PricePk pricePk = new PricePk();
        pricePk.setProduct(product.getId());
        pricePk.setStaff(account.getStaff().getId());
        pricePk.setAppliedDate(priceRequest.getAppliedDate());
        createdPrice.setPricePk(pricePk);
        createdPrice.setNewPrice(priceRequest.getNewPrice());
        createdPrice.setProduct(product);
        createdPrice.setStaff(account.getStaff());
        Price newPrice = this.priceRepository.save(createdPrice);
        return this.modelMapper.map(newPrice,PriceResponse.class);
    }
}

package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.entities.*;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.DiscountDetailsRequest;
import com.bqt.ecommerce.payloads.response.DiscountResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import com.bqt.ecommerce.repositories.DiscountDetailsRepository;
import com.bqt.ecommerce.repositories.DiscountRepository;
import com.bqt.ecommerce.repositories.ProductRepository;
import com.bqt.ecommerce.services.IDiscountDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DiscountDetailsServiceImpl implements IDiscountDetailsService {
    @Autowired
    private DiscountDetailsRepository discountDetailsRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public DiscountResponse updateDiscountDetails(DiscountDetailsRequest discountDetailsRequests, String discountId) {
        Discount discount = this.discountRepository.findById(discountId).orElseThrow(() -> new NotFoundException("Không tìm thấy khuyến mãi"));
        Date presentDay = new Date();
        Date startDayDisCount = discount.getStartDay();

        List<DiscountDetails> addDiscountDetails = new ArrayList<>();
        List<DiscountDetails> removeDiscountDetails = new ArrayList<>();

        if (!startDayDisCount.before(presentDay)){

            for (ProductResponse product: discountDetailsRequests.getProducts()){
                DiscountDetailsPk discountDetailsPk = new DiscountDetailsPk();
                discountDetailsPk.setDiscount(discount.getId());
                discountDetailsPk.setProduct(product.getId());
                DiscountDetails discountDetails = this.discountDetailsRepository.findById(discountDetailsPk).orElseGet(() -> new DiscountDetails());
                if (discountDetails.getDiscountDetailsPk() == null){
                    discountDetailsPk.setDiscount(discount.getId());
                    discountDetailsPk.setProduct(product.getId());
                    discountDetails.setDiscountDetailsPk(discountDetailsPk);
                    discountDetails.setDiscount(discount);

                    Product findProduct = this.productRepository.findById(product.getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
                    discountDetails.setProduct(findProduct);
                    addDiscountDetails.add(discountDetails);
                } else {
                    if (!discount.getDiscountDetails().contains(discountDetails)){
                        addDiscountDetails.add(discountDetails);
                    }
                }

            }

            if (discount.getDiscountDetails().size() > 0){
                List<Long> productIdList = new ArrayList<>();
                List<Long> productIdListRequest = new ArrayList<>();

                for (ProductResponse productResponse:discountDetailsRequests.getProducts()){
                    productIdListRequest.add(productResponse.getId());
                }

                for (DiscountDetails discountDetails:discount.getDiscountDetails()){
                    productIdList.add(discountDetails.getProduct().getId());
                }

                for (Long productId: productIdList){
                    if(!productIdListRequest.contains(productId)){
                        DiscountDetailsPk discountDetailsPk = new DiscountDetailsPk();
                        discountDetailsPk.setDiscount(discount.getId());
                        discountDetailsPk.setProduct(productId);
                        DiscountDetails discountDetails = this.discountDetailsRepository.findById(discountDetailsPk).
                                orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm khuyến mãi"));
                        System.out.print(discountDetails.getProduct().getId());
                        removeDiscountDetails.add(discountDetails);
                    }
                }
            }

            for(DiscountDetails discountDetails: addDiscountDetails){
                discount.getDiscountDetails().add(discountDetails);
            }

            for(DiscountDetails discountDetails: removeDiscountDetails){
                this.discountDetailsRepository.deleteDiscountDetailsByProductIdAndDiscountId(discountDetails.getProduct().getId(),discountDetails.getDiscount().getId());
                discount.getDiscountDetails().remove(discountDetails);
            }

            Discount saveDiscount = this.discountRepository.save(discount);
            return this.modelMapper.map(saveDiscount, DiscountResponse.class);
        }else {
            throw new BadRequestException("Không thể chỉnh sữa sản phẩm khi giảm giá đã bắt đầu");
        }
    }
}

package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.entities.Discount;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.DiscountRequest;
import com.bqt.ecommerce.payloads.response.DiscountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.repositories.DiscountRepository;
import com.bqt.ecommerce.services.IDiscountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
public class DiscountServiceImpl implements IDiscountService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public DiscountResponse createDiscount(DiscountRequest discountRequest, Account account) {
        boolean checkDiscount = this.discountRepository.findById(discountRequest.getId()).isEmpty();

        if (checkDiscount){
            Discount discount = this.modelMapper.map(discountRequest,Discount.class);
            discount.setStaff(account.getStaff());
            Discount newDiscount =  this.discountRepository.save(discount);
            return this.modelMapper.map(newDiscount, DiscountResponse.class);
        } else {
            throw new BadRequestException(AppConstant.EXISTED);
        }
    }

    @Override
    public DiscountResponse findDiscount(String discountId) {
        Discount discount = this.discountRepository.findById(discountId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        return this.modelMapper.map(discount,DiscountResponse.class);
    }

    @Override
    public void deleteDiscount(String discountId) {
        Discount discount = this.discountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException(AppConstant.NOT_FOUND));
        if (discount.getDiscountDetails().isEmpty()){
            this.discountRepository.delete(discount);
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }
    }

    @Override
    public PaginationResponse<DiscountResponse> getDiscountList(int page, int limit, String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page discountPage = this.discountRepository.findAll(pageable);

        List<Discount> pageContent = discountPage.getContent();

        Type listType = new TypeToken<List<DiscountResponse>>(){}.getType();

        List<DiscountResponse> discountResponses = modelMapper.map(pageContent,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(discountResponses);
        paginationResponse.setLastPage(discountPage.isLast());
        paginationResponse.setPageNumber(discountPage.getNumber());
        paginationResponse.setPageSize(discountPage.getSize());
        paginationResponse.setTotalElements(discountPage.getTotalElements());
        paginationResponse.setTotalPages(discountPage.getTotalPages());
        return paginationResponse;
    }

    @Override
    public DiscountResponse updateDiscount(String discountId, DiscountRequest discountRequest) {
        Discount discount = this.discountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException(AppConstant.NOT_FOUND));

        Date presentDay = new Date();
        Date startDayDisCount = discount.getStartDay();

        if (!startDayDisCount.before(presentDay)){
            this.modelMapper.map(discountRequest,discount);
            Discount saveDiscount = this.discountRepository.save(discount);
            return this.modelMapper.map(saveDiscount,DiscountResponse.class);

        } else {
            throw new BadRequestException(AppConstant.CANT_UPDATE);
        }


    }

    @Override
    public PaginationResponse<DiscountResponse> findDiscountList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page discountPage = this.discountRepository.findByIdContaining(pageable,search);

        List<Discount> pageContent = discountPage.getContent();

        Type listType = new TypeToken<List<DiscountResponse>>(){}.getType();

        List<DiscountResponse> discountResponses = modelMapper.map(pageContent,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(discountResponses);
        paginationResponse.setLastPage(discountPage.isLast());
        paginationResponse.setPageNumber(discountPage.getNumber());
        paginationResponse.setPageSize(discountPage.getSize());
        paginationResponse.setTotalElements(discountPage.getTotalElements());
        paginationResponse.setTotalPages(discountPage.getTotalPages());
        return paginationResponse;
    }
}

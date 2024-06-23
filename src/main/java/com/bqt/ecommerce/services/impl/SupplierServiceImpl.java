package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Supplier;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.SupplierRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.SupplierResponse;
import com.bqt.ecommerce.repositories.SupplierRepository;
import com.bqt.ecommerce.services.ISupplierService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class SupplierServiceImpl implements ISupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        Supplier mapperSupplier = this.modelMapper.map(supplierRequest,Supplier.class);
        boolean  checkSupplier = this.supplierRepository.findById(mapperSupplier.getId()).isPresent();
        if (checkSupplier){
            throw new BadRequestException(AppConstant.NOT_FOUND);
        } else {
            Supplier supplier = this.supplierRepository.save(mapperSupplier);
            return this.modelMapper.map(supplier,SupplierResponse.class);
        }
    }

    @Override
    public SupplierResponse updateSupplier(String idSupplier, SupplierRequest supplierRequest) {
        Supplier findSupplier = this.supplierRepository.findById(idSupplier).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));
        supplierRequest.setId(idSupplier);
        this.modelMapper.map(supplierRequest,findSupplier);
        Supplier supplier = this.supplierRepository.save(findSupplier);

        return this.modelMapper.map(supplier,SupplierResponse.class);
    }

    @Override
    public void removeSupplier(String idSupplier) {
        Supplier findSupplier = this.supplierRepository.findById(idSupplier).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));

        if (findSupplier.getOrders().isEmpty()){
            this.supplierRepository.delete(findSupplier);
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }

    }

    @Override
    public PaginationResponse getSupplierList(int page, int limit,String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page supplierPage = this.supplierRepository.findAll(pageable);

        Type listType = new TypeToken<List<SupplierResponse>>(){}.getType();

        List<SupplierResponse> supplierResponses = modelMapper.map(supplierPage.getContent(),listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(supplierResponses);
        paginationResponse.setLastPage(supplierPage.isLast());
        paginationResponse.setPageNumber(supplierPage.getNumber());
        paginationResponse.setPageSize(supplierPage.getSize());
        paginationResponse.setTotalElements(supplierPage.getTotalElements());
        paginationResponse.setTotalPages(supplierPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public SupplierResponse findById(String idSupplier) {
        Supplier supplier = this.supplierRepository.findById(idSupplier).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        return this.modelMapper.map(supplier,SupplierResponse.class);
    }

    @Override
    public List<SupplierResponse> getAllSupplier() {
        List<Supplier> suppliers = this.supplierRepository.findAll();
        Type listType = new TypeToken<List<SupplierResponse>>(){}.getType();

        List<SupplierResponse> supplierResponses = modelMapper.map(suppliers,listType);

        return supplierResponses;
    }

    @Override
    public List<SupplierResponse> getAllActiveSupplier(boolean b) {
        List<Supplier> suppliers = this.supplierRepository.findAllByStatus(b);
        Type listType = new TypeToken<List<SupplierResponse>>(){}.getType();

        List<SupplierResponse> supplierResponses = modelMapper.map(suppliers,listType);

        return supplierResponses;
    }

    @Override
    public PaginationResponse getFindSupplierList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page supplierPage = this.supplierRepository.findByNameContaining(pageable,search);

        Type listType = new TypeToken<List<SupplierResponse>>(){}.getType();

        List<SupplierResponse> supplierResponses = modelMapper.map(supplierPage.getContent(),listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(supplierResponses);
        paginationResponse.setLastPage(supplierPage.isLast());
        paginationResponse.setPageNumber(supplierPage.getNumber());
        paginationResponse.setPageSize(supplierPage.getSize());
        paginationResponse.setTotalElements(supplierPage.getTotalElements());
        paginationResponse.setTotalPages(supplierPage.getTotalPages());

        return paginationResponse;
    }
}

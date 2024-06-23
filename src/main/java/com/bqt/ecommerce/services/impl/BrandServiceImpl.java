package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.BrandRequest;
import com.bqt.ecommerce.payloads.response.BrandResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.repositories.BrandRepository;
import com.bqt.ecommerce.services.IBrandService;
import com.bqt.ecommerce.services.IFileService;
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
import java.util.List;

@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private IFileService IFileService;

    @Value("${spring.resources.static-locations}")
    private String path;

    @Override
    public List<BrandResponse> getAllActiveBrand(boolean status) {
        List<Brand> brands = brandRepository.findByStatus(status);
        Type listType = new TypeToken<List<BrandResponse>>(){}.getType();

        List<BrandResponse> brandResponses = modelMapper.map(brands,listType);

        return brandResponses;
    }

    @Override
    public BrandResponse createBrand(String fileName, BrandRequest brandRequest) {
        boolean checkedName = this.brandRepository.findByName(brandRequest.getName()).isPresent();
        if (checkedName){
            throw new BadRequestException(AppConstant.NOT_FOUND);
        } else {
            Brand brand = this.modelMapper.map(brandRequest,Brand.class);
            brand.setImg(fileName);
            Brand saveBrand = this.brandRepository.save(brand);
            return this.modelMapper.map(saveBrand,BrandResponse.class);
        }
    }

    @Override
    public BrandResponse updateBrand(Long idBrand, String fileName, BrandRequest brandRequest) {
        Brand findBrand = brandRepository.findById(idBrand).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

        if (brandRequest != null){
            String file = findBrand.getImg();
            brandRequest.setImg(file);
            this.modelMapper.map(brandRequest,findBrand);
        } else {
            this.IFileService.deleteImage(path,findBrand.getImg());
            findBrand.setImg(fileName);
        }
        Brand updateBrand = this.brandRepository.save(findBrand);
        return this.modelMapper.map(updateBrand,BrandResponse.class);
    }

    @Override
    public void removeBrand(Long idBrand) {
        Brand findBrand = brandRepository.findById(idBrand).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        if (findBrand.getProducts().isEmpty()){
            this.IFileService.deleteImage(path,findBrand.getImg());
            this.brandRepository.delete(findBrand);
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }

    }

    @Override
    public PaginationResponse getBrandList(int page, int limit,String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page brandPage = this.brandRepository.findAll(pageable);

        List<Brand> pageContent = brandPage.getContent();

        Type listType = new TypeToken<List<BrandResponse>>(){}.getType();

        List<BrandResponse> BrandResponse = modelMapper.map(pageContent,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(BrandResponse);
        paginationResponse.setLastPage(brandPage.isLast());
        paginationResponse.setPageNumber(brandPage.getNumber());
        paginationResponse.setPageSize(brandPage.getSize());
        paginationResponse.setTotalElements(brandPage.getTotalElements());
        paginationResponse.setTotalPages(brandPage.getTotalPages());
        return paginationResponse;
    }

    @Override
    public BrandResponse getBrandById(Long idBrand) {
        Brand brand = brandRepository.findById(idBrand).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        return this.modelMapper.map(brand,BrandResponse.class);
    }

    @Override
    public PaginationResponse findBrandList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page brandPage = this.brandRepository.findByNameContaining(pageable,search);

        List<Brand> pageContent = brandPage.getContent();

        Type listType = new TypeToken<List<BrandResponse>>(){}.getType();

        List<BrandResponse> BrandResponse = modelMapper.map(pageContent,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(BrandResponse);
        paginationResponse.setLastPage(brandPage.isLast());
        paginationResponse.setPageNumber(brandPage.getNumber());
        paginationResponse.setPageSize(brandPage.getSize());
        paginationResponse.setTotalElements(brandPage.getTotalElements());
        paginationResponse.setTotalPages(brandPage.getTotalPages());
        return paginationResponse;
    }

}

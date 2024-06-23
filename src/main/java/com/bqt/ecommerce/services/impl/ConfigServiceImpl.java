package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Config;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.ConfigRequest;
import com.bqt.ecommerce.payloads.response.ConfigResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ProductResponse;
import com.bqt.ecommerce.repositories.ConfigRepository;
import com.bqt.ecommerce.services.IConfigService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConfigResponse createConfig(ConfigRequest configRequest) {
        boolean checkedNameConfig = this.configRepository.findByNameConfig(configRequest.getNameConfig()).isPresent();
        if (checkedNameConfig){
            throw new BadRequestException(AppConstant.EXISTED_NAME_CONFIG);
        } else {
            Config mapperConfig = this.modelMapper.map(configRequest,Config.class);
            Config config = this.configRepository.save(mapperConfig);
            return this.modelMapper.map(config,ConfigResponse.class);
        }

    }

    @Override
    public ConfigResponse updateConfig(Long idSupplier, ConfigRequest configRequest) {
        Config findConfig = this.configRepository.findById(idSupplier).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));
        boolean checkedNameConfig = this.configRepository.findByNameConfig(configRequest.getNameConfig()).isPresent();
        boolean checkedNameConfigWithOther = configRequest.getNameConfig().equals(findConfig.getNameConfig());
        if (checkedNameConfig && !checkedNameConfigWithOther){
            throw new BadRequestException(AppConstant.EXISTED_NAME_CONFIG);
        } else {
            this.modelMapper.map(configRequest,findConfig);
            Config config = this.configRepository.save(findConfig);
            return this.modelMapper.map(config,ConfigResponse.class);
        }
    }

    @Override
    public void removeConfig(Long idConfig) {
        Config findConfig = this.configRepository.findById(idConfig).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));

        if (findConfig.getProducts().isEmpty()){
            this.configRepository.delete(findConfig);
        } else {
            throw new BadRequestException(AppConstant.CANT_REMOVE);
        }
    }

    @Override
    public PaginationResponse getConfigList(int page, int limit,String sortDir,String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page configPage = this.configRepository.findAll(pageable);

        List<Config> configs = configPage.getContent();

        Type listType = new TypeToken< List<ProductResponse>>(){}.getType();

        List<ConfigResponse> configResponses = new ArrayList<>();

        for (Config config : configs){
            List<ProductResponse> productResponses = this.modelMapper.map(config.getProducts(),listType);
            ConfigResponse configResponse = new ConfigResponse();
            configResponse.setId(config.getId());
            configResponse.setNameConfig(config.getNameConfig());
            configResponse.setCpu(config.getCpu());
            configResponse.setRam(config.getRam());
            configResponse.setSize(config.getSize());
            configResponse.setDisplaySize(config.getDisplaySize());
            configResponse.setGraphicCard(config.getGraphicCard());
            configResponse.setMadeYear(config.getMadeYear());
            configResponse.setMadeIn(config.getMadeIn());
            configResponse.setHardDrive(config.getHardDrive());
            configResponse.setOperatingSystem(config.getOperatingSystem());
            configResponse.setWeight(config.getWeight());
            configResponse.setProducts(productResponses);
            configResponses.add(configResponse);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(configResponses);
        paginationResponse.setLastPage(configPage.isLast());
        paginationResponse.setPageNumber(configPage.getNumber());
        paginationResponse.setPageSize(configPage.getSize());
        paginationResponse.setTotalElements(configPage.getTotalElements());
        paginationResponse.setTotalPages(configPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public ConfigResponse findById(Long idConfig) {
        Config config = this.configRepository.findById(idConfig).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        Type listType = new TypeToken< List<ProductResponse>>(){}.getType();
        List<ProductResponse> productResponses = this.modelMapper.map(config.getProducts(),listType);
        ConfigResponse configResponse = new ConfigResponse();
        configResponse.setId(config.getId());
        configResponse.setNameConfig(config.getNameConfig());
        configResponse.setCpu(config.getCpu());
        configResponse.setRam(config.getRam());
        configResponse.setSize(config.getSize());
        configResponse.setDisplaySize(config.getDisplaySize());
        configResponse.setGraphicCard(config.getGraphicCard());
        configResponse.setMadeYear(config.getMadeYear());
        configResponse.setMadeIn(config.getMadeIn());
        configResponse.setHardDrive(config.getHardDrive());
        configResponse.setOperatingSystem(config.getOperatingSystem());
        configResponse.setWeight(config.getWeight());
        configResponse.setProducts(productResponses);
        return configResponse;
    }

    @Override
    public List<ConfigResponse> getAllConfig() {
        List<Config> configs = this.configRepository.findAll();

        Type listType = new TypeToken< List<ProductResponse>>(){}.getType();

        List<ConfigResponse> configResponses = new ArrayList<>();

        for (Config config : configs){
            List<ProductResponse> productResponses = this.modelMapper.map(config.getProducts(),listType);
            ConfigResponse configResponse = new ConfigResponse();
            configResponse.setId(config.getId());
            configResponse.setNameConfig(config.getNameConfig());
            configResponse.setCpu(config.getCpu());
            configResponse.setRam(config.getRam());
            configResponse.setSize(config.getSize());
            configResponse.setDisplaySize(config.getDisplaySize());
            configResponse.setGraphicCard(config.getGraphicCard());
            configResponse.setMadeYear(config.getMadeYear());
            configResponse.setMadeIn(config.getMadeIn());
            configResponse.setHardDrive(config.getHardDrive());
            configResponse.setOperatingSystem(config.getOperatingSystem());
            configResponse.setWeight(config.getWeight());
            configResponse.setProducts(productResponses);
            configResponses.add(configResponse);
        }

        return configResponses;
    }

    @Override
    public PaginationResponse findConfigList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page configPage = this.configRepository.findByNameConfigContaining(pageable,search);

        List<Config> configs = configPage.getContent();

        Type listType = new TypeToken< List<ProductResponse>>(){}.getType();

        List<ConfigResponse> configResponses = new ArrayList<>();

        for (Config config : configs){
            List<ProductResponse> productResponses = this.modelMapper.map(config.getProducts(),listType);
            ConfigResponse configResponse = new ConfigResponse();
            configResponse.setId(config.getId());
            configResponse.setNameConfig(config.getNameConfig());
            configResponse.setCpu(config.getCpu());
            configResponse.setRam(config.getRam());
            configResponse.setSize(config.getSize());
            configResponse.setDisplaySize(config.getDisplaySize());
            configResponse.setGraphicCard(config.getGraphicCard());
            configResponse.setMadeYear(config.getMadeYear());
            configResponse.setMadeIn(config.getMadeIn());
            configResponse.setHardDrive(config.getHardDrive());
            configResponse.setOperatingSystem(config.getOperatingSystem());
            configResponse.setWeight(config.getWeight());
            configResponse.setProducts(productResponses);
            configResponses.add(configResponse);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(configResponses);
        paginationResponse.setLastPage(configPage.isLast());
        paginationResponse.setPageNumber(configPage.getNumber());
        paginationResponse.setPageSize(configPage.getSize());
        paginationResponse.setTotalElements(configPage.getTotalElements());
        paginationResponse.setTotalPages(configPage.getTotalPages());

        return paginationResponse;
    }


}

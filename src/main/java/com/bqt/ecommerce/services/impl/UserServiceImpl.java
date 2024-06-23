package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.User;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.UserRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.UserResponse;
import com.bqt.ecommerce.repositories.UserRepository;
import com.bqt.ecommerce.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponse updateUser(String image, UserRequest infoUser, Account account) {
        User user = this.userRepository.findById(account.getUser().getId()).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));
        if (infoUser != null){
            user.setFirstName(infoUser.getFirstName());
            user.setLastName(infoUser.getLastName());
            user.setBirthDay(infoUser.getBirthDay());
            user.setIdentification(infoUser.getIdentification());
            user.setPhoneNumber(user.getPhoneNumber());
        } else {
            user.setImage(image);
        }
        return modelMapper.map(this.userRepository.save(user),UserResponse.class);
    }

    @Override
    public UserResponse getUser(Account account) {
        User user = this.userRepository.findById(account.getUser().getId()).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));
        return modelMapper.map(user,UserResponse.class);
    }

    @Override
    public PaginationResponse getListUser(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1,limit);

        Page userPage = this.userRepository.findAll(pageable);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(userPage.getContent());
        paginationResponse.setLastPage(userPage.isLast());
        paginationResponse.setPageNumber(userPage.getNumber());
        paginationResponse.setPageSize(userPage.getSize());
        paginationResponse.setTotalElements(userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return paginationResponse;
    }
}

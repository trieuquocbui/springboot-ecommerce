package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Staff;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.RoleRequest;
import com.bqt.ecommerce.payloads.request.StaffRequest;
import com.bqt.ecommerce.payloads.response.StaffResponse;
import com.bqt.ecommerce.repositories.AccountRepository;
import com.bqt.ecommerce.repositories.StaffRepository;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.services.IStaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl implements IStaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${spring.resources.static-locations}")
    private String path;

    @Autowired
    private IFileService IFileService;

    @Override
    public StaffResponse getStaff(Account account) {
        Staff staff = this.staffRepository.findById(account.getStaff().getId()).orElseThrow( () -> new NotFoundException(AppConstant.NOT_FOUND));
        StaffResponse staffResponse = new StaffResponse();
        staffResponse.setImage(staff.getImage());
        staffResponse.setBirthDay(staff.getBirthDay());
        staffResponse.setFirstName(staff.getFirstName());
        staffResponse.setLastName(staff.getLastName());
        staffResponse.setIdentification(staff.getIdentification());
        staffResponse.setPhoneNumber(staff.getPhoneNumber());
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName(staff.getAccount().getRole().getName());
        return staffResponse;
    }

    @Override
    public StaffResponse updateStaff(String fileName, StaffRequest infoStaff,Account account) {
        Staff findStaff = this.staffRepository.findById(account.getStaff().getId()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        this.IFileService.deleteImage(path, findStaff.getImage());
        this.modelMapper.map(infoStaff, findStaff);
        findStaff.setImage(fileName);
        findStaff.setId(account.getStaff().getId());
        Staff staff = this.staffRepository.save(findStaff);
        return this.modelMapper.map(staff,StaffResponse.class);
    }
}

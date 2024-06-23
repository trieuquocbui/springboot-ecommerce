package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AuthConstant;
import com.bqt.ecommerce.constants.RoleConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Role;
import com.bqt.ecommerce.entities.Staff;
import com.bqt.ecommerce.entities.User;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.AccountRequest;
import com.bqt.ecommerce.payloads.request.SignUpRequest;
import com.bqt.ecommerce.payloads.request.SignUpStaffRequest;
import com.bqt.ecommerce.payloads.response.AccountResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.repositories.AccountRepository;
import com.bqt.ecommerce.repositories.RoleRepository;
import com.bqt.ecommerce.repositories.UserRepository;
import com.bqt.ecommerce.services.IAccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void createAccount(SignUpRequest signUpRequest) {
        Role role;

        Account account = modelMapper.map(signUpRequest,Account.class);

        if (!RoleConstant.USER.equals(account.getRole().getName()) ){
            throw new BadRequestException(AuthConstant.ROLE_NOT_EXIST);
        }

        Optional<Role> findRole = this.roleRepository.findByName(RoleConstant.USER);

        if (!findRole.isPresent()) {
            role = modelMapper.map(account.getRole(),Role.class);
            roleRepository.save(role);
        } else {
            role = findRole.get();
        }

        account.setRole(role);

        boolean checkUsername = this.accountRepository.existsByUsername(account.getUsername());

        if (checkUsername){
            throw new BadRequestException(AuthConstant.EXISTED_USERNAME);
        }

        account.setPassword(encoder.encode(account.getPassword()));
        account.setEmail(signUpRequest.getEmail());

        User user = new User();

        user.setAccount(account);
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());

        account.setUser(user);

        accountRepository.save(account);
    }

    @Override
    public PaginationResponse<AccountResponse> findAccountOfUser(int page, int limit, String sortDir, String sortBy, String username) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page userPage = this.accountRepository.findByUserNotNullAndUsernameContaining(pageable,username);

        List<Account> accounts = userPage.getContent();
        Type listType = new TypeToken<List<AccountResponse>>(){}.getType();

        List<AccountResponse> accountResponses = modelMapper.map(accounts,listType);

        PaginationResponse<AccountResponse> paginationResponse = new PaginationResponse();
        paginationResponse.setContent(accountResponses);
        paginationResponse.setLastPage(userPage.isLast());
        paginationResponse.setPageNumber(userPage.getNumber());
        paginationResponse.setPageSize(userPage.getSize());
        paginationResponse.setTotalElements(userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public PaginationResponse<AccountResponse> getUserList(int page, int limit,String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page userPage = this.accountRepository.findByUserNotNull(pageable);

        List<Account> accounts = userPage.getContent();
        Type listType = new TypeToken<List<AccountResponse>>(){}.getType();

        List<AccountResponse> accountResponses = modelMapper.map(accounts,listType);

        PaginationResponse<AccountResponse> paginationResponse = new PaginationResponse();
        paginationResponse.setContent(accountResponses);
        paginationResponse.setLastPage(userPage.isLast());
        paginationResponse.setPageNumber(userPage.getNumber());
        paginationResponse.setPageSize(userPage.getSize());
        paginationResponse.setTotalElements(userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return paginationResponse;
    }


    @Override
    public AccountResponse updateStatusAccount(Long idAccount, AccountRequest accountRequest) {

        Account account = this.accountRepository.findById(idAccount).orElseThrow(() -> new NotFoundException(AuthConstant.NOT_FOUND_USERNAME));

        Role role = this.roleRepository.findByName(accountRequest.getRole().getName()).orElseThrow(() -> new NotFoundException(AuthConstant.ROLE_NOT_EXIST));

        account.setStatus(accountRequest.isStatus());

        account.setRole(role);

        account = this.accountRepository.save(account);

        return this.modelMapper.map(account,AccountResponse.class);

    }

    @Override
    public PaginationResponse<AccountResponse> getStaffList(int page, int limit,String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page userPage = this.accountRepository.findByStaffNotNull(pageable);

        List<Account> accounts = userPage.getContent();
        Type listType = new TypeToken<List<AccountResponse>>(){}.getType();

        List<AccountResponse> orderResponses = modelMapper.map(accounts,listType);

        PaginationResponse<AccountResponse> paginationResponse = new PaginationResponse();
        paginationResponse.setContent(orderResponses);
        paginationResponse.setLastPage(userPage.isLast());
        paginationResponse.setPageNumber(userPage.getNumber());
        paginationResponse.setPageSize(userPage.getSize());
        paginationResponse.setTotalElements(userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public void createAccountStaff(SignUpStaffRequest signUpStaffRequest) {
        Role role;

        Account account = new Account();
        account.setUsername(signUpStaffRequest.getUsername());
        account.setEmail(signUpStaffRequest.getEmail());
        account.setPassword(AuthConstant.DEFAULT_PASSWORD);
        account.setRole(this.modelMapper.map(signUpStaffRequest.getRole(),Role.class));

        boolean checkUsername = this.accountRepository.existsByUsername(account.getUsername());

        if (checkUsername){
            throw new BadRequestException(AuthConstant.EXISTED_USERNAME);
        }

        Role findRole = this.roleRepository.findByName(signUpStaffRequest.getRole().getName()).orElseThrow(() -> new BadRequestException(AuthConstant.ROLE_NOT_EXIST));

        account.setRole(findRole);

        account.setPassword(encoder.encode(account.getPassword()));

        Staff staff = new Staff();
        staff.setAccount(account);
        staff.setBirthDay(signUpStaffRequest.getBirthDay());
        staff.setFirstName(signUpStaffRequest.getFirstName());
        staff.setLastName(signUpStaffRequest.getLastName());
        staff.setIdentification(signUpStaffRequest.getIdentification());
        staff.setPhoneNumber(signUpStaffRequest.getPhoneNumber());
        account.setStaff(staff);

        accountRepository.save(account);
    }

    @Override
    public PaginationResponse findAccountOfStaff(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1,limit,sort);

        Page userPage = this.accountRepository.findByStaffNotNullAndUsernameContaining(pageable,search);

        List<Account> accounts = userPage.getContent();
        Type listType = new TypeToken<List<AccountResponse>>(){}.getType();

        List<AccountResponse> accountResponses = modelMapper.map(accounts,listType);

        PaginationResponse<AccountResponse> paginationResponse = new PaginationResponse();
        paginationResponse.setContent(accountResponses);
        paginationResponse.setLastPage(userPage.isLast());
        paginationResponse.setPageNumber(userPage.getNumber());
        paginationResponse.setPageSize(userPage.getSize());
        paginationResponse.setTotalElements(userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public AccountResponse findAccountById(int accountId) {
        Account account = this.accountRepository.findById((long) accountId).orElseThrow(() -> new NotFoundException(AuthConstant.NOT_FOUND_USERNAME));

        return this.modelMapper.map(account,AccountResponse.class);
    }
}

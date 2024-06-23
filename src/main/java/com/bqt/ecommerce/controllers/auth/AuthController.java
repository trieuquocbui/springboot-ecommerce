package com.bqt.ecommerce.controllers.auth;

import com.bqt.ecommerce.constants.AuthConstant;
import com.bqt.ecommerce.constants.ApiConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.JwtAuthRequest;
import com.bqt.ecommerce.payloads.request.RoleRequest;
import com.bqt.ecommerce.payloads.request.SignUpStaffRequest;
import com.bqt.ecommerce.payloads.response.AccountResponse;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.JwtAuthResponse;
import com.bqt.ecommerce.security.jwt.JwtHelper;
import com.bqt.ecommerce.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import com.bqt.ecommerce.payloads.request.SignUpRequest;


@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private IAccountService IAccountService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/denied")
    public ResponseEntity<ApiResponse> showPageDenied(){

        ApiResponse response = new ApiResponse(ApiConstant.STATUS_FAIL,AuthConstant.DENIED,HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody SignUpRequest signUpRequest){
        this.IAccountService.createAccount(signUpRequest);

        ApiResponse response = new ApiResponse(ApiConstant.STATUS_SUCCESS,AuthConstant.REGISTER_SUCCESS,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/admin/register/staff")
    public ResponseEntity<ApiResponse> createAccountStaff(@RequestBody SignUpStaffRequest SignUpStaffRequest){
        this.IAccountService.createAccountStaff(SignUpStaffRequest);

        ApiResponse response = new ApiResponse(ApiConstant.STATUS_SUCCESS,AuthConstant.REGISTER_SUCCESS,HttpStatus.OK);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String jwt = jwtHelper.generateJwtToken(authentication);

        JwtAuthResponse response = new JwtAuthResponse();

        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setUsername(account.getUsername());
        RoleRequest role = new RoleRequest();
        role.setName(account.getRole().getName());
        accountResponse.setRole(role);

        response.setToken(jwt);
        response.setAccount(accountResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

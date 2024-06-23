package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.StaffRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.StaffResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.services.IStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController("Profile-Admin")
@CrossOrigin("*")
@RequestMapping("/admin/")
public class ProfileController {

    @Value("${spring.resources.static-locations}")
    private String path;

    @Autowired
    private IFileService IFileService;

    @Autowired
    private IStaffService IStaffService;

    @GetMapping("profile")
    public ResponseEntity<StaffResponse> getInfoStaff(@CurrentAccount Account account){

        StaffResponse infoStaff = this.IStaffService.getStaff(account);

        return ResponseEntity.status(HttpStatus.OK).body(infoStaff);
    }

    @PutMapping("profile")
    public ResponseEntity<StaffResponse> updateProfile(
            @RequestPart("image") MultipartFile file,
            @RequestPart StaffRequest infoStaff,
            @CurrentAccount Account account) throws IOException {

        String fileName = this.IFileService.uploadImage(path, file);

        StaffResponse staffResponse = this.IStaffService.updateStaff(fileName,infoStaff,account);

        return ResponseEntity.status(HttpStatus.OK).body(staffResponse);
    }

}

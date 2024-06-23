package com.bqt.ecommerce.controllers.user;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.UserRequest;
import com.bqt.ecommerce.payloads.response.UserResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.IFileService;
import com.bqt.ecommerce.services.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class ProfileController {

    @Value("${spring.resources.static-locations}")
    private String path;

    @Autowired
    private IFileService IFileService;

    @Autowired
    private IUserService IUserService;

    @GetMapping("profile")
    public ResponseEntity<UserResponse> showInfoUser(@CurrentAccount Account account){

        UserResponse infoUser = this.IUserService.getUser(account);

        return ResponseEntity.status(HttpStatus.OK).body(infoUser);
    }

    @PutMapping("profile/upload")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestParam("image") MultipartFile file,
            @CurrentAccount Account account) throws IOException {

        String fileName = this.IFileService.uploadImage(path,file);

        UserResponse userResponse = this.IUserService.updateUser(fileName,null,account);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping("profile")
    public ResponseEntity<UserResponse> updateProfile(
            @RequestBody UserRequest infoUser,
            @CurrentAccount Account account) throws IOException {

        UserResponse userResponse = this.IUserService.updateUser(null,infoUser,account);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping(value = "image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {

        InputStream resource = this.IFileService.getResource(path,imageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource,response.getOutputStream());
    }
}

package com.bqt.ecommerce.controllers.user;

import com.bqt.ecommerce.constants.PaginationConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.NotificationRequest;
import com.bqt.ecommerce.payloads.response.ApiResponse;
import com.bqt.ecommerce.payloads.response.NotificationResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.security.CurrentAccount;
import com.bqt.ecommerce.services.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @GetMapping("notification/list")
    public ResponseEntity<PaginationResponse<NotificationResponse>> showOrderList(
            @RequestParam(name = "page", defaultValue = PaginationConstant.DEFAULT_PAGE) int page,
            @RequestParam(name = "limit", defaultValue = PaginationConstant.DEFAULT_LIMIT) int limit,
            @RequestParam(name = "sortDir", defaultValue = PaginationConstant.DEFAULT_DIR) String sortDir,
            @RequestParam(name = "sortBy", defaultValue = PaginationConstant.DEFAULT_BY) String sortBy,
            @CurrentAccount Account account){

        PaginationResponse paginationResponse = this.notificationService.getNotificationListOfUser(page,limit,sortDir,sortBy,account);

        return ResponseEntity.status(HttpStatus.OK).body(paginationResponse);
    }

    @PutMapping("notification/{notificationId}")
    public ResponseEntity<NotificationResponse> finishOrder(
            @PathVariable(name = "notificationId") Long notificationId,
            @RequestBody NotificationRequest notificationRequest,
            @CurrentAccount Account account){

        NotificationResponse notificationResponse = this.notificationService.update(notificationId,notificationRequest,account);

        return ResponseEntity.status(HttpStatus.OK).body(notificationResponse);
    }
}

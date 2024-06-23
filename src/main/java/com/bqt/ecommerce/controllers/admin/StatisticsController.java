package com.bqt.ecommerce.controllers.admin;

import com.bqt.ecommerce.payloads.request.StatisticsRequest;
import com.bqt.ecommerce.payloads.response.BillResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;
import com.bqt.ecommerce.services.IBillService;
import com.bqt.ecommerce.services.IReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/")
@PreAuthorize("hasAuthority('ADMIN')")
public class StatisticsController {

    @Autowired
    private IReceiptService iReceiptService;

    @Autowired
    private IBillService iBillService;;

    @GetMapping("receipt/statistics")
    public ResponseEntity<List<ReceiptResponse>> getReceiptStatistics(
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate
            ) throws ParseException {

        List<ReceiptResponse> receiptResponses = this.iReceiptService.getReceiptListFromDateToDate(fromDate,toDate);


        return ResponseEntity.status(HttpStatus.OK).body(receiptResponses);
    }

    @GetMapping("bill/statistics")
    public ResponseEntity<List<BillResponse>> getBillStatistics(
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate) throws ParseException {

        List<BillResponse> billResponses = this.iBillService.getBillListFromDateToDate(fromDate,toDate);


        return ResponseEntity.status(HttpStatus.OK).body(billResponses);
    }

}

package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.ReceiptRequest;
import com.bqt.ecommerce.payloads.request.StatisticsRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IReceiptService {
    ReceiptResponse createReceipt(ReceiptRequest receiptRequest, Account account);

    void removeReceipt(String idReceipt);

    PaginationResponse getReceiptList(int page, int limit, String sortDir, String sortBy);

    ReceiptResponse getById(String receiptId);

    PaginationResponse getFindReceiptList(int page, int limit, String sortDir, String sortBy, String search);


    List<ReceiptResponse> getReceiptListFromDateToDate(String fromDate,String toDate) throws ParseException;
}

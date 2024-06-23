package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.payloads.request.BillRequest;
import com.bqt.ecommerce.payloads.request.StatisticsRequest;
import com.bqt.ecommerce.payloads.response.BillResponse;
import com.bqt.ecommerce.payloads.response.OrderResponse;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;

import java.text.ParseException;
import java.util.List;

public interface IBillService {
    BillResponse createBill(BillRequest orderRequest, Account account);

    PaginationResponse getBillList(int page, int limit, String dir, String sortBy,Account account);

    BillResponse finishBill(Long idOrder, BillRequest billRequest, Account account);

    void cancelBill(Long idOrder, Account account);

    PaginationResponse getStatusBillList(int page, int limit, int status,String sortDir, String sortBy);

    BillResponse updateStatusBill(Long idOrder, BillRequest orderRequest, Account account);

    List<BillResponse> getBillListFromDateToDate(String fromDate,String toDate) throws ParseException;
}

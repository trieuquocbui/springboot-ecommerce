package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.entities.*;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.ReceiptRequest;
import com.bqt.ecommerce.payloads.request.StatisticsRequest;
import com.bqt.ecommerce.payloads.response.PaginationResponse;
import com.bqt.ecommerce.payloads.response.ReceiptResponse;
import com.bqt.ecommerce.repositories.*;
import com.bqt.ecommerce.services.IReceiptService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ReceiptServiceImpl implements IReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private DetailsReceiptRepository detailsReceiptRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SeriRepository seriRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReceiptDetailsRepository receiptDetailsRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public ReceiptResponse createReceipt(ReceiptRequest receiptRequest, Account account) {
        Receipt receipt = new Receipt();
        receipt.setId(receiptRequest.getId());
        receipt.setDate(receiptRequest.getDate());
        receipt.setStaff(account.getStaff());
        receipt.setOrder(receiptRequest.getOrder());

        Receipt newReceipt = this.receiptRepository.save(receipt);

        for (int i = 0; i < receiptRequest.getListReceiptDetail().size();i++){
            ReceiptDetails receiptDetails = new ReceiptDetails();
            ReceiptDetailsPk receiptDetailsPk = new ReceiptDetailsPk();;
            OrderDetailsPk orderDetailsPk = new OrderDetailsPk();

            orderDetailsPk.setProduct(receiptRequest.getListReceiptDetail().get(i).getProduct().getId());
            orderDetailsPk.setOrder(receiptRequest.getOrder().getId());

            OrderDetails orderDetails = this.orderDetailsRepository.findById(orderDetailsPk).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

            receiptDetailsPk.setReceipt(newReceipt.getId());
            receiptDetailsPk.setOrderDetailsPk(orderDetails.getOrderDetailsPk());

            receiptDetails.setId(receiptDetailsPk);
            receiptDetails.setReceipt(newReceipt);
            receiptDetails.setQuantity(receiptRequest.getListReceiptDetail().get(i).getQuantity());
            receiptDetails.setPrice(receiptRequest.getListReceiptDetail().get(i).getPrice());
            receiptDetails.setOrderDetails(orderDetails);
            ReceiptDetails newReceiptDetails = this.receiptDetailsRepository.save(receiptDetails);
            newReceipt.getListReceiptDetails().add(newReceiptDetails);
        }

        Receipt updateReceipt = this.receiptRepository.save(newReceipt);

        for (ReceiptDetails receiptDetails: updateReceipt.getListReceiptDetails()){
            for (int i = 0; i < receiptDetails.getQuantity(); i++){
                Seri seri = new Seri();
                seri.setReceivingDate(updateReceipt.getDate());
                seri.setProduct(receiptDetails.getOrderDetails().getProduct());
                this.seriRepository.save(seri);
            }
        }

        return this.modelMapper.map(updateReceipt,ReceiptResponse.class);
    }


    @Override
    public void removeReceipt(String idReceipt) {
        Receipt receipt = this.receiptRepository.findById(idReceipt).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

        List<Seri> seris = this.seriRepository.findByReceivingDate(receipt.getDate());

        this.seriRepository.deleteAll(seris);

        for (ReceiptDetails receiptDetails : receipt.getListReceiptDetails()) {
            this.detailsReceiptRepository.delete(receiptDetails);
        }

        this.receiptRepository.delete(receipt);

    }

    @Override
    public PaginationResponse getReceiptList(int page, int limit, String sortDir, String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, limit,sort);

        Page receiptPage = this.receiptRepository.findAll(pageable);

        List<Receipt> receipts = receiptPage.getContent();

        Type listType = new TypeToken<List<ReceiptResponse>>(){}.getType();

        List<ReceiptResponse> list = this.modelMapper.map(receipts,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(list);
        paginationResponse.setLastPage(receiptPage.isLast());
        paginationResponse.setPageNumber(receiptPage.getNumber());
        paginationResponse.setPageSize(receiptPage.getSize());
        paginationResponse.setTotalElements(receiptPage.getTotalElements());
        paginationResponse.setTotalPages(receiptPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public ReceiptResponse getById(String receiptId) {

        Receipt receipt = this.receiptRepository.findById(receiptId).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

        return this.modelMapper.map(receipt,ReceiptResponse.class);
    }

    @Override
    public PaginationResponse getFindReceiptList(int page, int limit, String sortDir, String sortBy, String search) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, limit,sort);

        Page receiptPage = this.receiptRepository.findByIdContaining(pageable,search);

        List<Receipt> receipts = receiptPage.getContent();

        Type listType = new TypeToken<List<ReceiptResponse>>(){}.getType();

        List<ReceiptResponse> list = this.modelMapper.map(receipts,listType);

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(list);
        paginationResponse.setLastPage(receiptPage.isLast());
        paginationResponse.setPageNumber(receiptPage.getNumber());
        paginationResponse.setPageSize(receiptPage.getSize());
        paginationResponse.setTotalElements(receiptPage.getTotalElements());
        paginationResponse.setTotalPages(receiptPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public List<ReceiptResponse> getReceiptListFromDateToDate(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date convertFromDate = formatter.parse(fromDate);
        Date convertToDate = formatter.parse(toDate);

        List<Receipt> receipts =  this.receiptRepository.findByDateBetween(convertFromDate,convertToDate);
        Type listType = new TypeToken<List<ReceiptResponse>>(){}.getType();

        List<ReceiptResponse> list = this.modelMapper.map(receipts,listType);

        return list;
    }

}

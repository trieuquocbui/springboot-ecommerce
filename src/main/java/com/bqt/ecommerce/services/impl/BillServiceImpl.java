package com.bqt.ecommerce.services.impl;

import com.bqt.ecommerce.constants.AppConstant;
import com.bqt.ecommerce.constants.NotificationEmailConstant;
import com.bqt.ecommerce.constants.StatusOrderEnum;
import com.bqt.ecommerce.entities.*;
import com.bqt.ecommerce.exceptions.BadRequestException;
import com.bqt.ecommerce.exceptions.NotFoundException;
import com.bqt.ecommerce.payloads.request.BillDetailsRequest;
import com.bqt.ecommerce.payloads.request.BillRequest;
import com.bqt.ecommerce.payloads.request.StatisticsRequest;
import com.bqt.ecommerce.payloads.response.*;
import com.bqt.ecommerce.repositories.*;
import com.bqt.ecommerce.services.IBillService;
import com.bqt.ecommerce.services.IEmailService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements IBillService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillDetailsRepository billDetailsRepository;

    @Autowired
    private SeriRepository seriRepository;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProductServiceImpl productService;

    @Override
    public BillResponse createBill(BillRequest billRequest, Account account) {
        Bill bill = new Bill();
        bill.setNote(billRequest.getNote());
        bill.setDistrict(billRequest.getDistrict());
        bill.setProvince(billRequest.getProvince());
        bill.setAddressHome(billRequest.getAddressHome());
        bill.setWard(billRequest.getWard());
        bill.setOrderDay(new Date());
        bill.setUser(account.getUser());
        Bill newBill = this.billRepository.save(bill);

        double totalPay = 0;
        for (BillDetailsRequest billDetailsRequest: billRequest.getBillDetails()){
            BillDetails billDetails = new BillDetails();
            billDetails.setBill(newBill);
            billDetails.setQuantity(billDetailsRequest.getQuantity());
            Product product = this.productRepository.findById(billDetailsRequest.getProduct().getId()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
            billDetails.setProduct(product);
            BillDetails newBillDetails = billDetailsRepository.save(billDetails);
            newBill.getBillDetails().add(newBillDetails);
            totalPay += billDetailsRequest.getQuantity() * billDetailsRequest.getProduct().getRate() * billDetailsRequest.getProduct().getPrice();
        }

        newBill.setTotalAmount(totalPay);
        Bill saveBill = this.billRepository.save(newBill);

        return this.modelMapper.map(saveBill,BillResponse.class);
    }

    @Override
    public PaginationResponse getBillList(int page, int limit, String dir,String sortBy, Account account) {
        Sort sort = dir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, limit,sort );

        Page pageBill = this.billRepository.findByUser(account.getUser(), pageable);

        List<Bill> bills = pageBill.getContent();

        Type listType = new TypeToken<List<BillResponse>>() {}.getType();

        List<BillResponse> billResponses = modelMapper.map(bills, listType);

        for (BillResponse billResponse: billResponses){

            for (BillDetailsRequest billDetails : billResponse.getBillDetails()){
                Product product = this.productRepository.findByName(billDetails.getProduct().getName()).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));

                double price = 0;
                int rate = 0;

                Optional<DiscountDetails> discountDetails = product.getDiscountDetails().stream().filter(item ->
                        item.getDiscount().getStartDay().before(new Date()) &&
                                item.getDiscount().getEndDay().after(new Date())).findFirst();

                Optional<Price> findPrice = product.getPrices().stream().filter(
                                item -> !item.getPricePk().getAppliedDate().after(new Date()))
                        .max(Comparator.comparing(item -> item.getPricePk().getAppliedDate(), Date::compareTo));

                if (!discountDetails.isEmpty()){
                    rate = discountDetails.get().getDiscount().getDiscountPercent();
                }

                if (!findPrice.isEmpty()){
                    price = findPrice.get().getNewPrice();
                }

                ProductResponse productResponse = this.modelMapper.map(product, ProductResponse.class);
                productResponse.setRate(rate);
                productResponse.setPrice(price);
                billDetails.setProduct(productResponse);
            }

        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(billResponses);
        paginationResponse.setLastPage(pageBill.isLast());
        paginationResponse.setPageNumber(pageBill.getNumber());
        paginationResponse.setPageSize(pageBill.getSize());
        paginationResponse.setTotalElements(pageBill.getTotalElements());
        paginationResponse.setTotalPages(pageBill.getTotalPages());

        return paginationResponse;
    }

    @Override
    public BillResponse finishBill(Long idOrder, BillRequest billRequest, Account account) {
        Bill bill = this.billRepository.findById(idOrder).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        bill.setStatus(billRequest.getStatus());
        List<Seri> seris = new ArrayList<>();
        for (BillDetails billDetails : bill.getBillDetails()){
            List<Seri> find = this.seriRepository.findBySaleDateIsNullAndProductAndBill(billDetails.getProduct(), bill);
            List<Seri> collect = find.stream().map(seri -> {seri.setSaleDate(new Date()); return seri;}).collect(Collectors.toList());
            seris.addAll(collect);
        }
        bill.setSeris(seris);
        Bill saveBill = this.billRepository.save(bill);

        return this.modelMapper.map(saveBill,BillResponse.class);
    }

    @Override
    @Transactional
    public void cancelBill(Long idOrder, Account account) {
        Bill bill = this.billRepository.findById(idOrder).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        if (bill.getStatus() == 1) {
            throw new NotFoundException(AppConstant.CANT_REMOVE);
        }
        this.billRepository.deleteBill(bill.getId());
    }

    @Override
    public PaginationResponse getStatusBillList(int page, int limit, int status,String sortDir,String sortBy) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, limit,sort);
        Page billPage = this.billRepository.findAllByStatus(pageable, status);

        List<Bill> pageContent = billPage.getContent();
        Type listType = new TypeToken<List<BillResponse>>() {}.getType();

        List<BillResponse> billResponses = modelMapper.map(pageContent, listType);

        for (BillResponse billResponse:billResponses){
            List<BillDetailsRequest> billDetailsRequests = new LinkedList<>();
            for (BillDetailsRequest billDetailsRequest1 : billResponse.getBillDetails()){
                BillDetailsRequest billDetailsRequest = new BillDetailsRequest();;
                billDetailsRequest.setQuantity(billDetailsRequest1.getQuantity());
                ProductResponse productResponse = productService.getProductById(billDetailsRequest1.getProduct().getId());
                billDetailsRequest.setProduct(productResponse);
                billDetailsRequests.add(billDetailsRequest);
            }
            billResponse.setBillDetails(billDetailsRequests);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(billResponses);
        paginationResponse.setLastPage(billPage.isLast());
        paginationResponse.setPageNumber(billPage.getNumber());
        paginationResponse.setPageSize(billPage.getSize());
        paginationResponse.setTotalElements(billPage.getTotalElements());
        paginationResponse.setTotalPages(billPage.getTotalPages());

        return paginationResponse;
    }

    @Override
    public BillResponse updateStatusBill(Long idOrder, BillRequest orderRequest, Account account) {
        Bill bill = this.billRepository.findById(idOrder).orElseThrow(() -> new NotFoundException(AppConstant.NOT_FOUND));
        bill.setStatus(orderRequest.getStatus());
        bill.setStaff(account.getStaff());

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(bill.getUser().getAccount().getEmail());
        emailDetails.setSubject(NotificationEmailConstant.TITLE);

        Notification notification = new Notification();
        notification.setAccount(bill.getUser().getAccount());
        notification.setTitle(AppConstant.NOTIFICATION_TITLE);
        notification.setDate(new Date());

        if (bill.getStatus() == StatusOrderEnum.APPROVE_ORDER.getValue()){
            for (BillDetails billDetails: bill.getBillDetails()){
                long countSeri = this.seriRepository.findAllByProduct(billDetails.getProduct()).size();
                if (countSeri == 0){
                    throw new BadRequestException(AppConstant.PLEASE_IMPORT_GOODS);
                } else if (countSeri < billDetails.getQuantity()){
                    throw new BadRequestException(AppConstant.INSUFFICIENT_QUANTITY);
                } else {
                    List<Seri> seris = this.seriRepository.findRandomSerisByProductAndQuantity(billDetails.getProduct().getId(), billDetails.getQuantity());
                    for(Seri seri: seris){
                        seri.setBill(bill);
                        this.seriRepository.save(seri);
                    }
                }
            }

            notification.setBody(NotificationEmailConstant.BODY_APPROVE);

            emailDetails.setMsgBody(NotificationEmailConstant.BODY_APPROVE);

        } else if (bill.getStatus() == StatusOrderEnum.COMPLETED.getValue()){

            notification.setBody(NotificationEmailConstant.BODY_COMPLETED);

            emailDetails.setMsgBody(NotificationEmailConstant.BODY_COMPLETED);

        } else if (bill.getStatus() == StatusOrderEnum.IN_TRANSIT.getValue()){

            notification.setBody(NotificationEmailConstant.BODY_IN_TRANSIT);

            emailDetails.setMsgBody(NotificationEmailConstant.BODY_IN_TRANSIT);

        } else if (bill.getStatus() == StatusOrderEnum.DELIVERED.getValue()){

            notification.setBody(NotificationEmailConstant.BODY_DELIVERED);

            emailDetails.setMsgBody(NotificationEmailConstant.BODY_DELIVERED);

        } else if (bill.getStatus() == StatusOrderEnum.DISAPPROVE_ORDER.getValue()){

            emailDetails.setMsgBody(NotificationEmailConstant.BODY_DISAPPROVE_ORDER);

            notification.setBody(NotificationEmailConstant.BODY_DISAPPROVE_ORDER);
        }

        emailService.sendSimpleMail(emailDetails);

        this.notificationRepository.save(notification);

        Bill updateBill = this.billRepository.save(bill);

        return this.modelMapper.map(updateBill,BillResponse.class);
    }

    @Override
    public List<BillResponse> getBillListFromDateToDate(String fromDate,String toDate) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date convertFromDate = formatter.parse(fromDate);
        Date convertToDate = formatter.parse(toDate);

        List<Bill> bills = this.billRepository.findByStatusAndOrderDayBetween(StatusOrderEnum.COMPLETED.getValue(),convertFromDate,convertToDate);
        Type listType = new TypeToken<List<BillResponse>>() {}.getType();

        List<BillResponse> billResponses = modelMapper.map(bills, listType);

        for (BillResponse billResponse:billResponses){
            List<BillDetailsRequest> billDetailsRequests = new LinkedList<>();
            for (BillDetailsRequest billDetailsRequest1 : billResponse.getBillDetails()){
                BillDetailsRequest billDetailsRequest = new BillDetailsRequest();;
                billDetailsRequest.setQuantity(billDetailsRequest1.getQuantity());
                ProductResponse productResponse = productService.getProductById(billDetailsRequest1.getProduct().getId());
                billDetailsRequest.setProduct(productResponse);
                billDetailsRequests.add(billDetailsRequest);
            }
            billResponse.setBillDetails(billDetailsRequests);
        }

        return billResponses;
    }

}

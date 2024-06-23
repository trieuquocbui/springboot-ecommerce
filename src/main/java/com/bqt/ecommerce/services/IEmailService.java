package com.bqt.ecommerce.services;

import com.bqt.ecommerce.entities.EmailDetails;

public interface IEmailService {

    String sendSimpleMail(EmailDetails details);


}

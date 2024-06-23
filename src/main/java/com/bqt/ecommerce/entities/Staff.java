package com.bqt.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,nullable = false)
    private String firstName;

    @Column(length = 50,nullable = false)
    private String lastName;

    @Column(length = 10,nullable = false)
    private String phoneNumber;

    @Column(length = 12,nullable = false)
    private String identification;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private Date birthDay;

    @Column(length = 36,nullable = false)
    private String image = "default_image.jpg";

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staff")
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "staff")
    private List<Receipt> receipts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "staff")
    private List<Bill> bills;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "staff")
    private List<Discount> discounts;

    @JsonIgnore
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staff",fetch = FetchType.EAGER)
    private List<Price> prices;
}

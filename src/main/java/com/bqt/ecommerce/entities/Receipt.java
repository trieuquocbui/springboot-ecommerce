package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "receipts")
public class Receipt {
    @Id
    @Column(length = 10)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff;


    @OneToMany(mappedBy = "receipt",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ReceiptDetails> listReceiptDetails = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "id_order")
    private Order order;
}

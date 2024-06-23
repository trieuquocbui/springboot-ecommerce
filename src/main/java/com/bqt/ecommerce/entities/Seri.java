package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Table(name = "seri")
public class Seri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date receivingDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

//    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

}

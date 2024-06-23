package com.bqt.ecommerce.entities;

import com.bqt.ecommerce.constants.StatusOrderEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderDay;

    @Column(nullable = false)
    private double totalAmount;

    private int status = StatusOrderEnum.NOT_APPROVE_YET_ORDER.getValue();

    @Column(length = 500,nullable = false)
    private String addressHome;

    @Column(length = 100,nullable = false)
    private String province;

    @Column(length = 100,nullable = false)
    private String district;

    @Column(length = 100,nullable = false)
    private String ward;

    @Column(length = 5000)
    private String note;

    @Column(length = 10)
    private String taxCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<BillDetails> billDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<Seri> seris = new ArrayList<>();

}

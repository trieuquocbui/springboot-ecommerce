package com.bqt.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Discount {
    @Id
    @Column(name = "id", nullable = false, length = 20)
    private String id;

    @Column(nullable = false)
    private String reason;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDay;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDay;

    @Column(nullable = false)
    private int discountPercent;

    @ManyToOne
    @JoinColumn(name="staff_id",nullable = false)
    private Staff staff;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discount")
    private List<DiscountDetails> discountDetails;

}

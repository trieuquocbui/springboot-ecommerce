package com.bqt.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter
public class Supplier {
    @Id
    @Column(length = 10)
    private String id;

    @Column(length = 50,nullable = false)
    private String name;

    @Column(length = 10,nullable = false)
    private String phoneNumber;

    @Column(length = 1000,nullable = false)
    private String address;

    private boolean status = true;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "supplier")
    private List<Order> orders = new ArrayList<>();

}

package com.bqt.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Table(name = "config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150,unique = true,nullable = false)
    private String nameConfig;

    @Column(length = 100,nullable = false)
    private String ram;

    @Column(length = 100,nullable = false)
    private String cpu;

    @Column(length = 200,nullable = false)
    private String displaySize;

    @Column(length = 100)
    private String graphicCard;

    @Column(length = 100,nullable = false)
    private String operatingSystem;

    @Column(length = 10,nullable = false)
    private String weight;

    @Column(length = 70,nullable = false)
    private String madeIn;

    @Column(length = 70,nullable = false)
    private String hardDrive;

    @Column(length = 100,nullable = false)
    private String size;

    @Column(nullable = false)
    private int madeYear;

    @JsonIgnore
    @OneToMany(mappedBy = "config",cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}

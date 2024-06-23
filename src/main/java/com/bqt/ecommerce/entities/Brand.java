package com.bqt.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30,unique = true,nullable = false)
    private String name;

    @Column(length = 36,nullable = false)
    private String img;

    private boolean status = true;

    @JsonIgnore
    @OneToMany(mappedBy = "brand",fetch = FetchType.LAZY)
    private List<Product> products;
}

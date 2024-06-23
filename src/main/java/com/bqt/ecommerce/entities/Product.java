package com.bqt.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 36,nullable = false)
    private String img;

    @Column(length = 150,nullable = false)
    private String name;

    @Column(length = 5000,nullable = false)
    private String description;

    private boolean status = true;

    @Column(length = 100)
    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<Seri> seris;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_config")
    private Config config;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<OrderDetails> orderDetails;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<DiscountDetails> discountDetails;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product",fetch = FetchType.EAGER)
    private List<BillDetails> billDetails;

    @JsonIgnore
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product",fetch = FetchType.EAGER)
    private List<Price> prices;

    private List<Integer> handleDiscount(List<Product> products){
        List<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < products.size(); i++){
            int rate = 0;

            Optional<DiscountDetails> discountDetails = products.get(i).getDiscountDetails().stream().filter(item ->
                    !item.getDiscount().getStartDay().after(new Date()) &&
                            !item.getDiscount().getEndDay().after(new Date())).findFirst();

            if (!discountDetails.isEmpty()){
                rate = discountDetails.get().getDiscount().getDiscountPercent();
            }

            list.add(rate);
        }

        return list;
    }

    private List<Double> handlePrice(List<Product> products){
        List<Double> list = new ArrayList<>();
        for(int i = 0 ; i < products.size(); i++){
            double price = 0;

            Optional<Price> findPrice = products.get(i).getPrices().stream().filter(item -> !item.getPricePk().getAppliedDate().after(new Date())).findFirst();

            if (!findPrice.isEmpty()){
                price = findPrice.get().getNewPrice();
            }
            list.add(price);
        }

        return list;
    }
}

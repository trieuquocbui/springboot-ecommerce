package com.bqt.ecommerce.specifications;

import com.bqt.ecommerce.entities.Brand;
import com.bqt.ecommerce.entities.Config;
import com.bqt.ecommerce.entities.Price;
import com.bqt.ecommerce.entities.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.Date;

public class ProductSpecification implements Specification<Product> {
    private final Config config;

    private final Brand brand;

    private final Integer maxPrice;

    private final Integer minPrice;

    private final String label;

    public ProductSpecification(Config config,Brand brand,Integer minPrice,Integer maxPrice,String label){
        this.config = config;
        this.brand = brand;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.label = label;
    }


    // root is object represent for table
    // query is a query statement
    // criteriaBuilder is object create CriteriaQuery and Operation, Condition,...
    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        // Join two tables with together
        Join<Product,Config> configJoin = root.join("config");
        Join<Product,Price> priceJoin = root.join("prices");

        // Đầu tiên, lấy ngày hiện tại
        Date currentDate = new Date();

        // Tạo một Subquery để lấy giá gần với ngày hiện tại nhất cho mỗi sản phẩm
        Subquery<Double> subquery = query.subquery(Double.class);
        Root<Price> subqueryRoot = subquery.from(Price.class);

        // Thiết lập các điều kiện trong Subquery
        Predicate subqueryPredicate = criteriaBuilder.conjunction();
        subqueryPredicate = criteriaBuilder.and(
                criteriaBuilder.equal(subqueryRoot.get("product"), root),
                criteriaBuilder.lessThanOrEqualTo(
                        subqueryRoot.get("pricePk").get("appliedDate"),
                        currentDate
                )
        );

        subquery.select(criteriaBuilder.max(subqueryRoot.get("newPrice")))
                .where(subqueryPredicate);

        //Predicate represent a check function:  Operation and (conjunction), or (disjunction),...
        Predicate predicate = criteriaBuilder.conjunction();

        // concat condition together
        if (config.getRam() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(configJoin.get("ram"), config.getRam() + "%"));
        }
        if (config.getCpu() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(configJoin.get("cpu"), config.getCpu() + "%"));
        }
        if (config.getDisplaySize() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("displaySize"), config.getDisplaySize()));
        }
        if (config.getGraphicCard() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("graphicCard"), config.getGraphicCard()));
        }
        if (config.getOperatingSystem() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("operatingSystem"), config.getOperatingSystem()));
        }
        if (config.getWeight() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("weight"), config.getWeight()));
        }
        if (config.getHardDrive() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("hardDrive"), config.getHardDrive()));
        }
        if (config.getSize() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(configJoin.get("size"), config.getSize()));
        }
        if (minPrice != null && maxPrice != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    criteriaBuilder.between(priceJoin.get("newPrice"), minPrice, maxPrice)
            );
        }
        if (brand != null){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("brand"),brand));
        }

        // Kết hợp Predicate về giá và các điều kiện khác bằng AND
        Predicate finalPredicate = criteriaBuilder.and(
                criteriaBuilder.equal(priceJoin.get("newPrice"), subquery)
        );

        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"),true));

        //predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("label"),this.label));

        predicate = criteriaBuilder.and(predicate,finalPredicate);

        return predicate;
    }

}

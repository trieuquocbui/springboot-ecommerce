package com.bqt.ecommerce.entities;


import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.*;


import java.util.Date;
import java.util.List;



@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,nullable = false)
    private String firstName;

    @Column(length = 50,nullable = false)
    private String lastName;

    @Column(length = 10,nullable = false)
    private String phoneNumber;

    @Column(length = 12)
    private String identification;

    @Temporal(TemporalType.DATE)
    private Date birthDay;

    @Column(length = 13)
    private String taxCode;

    @Column(length = 36,nullable = false)
    private String image = "default_image.jpg";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="user",fetch = FetchType.EAGER)
    private List<Bill> bills;
}

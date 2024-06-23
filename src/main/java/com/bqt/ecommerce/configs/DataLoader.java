package com.bqt.ecommerce.configs;

import com.bqt.ecommerce.constants.AuthConstant;
import com.bqt.ecommerce.constants.RoleConstant;
import com.bqt.ecommerce.entities.Account;
import com.bqt.ecommerce.entities.Role;
import com.bqt.ecommerce.entities.Staff;
import com.bqt.ecommerce.repositories.AccountRepository;
import com.bqt.ecommerce.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isUserRole = roleRepository.existsById(1);
        if(!isUserRole){
            Role role = Role.builder()
                    .name(RoleConstant.USER)
                    .build();
            roleRepository.save(role);
        }

        boolean isAdminRole = roleRepository.existsById(2);
        if(!isAdminRole){
            Role role = Role.builder()
                    .name(RoleConstant.ADMIN)
                    .build();
            roleRepository.save(role);
        }

        boolean isSaleRole = roleRepository.existsById(3);
        if(!isSaleRole){
            Role role = Role.builder()
                    .name(RoleConstant.SALE)
                    .build();
            roleRepository.save(role);
        }

        boolean isAdminAccount = accountRepository.existsByUsername("admin");
        if(!isAdminAccount){
            Role role = roleRepository.findById(2).orElse(Role.builder().name(RoleConstant.ADMIN).build());

            Staff staff = Staff.builder()
                    .firstName("123")
                    .identification("1234567890")
                    .lastName("admin")
                    .image("default_image.jpg")
                    .phoneNumber("123456789")
                    .build();

            Account account = Account.builder()
                    .username("admin")
                    .password(passwordEncoder.encode(AuthConstant.DEFAULT_PASSWORD))
                    .status(true)
                    .role(role)
                    .build();

            staff.setAccount(account);

            account.setStaff(staff);

            accountRepository.save(account);
        }
    }
}

package com.investec.clienttransaction.repository;

import com.investec.clienttransaction.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByFirstNameOrIdNumberOrMobileNumber(String firstName, String idNumber, String mobileNumber);

    Client findByIdNumber(String idNumber);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByIdNumber(String idNumber);

    boolean existsByIdAndMobileNumber(Long id, String mobileNumber);
}

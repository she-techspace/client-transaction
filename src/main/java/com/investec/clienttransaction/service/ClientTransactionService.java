package com.investec.clienttransaction.service;

import com.investec.clienttransaction.entity.Client;

import java.util.List;

public interface ClientTransactionService {

    Client saveClientTransaction(Client client);

    List<Client> findByFirstNameOrIdNumberOrMobileNumber(String firstName, String idNumber, String mobileNumber);

    Client updateClientTransaction(Client updateClient);

    void deleteClientTransactionByIdNumber(String idNumber);
}

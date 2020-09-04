package com.investec.clienttransaction.service.impl;

import com.investec.clienttransaction.entity.Client;
import com.investec.clienttransaction.exception.ClientTransactionException;
import com.investec.clienttransaction.model.ValidationMessage;
import com.investec.clienttransaction.repository.ClientRepository;
import com.investec.clienttransaction.service.ClientTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class ClientTransactionServiceImpl implements ClientTransactionService {

    @Autowired
    private ClientRepository clientRepository;

    public Client saveClientTransaction(Client client) {
        validateDuplicateColumns(client);
        return clientRepository.save(client);
    }

    private void validateDuplicateColumns(Client client) {
        if (clientRepository.existsByIdNumber(client.getIdNumber())) {
            throw new ClientTransactionException(ValidationMessage.NO_DUPLICATE_ID_NUMBER);
        } else if (clientRepository.existsByMobileNumber(client.getMobileNumber()))
            throw new ClientTransactionException(ValidationMessage.NO_DUPLICATE_MOBILE_NUMBER);
    }

    public List<Client> findByFirstNameOrIdNumberOrMobileNumber(String firstName, String idNumber, String mobileNumber) {
        List<Client> clients = clientRepository.findByFirstNameOrIdNumberOrMobileNumber(firstName, idNumber, mobileNumber);
        if (CollectionUtils.isEmpty(clients))
            throw new ClientTransactionException(ValidationMessage.CLIENT_NOT_FOUND);
        return clients;
    }

    public Client updateClientTransaction(Client updateClient) {
        Client existingClient = clientRepository.findByIdNumber(updateClient.getIdNumber());
        if (existingClient == null)
            throw new ClientTransactionException(ValidationMessage.UPDATE_CLIENT_NOT_FOUND);
        if (!clientRepository.existsByIdAndMobileNumber(existingClient.getId(), updateClient.getMobileNumber())
                && clientRepository.existsByMobileNumber(updateClient.getMobileNumber())) {
            throw new ClientTransactionException(ValidationMessage.NO_DUPLICATE_MOBILE_NUMBER);
        }
        updateClientTransaction(updateClient, existingClient);
        return clientRepository.save(existingClient);
    }

    private void updateClientTransaction(Client updateClient, Client existingClient) {
        existingClient.setFirstName(updateClient.getFirstName());
        existingClient.setLastName(updateClient.getLastName());
        if (!StringUtils.isEmpty(updateClient.getFullName()))
        existingClient.setFullName(updateClient.getFullName());
        if (!StringUtils.isEmpty(updateClient.getMobileNumber()));
        existingClient.setMobileNumber(updateClient.getMobileNumber());
        if (!StringUtils.isEmpty(updateClient.getPhysicalAddress()));
        existingClient.setPhysicalAddress(updateClient.getPhysicalAddress());
        existingClient.getTransactions().stream().findFirst().get().setAmount(updateClient.getTransactions().
                stream().findFirst().get().getAmount());

    }

    public void deleteClientTransactionByIdNumber(String idNumber) {
        Client client = clientRepository.findByIdNumber(idNumber);
        if (client != null)
            clientRepository.delete(client);
    }

}

package com.investec.clienttransaction.service.impl;

import com.investec.clienttransaction.entity.Client;
import com.investec.clienttransaction.entity.Transaction;
import com.investec.clienttransaction.exception.ClientTransactionException;
import com.investec.clienttransaction.model.ValidationMessage;
import com.investec.clienttransaction.repository.ClientRepository;
import com.investec.clienttransaction.service.ClientTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ClientTransactionServiceImplTest {

    @InjectMocks
    private ClientTransactionService clientTransactionService = new ClientTransactionServiceImpl();

    @MockBean
    private ClientRepository clientRepository;

    @Test
    void saveClientTransaction() {
        Client client = getClient();
        Mockito.when(clientRepository.save(client)).thenReturn(client);
        assertThat(clientTransactionService.saveClientTransaction(client)).isEqualTo(client);
    }

    @Test
    void saveClientTransaction_Duplicate_IdNumber() {
        Client client = getClient();
        Mockito.when(clientRepository.existsByIdNumber("7008090530080")).thenReturn(true);
        ClientTransactionException exception = assertThrows(ClientTransactionException.class, () -> {
            clientTransactionService.saveClientTransaction(client);
        });
        String expectedMessage = "No duplicate ID numbers";
        String actualMessage = exception.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveClientTransaction_Duplicate_MobileNumber() {
        Client client = getClient();

        Mockito.when(clientRepository.existsByMobileNumber("0743654345")).thenReturn(true);
        ClientTransactionException exception = assertThrows(ClientTransactionException.class, () -> {
            clientTransactionService.saveClientTransaction(client);
        });
        String expectedMessage = "No duplicate Mobile numbers";
        String actualMessage = exception.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findClientTransaction_found() {
        Client client = getClient();
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        Mockito.when(clientRepository.findByFirstNameOrIdNumberOrMobileNumber(anyString(), anyString(), anyString())).thenReturn(clients);
        assertThat(clientTransactionService.findByFirstNameOrIdNumberOrMobileNumber(anyString(), anyString(), anyString())).isEqualTo(clients);
    }

    @Test
    void findClientTransaction_notFound() {
        ClientTransactionException exception = assertThrows(ClientTransactionException.class, () -> {
            clientTransactionService.findByFirstNameOrIdNumberOrMobileNumber(anyString(), anyString(), anyString());
        });
        String expectedMessage = "Client transaction not found";
        String actualMessage = exception.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateClientTransaction() {
        Client client = getClient();
        Mockito.when(clientRepository.findByIdNumber("7008090530080")).thenReturn(client);
        Mockito.when(clientRepository.save(client)).thenReturn(client);
        assertThat(clientTransactionService.updateClientTransaction(client)).isEqualTo(client);
    }

    @Test
    void updateClientTransaction_ClientTransaction_Not_Found() {
        Client client = getClient();
        Mockito.when(clientRepository.findByIdNumber("7008090530080")).thenReturn(null);
        ClientTransactionException exception = assertThrows(ClientTransactionException.class, () -> {
            clientTransactionService.updateClientTransaction(client);
        });
        String expectedMessage = ValidationMessage.UPDATE_CLIENT_NOT_FOUND;
        String actualMessage = exception.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateClientTransaction_Duplicate_MobileNumber() {
        Client client = getClient();
        Mockito.when(clientRepository.findByIdNumber("7008090530080")).thenReturn(client);
        Mockito.when(clientRepository.existsByIdAndMobileNumber(1L,"0743654345")).thenReturn(false);
        Mockito.when(clientRepository.existsByMobileNumber("0743654345")).thenReturn(true);
        ClientTransactionException exception = assertThrows(ClientTransactionException.class, () -> {
            clientTransactionService.updateClientTransaction(client);
        });
        String expectedMessage = ValidationMessage.NO_DUPLICATE_MOBILE_NUMBER;
        String actualMessage = exception.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteClientByIdNumber() {
        Client client = getClient();
        Mockito.when(clientRepository.findByIdNumber(anyString())).thenReturn(client);
        clientTransactionService.deleteClientTransactionByIdNumber(anyString());
        verify(clientRepository).delete(client);
    }

    private Client getClient() {
        Client client = new Client();
        Set<Transaction> trans = new HashSet<>();
        Transaction tran = new Transaction();
        trans.add(tran);
        tran.setId(1L);
        tran.setAmount(new BigDecimal(150));
        client.setId(1L);
        client.setFirstName("Bongani");
        client.setLastName("Mthombeni");
        client.setIdNumber("7008090530080");
        client.setMobileNumber("0743654345");
        client.setPhysicalAddress("Rose, Norfolk Street, Midrand, 200");
        client.setTransactions(trans);
        return client;
    }
}
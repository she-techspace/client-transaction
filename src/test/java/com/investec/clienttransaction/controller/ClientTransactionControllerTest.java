package com.investec.clienttransaction.controller;

import com.investec.clienttransaction.entity.Transaction;
import com.investec.clienttransaction.service.ClientTransactionService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.investec.clienttransaction.entity.Client;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class ClientTransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientTransactionService clientTransactionService;

    private static String CREATE_URI = "/api/client/create";
    private static String FIND_URI = "/api/client/find";
    private static String UPDATE_URI = "/api/client/update";
    private static String DELETE_URI = "/api/client/delete?idNumber=";

    @Test
    void createClientTransaction_status_isOk() throws Exception {
        String client = "{\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":7008090530080," +
                "\"mobileNumber\":\"0743654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createClientTransaction_save() throws Exception {
        when(clientTransactionService.saveClientTransaction(any(Client.class))).thenReturn(getClient());
        String client = "{\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":7008090530080," +
                "\"mobileNumber\":\"0743654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Bongani"))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content()
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createClientTransaction_validate_firstName_LastName() throws Exception {
        String client = "{\"firstName\":\"\",\"lastName\":\"\",\"fullname\":null,\"idNumber\" : \"7008090530080\"," +
                "\"mobileNumber\":\"0743654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":null}]}";
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Is.is("First Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Is.is("Last Name is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createClientTransaction_validate_null_idNumber_mobile_number() throws Exception {
        String client = "{\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":null,\"mobileNumber\":null," +
                "\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber", Is.is("ID number is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber", Is.is("ID number is mandatory")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createClientTransaction_validate_invalid_idNumber_mobile_number() throws Exception {
        String client = "{\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":70000530080," +
                "\"mobileNumber\":\"07A3654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber", Is.is("Must be a valid SA ID Number")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber", Is.is("Must be a valid SA Mobile Number")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findClient_returnClientDetails_status_isOk() throws Exception {
        String clientModel = "{\"firstName\": \"Bongani\", \"idNumber\" : \"9008090530080\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(FIND_URI).content(clientModel).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateClient_status_isOk() throws Exception {
        String client = "{\"id\":1,\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":7008090530080," +
                "\"mobileNumber\":\"0743654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateClientTransaction_validate_firstName_LastName() throws Exception {
        String client = "{\"id\":1,\"firstName\":\"\",\"lastName\":\"\",\"fullname\":null,\"idNumber\" : \"7008090530080\"," +
                "\"mobileNumber\":\"0743654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":null}]}";
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Is.is("First Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Is.is("Last Name is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateClientTransaction_validate_null_idNumber_mobile_number() throws Exception {
        String client = "{\"id\":1,\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":null," +
                "\"mobileNumber\":null,\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber", Is.is("ID number is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber", Is.is("ID number is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateClientTransaction_validate_invalid_idNumber_mobile_number() throws Exception {
        String client = "{\"id\":1,\"firstName\":\"Bongani\",\"lastName\":\"Mthombeni\",\"fullname\":null,\"idNumber\":70000530080," +
                "\"mobileNumber\":\"07A3654345\",\"physicalAddress\":\"Rose, Norfolk Street, Midrand, 200\",\"transactions\":[{\"amount\":150}]}";
        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_URI).content(client).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idNumber", Is.is("Must be a valid SA ID Number")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber", Is.is("Must be a valid SA Mobile Number")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteClient_status_isOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_URI).content("7008094530980").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private List<Client> clients() {
        List<Client> clients = new ArrayList<>();
        Client client = getClient();
        clients.add(client);
        return clients;
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
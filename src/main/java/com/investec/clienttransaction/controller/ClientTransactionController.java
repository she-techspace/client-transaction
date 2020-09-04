package com.investec.clienttransaction.controller;

import com.investec.clienttransaction.entity.Client;
import com.investec.clienttransaction.model.ClientModel;
import com.investec.clienttransaction.service.ClientTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientTransactionController {

    @Autowired
    private ClientTransactionService clientTransactionService;

    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientTransactionService.saveClientTransaction(client));
    }

    @PostMapping("/find")
    public ResponseEntity<List<Client>> findClient(@RequestBody ClientModel clientModel) {
        List<Client> client = clientTransactionService.findByFirstNameOrIdNumberOrMobileNumber(clientModel.getFirstName(),
                clientModel.getIdNumber(), clientModel.getMobileNumber());
        return ResponseEntity.ok(client);
    }

    @PutMapping("/update")
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientTransactionService.updateClientTransaction(client));
    }

    @DeleteMapping("/delete")
    public void deleteClient(@RequestParam(value = "idNumber") final String idNumber) {
        clientTransactionService.deleteClientTransactionByIdNumber(idNumber);
    }
}

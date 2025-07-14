package com.example.TransactionService.services;

import com.example.Common.constants.CommonConstants;
import com.example.TransactionService.model.Transaction;
import com.example.TransactionService.model.TxnStatus;
import com.example.TransactionService.repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService
{
    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    TransactionRepository transactionRepository;

    public String initiateTranasction(String sender, String receiver, String amount, String purpose)
    {
        Transaction transaction = new Transaction();
        String txnId = UUID.randomUUID().toString();
        transaction.setTxnId(txnId);
        transaction.setSenderId(sender);
        transaction.setReceiverId(receiver);
        transaction.setAmount(Double.parseDouble(amount));
        transaction.setPurpose(purpose);
        transaction.setTxnStatus(TxnStatus.INITIATED);
        transaction.setTxnStatusMessage("Transaction Initiated");

        transactionRepository.save(transaction);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.SENDER_ID, sender);
        jsonObject.put(CommonConstants.RECEIVER_ID,receiver);
        jsonObject.put(CommonConstants.TXN_ID,txnId);
        jsonObject.put(CommonConstants.TXN_AMOUNT,amount);

        kafkaTemplate.send(CommonConstants.TXN_DETAILS_TOPIC,jsonObject.toString());

        return txnId;
    }
}

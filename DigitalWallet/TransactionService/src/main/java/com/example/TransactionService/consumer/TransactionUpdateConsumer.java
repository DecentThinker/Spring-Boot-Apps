package com.example.TransactionService.consumer;

import com.example.Common.constants.CommonConstants;
import com.example.TransactionService.model.TxnStatus;
import com.example.TransactionService.repository.TransactionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionUpdateConsumer
{
    @Autowired
    TransactionRepository transactionRepository;

    @KafkaListener(topics = "TXN_UPDATE_TOPIC",groupId = "txn-update-group")
    public void consumeUpdatedTransaction(String data)
    {
        System.out.println("Updated Transaction: "+data);
        JSONObject jsonObject = new JSONObject(data);
        String txnId = jsonObject.getString(CommonConstants.TXN_ID);
        String txnStatus = jsonObject.getString(CommonConstants.TXN_STATUS);
        String txnStatusMessage = jsonObject.getString(CommonConstants.TXN_STATUS_MESSAGE);

        transactionRepository.updateTransactionDetails(txnId, TxnStatus.valueOf(txnStatus),txnStatusMessage);

        System.out.println("Transaction details Updated");
    }

}

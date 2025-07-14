package com.example.WalletService.consumer;

import com.example.Common.constants.CommonConstants;
import com.example.Common.model.UserIdentifier;
import com.example.WalletService.service.WalletService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WalletConsumer
{
    @Autowired
    WalletService walletService;

    @KafkaListener(topics = "USER_DETAILS_QUEUE",groupId = "wallet-consumer")
    public void listenNewlyCreatedUser(String data)
    {
        System.out.println("User Data Received"+data);
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.getString(CommonConstants.USER_NAME);
        UserIdentifier userIdentifier = jsonObject.getEnum(UserIdentifier.class, CommonConstants.USER_IDENTIFIER);
        String userIdentifierValue =  jsonObject.getString(CommonConstants.USER_IDENTIFIER_VALUE);
        int userId = jsonObject.getInt(CommonConstants.USER_ID);
        String mobileNo = jsonObject.getString(CommonConstants.USER_MOBILE);

        walletService.createWalletAccount(Integer.toString(userId),mobileNo,name,userIdentifier,userIdentifierValue);
    }
    @KafkaListener(topics = "TXN_DETAILS_TOPIC",groupId = "wallet-txn-details-group")
    public void listenNewTransaction(String data)
    {
        System.out.println("Txn data received"+data);
        JSONObject jsonObject = new JSONObject(data);
        String amount = jsonObject.getString(CommonConstants.TXN_AMOUNT);
        String sender = jsonObject.getString(CommonConstants.SENDER_ID);
        String receiver = jsonObject.getString(CommonConstants.RECEIVER_ID);
        String txnId = jsonObject.getString(CommonConstants.TXN_ID);

        walletService.updateWalletBalance(txnId, sender,receiver,amount);
    }
}

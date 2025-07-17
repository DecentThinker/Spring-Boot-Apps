package com.example.WalletService.service;

import com.example.Common.constants.CommonConstants;
import com.example.Common.model.UserIdentifier;
import com.example.Common.model.WalletStatus;
import com.example.WalletService.model.Wallet;
import com.example.WalletService.repository.WalletRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService
{

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Value("${wallet.initial.amount}")
    private String walletAmount;

    String txnStatus;
    String txnStatusMessage;

    public void createWalletAccount(String userId, String mobileNo, String name,
                                    UserIdentifier userIdentifier, String userIdentifierValue)
    {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setName(name);
        wallet.setMobileNo(mobileNo);
        wallet.setUserIdentifier(userIdentifier);
        wallet.setUserIdentifierValue(userIdentifierValue);
        wallet.setBalance(Double.parseDouble(walletAmount));
        wallet.setWalletStatus(WalletStatus.ACTIVE);

        walletRepository.save(wallet);

        System.out.println("Wallet Account has been created");
    }

    public void updateWalletBalance(String txnId, String senderId, String receiverId, String amount)
    {
        Wallet senderWallet = walletRepository.findByMobileNo(senderId);
        if(senderWallet==null)
        {
            txnStatus = "FAILED";
            txnStatusMessage="Sender wallet does not exist";
        }
        if(senderWallet!=null && senderWallet.getWalletStatus()!=WalletStatus.ACTIVE)
        {
            txnStatus = "FAILED";
            txnStatusMessage="Your Wallet account is blocked";
        }
        Wallet receiverWallet = walletRepository.findByMobileNo(receiverId);
        if(receiverWallet==null)
        {
            txnStatus = "FAILED";
            txnStatusMessage="Receiver wallet does not exist";
        }
        if(receiverWallet!=null && receiverWallet.getWalletStatus()!=WalletStatus.ACTIVE)
        {
            txnStatus = "FAILED";
            txnStatusMessage="Receiver Wallet account is blocked";
        }
        if(senderWallet.getBalance()<Double.parseDouble(amount))
        {
            txnStatus = "FAILED";
            txnStatusMessage="Insufficient balance";
        }
        else if(doTransaction(senderId,receiverId,Double.parseDouble(amount)))
        {
            txnStatus = "SUCCESS";
            txnStatusMessage="Transaction is successful";
        }
        else {
            txnStatus = "PENDING";
            txnStatusMessage="Transaction is Pending";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.TXN_ID, txnId);
        jsonObject.put(CommonConstants.TXN_AMOUNT,amount);
        jsonObject.put(CommonConstants.TXN_STATUS,txnStatus);
        jsonObject.put(CommonConstants.TXN_STATUS_MESSAGE, txnStatusMessage);

        kafkaTemplate.send(CommonConstants.TXN_UPDATE_TOPIC,jsonObject.toString());

        System.out.println("Transaction updated details send to kafka");

    }

    public boolean doTransaction(String sender, String receiver,double amount)
    {
        boolean isDone =false;
        try
        {
            walletRepository.updateWallet(sender,-amount);
            walletRepository.updateWallet(receiver,amount);
            isDone = true;
        }
        catch (Exception e)
        {
            isDone=false;
            System.out.println(e);
        }

        return isDone;
    }
}

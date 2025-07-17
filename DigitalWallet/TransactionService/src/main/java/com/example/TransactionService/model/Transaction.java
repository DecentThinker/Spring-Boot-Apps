package com.example.TransactionService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Entity(name="transaction")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String txnId;
    String senderId;
    String receiverId;
    double amount;
    String purpose;

    @Enumerated(EnumType.STRING)
    TxnStatus txnStatus;

    String txnStatusMessage;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;


}

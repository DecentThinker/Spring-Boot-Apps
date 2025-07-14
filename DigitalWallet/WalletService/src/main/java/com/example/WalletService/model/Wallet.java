package com.example.WalletService.model;

import com.example.Common.model.UserIdentifier;
import com.example.Common.model.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity(name="wallet")
@AllArgsConstructor
@NoArgsConstructor
@Data


public class Wallet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userId;

    String name;

    String mobileNo;

    @Enumerated(EnumType.STRING)
    WalletStatus walletStatus;

    double balance;

    @Enumerated(EnumType.STRING)
    UserIdentifier userIdentifier;

    String userIdentifierValue;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;
}

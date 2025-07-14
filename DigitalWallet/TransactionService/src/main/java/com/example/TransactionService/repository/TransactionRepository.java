package com.example.TransactionService.repository;

import com.example.TransactionService.model.Transaction;

import com.example.TransactionService.model.TxnStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer>
{

    @Modifying
    @Transactional
    @Query("update transaction as t set t.txnStatus=:status,t.txnStatusMessage=:message where t.txnId=:txnId")
        // @Query(value = "update transaction set txn_status=:status and txn_status_message=:message where txn_id=:txnId",nativeQuery = true)
    void updateTransactionDetails(String txnId, TxnStatus status, String message);
}

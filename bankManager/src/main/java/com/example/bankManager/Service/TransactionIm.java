package com.example.bankManager.Service;

import com.example.bankManager.Dto.TransactionDTO;
import com.example.bankManager.Entity.Transaction;
import com.example.bankManager.Repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionIm implements TransactionService{

    @Autowired
    TransactionRepo transactionRepo;
    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAccountNumber(transactionDTO.getAccountNumber());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus("SUCCESSES");

        transactionRepo.save(transaction);

    }
}

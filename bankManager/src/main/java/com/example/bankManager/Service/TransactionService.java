package com.example.bankManager.Service;

import com.example.bankManager.Dto.TransactionDTO;
import com.example.bankManager.Entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDTO);
}

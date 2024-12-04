package com.example.bankManager.Service;

import com.example.bankManager.Dto.*;
import com.example.bankManager.Entity.User;
import com.example.bankManager.Repo.UserRepo;
import com.example.bankManager.Util.AccountUtil;
import com.example.bankManager.Util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserService implements UserServiceIn {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionService transactionService;



    @Override
    public ResponseDTO createAccount(UserDTO userDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        User exitingUser = userRepo.findByEmail(userDTO.getEmail());
        if (exitingUser != null){
            responseDTO.setCode("ERROR");
            responseDTO.setMessage("Account with this email already exists");
            return responseDTO;
        }
        String accountNumber = AccountUtil.generateAccountNumber();

        User user = new User();
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAccountNumber(accountNumber);
        user.setAccountBalance(BigDecimal.ZERO);

        userRepo.save(user);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setRecipient(userDTO.getEmail());
        emailDTO.setSubject("ACCOUNT CREATION");
        emailDTO.setMessageBody("Congratulation Your Account has been Successfully\n Account Details\n" +
                "Account Name : "+userDTO.getName() +" " +"\nAccount Number :" +userDTO.getAccountNumber());
        emailService.sendEmailAlert(emailDTO);


        responseDTO.setMessage("Account created success!");
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNumber(user.getAccountNumber());
        accountInfo.setAccountBalance(user.getAccountBalance());
        responseDTO.setAccountInfo(accountInfo);

        return responseDTO;
    }

    @Override
    public ResponseDTO balanceEnquiry(EnquiryDTO enquiryDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryDTO.getAccountNumber());
        if (! isAccountExist){
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("User with the provided Account Number does not exist");
            return responseDTO;
        }
        User foundUser = userRepo.findByAccountNumber(enquiryDTO.getAccountNumber());

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNumber(foundUser.getAccountNumber());
        accountInfo.setAccountBalance(foundUser.getAccountBalance());
        accountInfo.setAccountName(foundUser.getName());

        responseDTO.setCode(AccountUtil.ACCOUNT_FOUND_CODE);
        responseDTO.setMessage(AccountUtil.ACCOUNT_FOUND_SUCCESS);
        responseDTO.setAccountInfo(accountInfo);

        return responseDTO;
    }

    @Override
    public String nameEnquiry(EnquiryDTO enquiryDTO) {
        boolean isAccountExist = userRepo.existsByAccountNumber(enquiryDTO.getAccountNumber());
        if (!isAccountExist){
            return AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepo.findByAccountNumber(enquiryDTO.getAccountNumber());
        return foundUser.getName();
    }

    @Override
    public ResponseDTO creditAccount(CreditDebitDTO creditDebitDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        //check account exist
        boolean isAccountExist = userRepo.existsByAccountNumber(creditDebitDTO.getAccountNumber());
        if (!isAccountExist){
            responseDTO.setCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            responseDTO.setMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            return responseDTO;
        }
        User userToCredit = userRepo.findByAccountNumber(creditDebitDTO.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitDTO.getAmount()));
        userRepo.save(userToCredit);

        //save transaction
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber(userToCredit.getAccountNumber());
        transactionDTO.setTransactionType("CREDIT");
        transactionDTO.setAmount(creditDebitDTO.getAmount());

        transactionService.saveTransaction(transactionDTO);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountNumber(userToCredit.getAccountNumber());
        accountInfo.setAccountBalance(userToCredit.getAccountBalance());
        accountInfo.setAccountName(userToCredit.getName());

        responseDTO.setCode(AccountUtil.ACCOUNT_CREATION_SUCCESS);
        responseDTO.setMessage(AccountUtil.ACCOUNT_CREDITED_SUCCESS_MESSAGE);
        responseDTO.setAccountInfo(accountInfo);

        return responseDTO;
    }

    @Override
    public ResponseDTO debitAccount(CreditDebitDTO creditDebitDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        boolean isAccountExist = userRepo.existsByAccountNumber(creditDebitDTO.getAccountNumber());
        if (!isAccountExist){
            responseDTO.setCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            responseDTO.setMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            return responseDTO;
        }
        User userToDebit = userRepo.findByAccountNumber(creditDebitDTO.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = creditDebitDTO.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()){
            responseDTO.setCode(AccountUtil.INSUFFICIENT_BALANCE_CODE);
            responseDTO.setMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE);
            responseDTO.setAccountInfo(null);
        }else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitDTO.getAmount()));
            userRepo.save(userToDebit);
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountNumber(userToDebit.getAccountNumber());
            accountInfo.setAccountBalance(userToDebit.getAccountBalance());
            accountInfo.setAccountName(userToDebit.getName());

            responseDTO.setCode(AccountUtil.ACCOUNT_DEBITED_SUCCESS);
            responseDTO.setMessage(AccountUtil.ACCOUNT_DEBITED_MESSAGE);
            responseDTO.setAccountInfo(accountInfo);

        }
        //save transaction
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber(userToDebit.getAccountNumber());
        transactionDTO.setTransactionType("DEBIT");
        transactionDTO.setAmount(creditDebitDTO.getAmount());

        transactionService.saveTransaction(transactionDTO);

        return responseDTO;
    }

    @Override
    public ResponseDTO transfer(TransferRequest transferRequest) {
        ResponseDTO responseDTO = new ResponseDTO();
        boolean isDestinationAccountExit = userRepo.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if(!isDestinationAccountExit){
            responseDTO.setCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE);
            responseDTO.setMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE);
            return responseDTO;
        }
        User sourceAccountUser = userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if (transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            responseDTO.setCode(AccountUtil.INSUFFICIENT_BALANCE_CODE);
            responseDTO.setMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE);
            responseDTO.setAccountInfo(null);
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepo.save(sourceAccountUser);
        EmailDTO debitAlert = new EmailDTO();
        debitAlert.setSubject("DEBIT ALERT");
        debitAlert.setRecipient(sourceAccountUser.getEmail());
        debitAlert.setMessageBody("The sum of " + transferRequest.getAmount() + "has been deducted from your account! Your current balance is " + sourceAccountUser.getAccountBalance());

        emailService.sendEmailAlert(debitAlert);

        User destinationAccountUser = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));
        String recipientAccountUser = destinationAccountUser.getName();
        userRepo.save(destinationAccountUser);
        EmailDTO creditAlert = new EmailDTO();
        creditAlert.setSubject("CREDIT ALERT");
        creditAlert.setRecipient(sourceAccountUser.getEmail());
        creditAlert.setMessageBody("The sum of " + transferRequest.getAmount() + "has been sent from "+sourceAccountUser.getName() +"Your Current balance is " +sourceAccountUser.getAccountBalance());

        emailService.sendEmailAlert(creditAlert);

        //save transaction
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber(destinationAccountUser.getAccountNumber());
        transactionDTO.setTransactionType("CREDIT");
        transactionDTO.setAmount(transferRequest.getAmount());

        transactionService.saveTransaction(transactionDTO);


        responseDTO.setCode(AccountUtil.TRANSFER_SUCCESSFUL_CODE);
        responseDTO.setMessage(AccountUtil.TRANSFER_SUCCESSFUL_MESSAGE);
        responseDTO.setAccountInfo(null);
        return responseDTO;

    }

}

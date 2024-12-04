package com.example.bankManager.Service;

import com.example.bankManager.Dto.*;

public interface UserServiceIn {

    ResponseDTO createAccount (UserDTO userDTO);
    ResponseDTO balanceEnquiry (EnquiryDTO enquiryDTO);
    String nameEnquiry (EnquiryDTO enquiryDTO);

    ResponseDTO creditAccount (CreditDebitDTO creditDebitDTO);

    ResponseDTO debitAccount (CreditDebitDTO creditDebitDTO);

    ResponseDTO transfer(TransferRequest transferRequest);

}

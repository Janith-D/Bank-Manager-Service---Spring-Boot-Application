package com.example.bankManager.Controller;

import com.example.bankManager.Dto.*;
import com.example.bankManager.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/userControl")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("createAccount")
    public ResponseDTO createAccount(@RequestBody UserDTO userDTO) {
        return userService.createAccount(userDTO);
    }

    @GetMapping("/balanceEnquiry")
    public ResponseDTO balanceEnquiry(@RequestBody EnquiryDTO enquiryDTO){
        return userService.balanceEnquiry(enquiryDTO);
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryDTO enquiryDTO){
        return userService.nameEnquiry(enquiryDTO);
    }

    @PostMapping("/creditAccount")
    public ResponseDTO creditAccount(@RequestBody CreditDebitDTO creditDebitDTO){
        return  userService.creditAccount(creditDebitDTO);
    }

    @PostMapping("/debitAccount")
    public ResponseDTO debitAccount (@RequestBody CreditDebitDTO creditDebitDTO){
        return userService.debitAccount(creditDebitDTO);
    }
    @PostMapping("/transferAccount")
    public ResponseDTO transfer (@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }


}

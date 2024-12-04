package com.example.bankManager.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String address;
    private String PhoneNumber;
    private String email;
    private String accountNumber;
    private BigDecimal accountBalance;
}

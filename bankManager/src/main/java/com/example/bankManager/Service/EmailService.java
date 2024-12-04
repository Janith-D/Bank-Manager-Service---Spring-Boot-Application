package com.example.bankManager.Service;

import com.example.bankManager.Dto.EmailDTO;

public interface EmailService {
    void sendEmailAlert(EmailDTO emailDTO);
}

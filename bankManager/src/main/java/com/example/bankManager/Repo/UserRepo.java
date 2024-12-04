package com.example.bankManager.Repo;

import com.example.bankManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    boolean existsByAccountNumber (String accountNumber);
    User findByAccountNumber(String accountNumber);
}

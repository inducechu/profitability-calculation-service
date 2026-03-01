package com.induce.profitability_calculation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.induce.profitability_calculation_service.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

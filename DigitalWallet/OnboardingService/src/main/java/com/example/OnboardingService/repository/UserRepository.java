package com.example.OnboardingService.repository;

import com.example.OnboardingService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    User findByMobileNo(String mobileNo);
}

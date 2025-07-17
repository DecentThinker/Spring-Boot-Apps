package com.example.OnboardingService.model;

import com.example.Common.model.UserIdentifier;
import com.example.Common.model.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.Common.model.UserType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobileNo;

    private String password;

    private String dob;

    @Enumerated(value=EnumType.STRING)
    private UserType userType;

    @Enumerated(value=EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(value=EnumType.STRING)
    private UserIdentifier userIdentifier;

    private String userIdentifierValue;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;
}

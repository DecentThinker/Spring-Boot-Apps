package com.example.OnboardingService.request;

import com.example.Common.model.UserIdentifier;
import com.example.Common.model.UserStatus;
import com.example.Common.model.UserType;
import com.example.OnboardingService.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest
{
    private String name;
    private String email;
    private String mobileNo;
    private String password;
    private String dob;
    private UserIdentifier userIdentifier;
    private String userIdentifierValue;
    private String role;
    private UserType userType;


    public User toUser() {
        User user = User.builder().name(this.name).email(this.email)
                .password(this.password).mobileNo(this.mobileNo).userIdentifier(this.userIdentifier).dob(this.dob)
                .userIdentifierValue(this.userIdentifierValue).userStatus(UserStatus.ACTIVE).build();
        return user;
    }
}

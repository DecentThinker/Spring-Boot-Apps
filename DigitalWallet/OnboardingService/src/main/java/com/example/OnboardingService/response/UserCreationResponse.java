package com.example.OnboardingService.response;


import com.example.Common.model.UserIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationResponse
{
    String message;
    String code;
    String name;
    String email;
    UserIdentifier userIdentifier;
    String userIdentifierValue;
}

package com.example.OnboardingService.service;

import com.example.Common.constants.CommonConstants;
import com.example.Common.model.UserType;
import com.example.OnboardingService.model.User;
import com.example.OnboardingService.repository.UserRepository;
import com.example.OnboardingService.request.UserCreationRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
            //topic type and data type
    KafkaTemplate<String,String> kafkaTemplate;

    public User onboardNewUser(UserCreationRequest userCreationRequest)
    {
        User user = userCreationRequest.toUser();
        String password = passwordEncoder.encode(userCreationRequest.getPassword());
        user.setPassword(password);
        user.setUserType(UserType.NORMAL);

        User savedUser = null;
        try{
            savedUser = userRepository.save(user);
            logger.info("User saved to database");
        }
        catch(Exception ex)
        {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_NAME,savedUser.getName());
        jsonObject.put(CommonConstants.USER_EMAIL,savedUser.getEmail());
        jsonObject.put(CommonConstants.USER_ID,savedUser.getId());
        jsonObject.put(CommonConstants.USER_IDENTIFIER,savedUser.getUserIdentifier());
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE,savedUser.getUserIdentifierValue());
        jsonObject.put(CommonConstants.USER_MOBILE, savedUser.getMobileNo());

        kafkaTemplate.send(CommonConstants.USER_DETAILS_QUEUE_TOPIC, jsonObject.toString());

        return savedUser;
    }

    public String getUserData(String mobileNo)
    {
        User user = userRepository.findByMobileNo(mobileNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_MOBILE,user.getMobileNo());
        jsonObject.put(CommonConstants.USER_PASSWORD, user.getPassword());

        return jsonObject.toString();
    }
}

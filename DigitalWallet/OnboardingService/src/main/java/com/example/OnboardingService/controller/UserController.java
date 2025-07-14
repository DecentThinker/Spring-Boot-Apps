package com.example.OnboardingService.controller;

import com.example.OnboardingService.model.User;
import com.example.OnboardingService.request.UserCreationRequest;
import com.example.OnboardingService.response.UserCreationResponse;
import com.example.OnboardingService.service.UserService;
import jodd.util.StringUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onboarding-service")
public class UserController
{
    @Autowired
    UserService userService;

    @PostMapping("/onboard/user")
    public ResponseEntity<UserCreationResponse> onboardUser(@RequestBody UserCreationRequest userCreationRequest)
    {

        UserCreationResponse userCreationResponse = new UserCreationResponse();

        if(userCreationRequest==null)
        {
            userCreationResponse.setMessage("Request cannot be null");
            userCreationResponse.setCode("01");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        }
        if(StringUtil.isBlank(userCreationRequest.getEmail()) || StringUtil.isBlank(userCreationRequest.getName()))
        {
            userCreationResponse.setMessage("Email or Name cannot be Blank");
            userCreationResponse.setCode("02");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        }
        else if(StringUtil.isBlank(userCreationRequest.getUserIdentifierValue()) || StringUtil.isBlank(userCreationRequest.getUserIdentifierValue()))
        {
            userCreationResponse.setMessage("Please Provide the User Identification");
            userCreationResponse.setCode("03");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        }
        else if(StringUtil.isBlank(userCreationRequest.getMobileNo()))
        {
            userCreationResponse.setMessage("Mobile No can't be blank");
            userCreationResponse.setCode("04");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        }
        else if(StringUtil.isBlank(userCreationRequest.getMobileNo()) && userCreationRequest.getMobileNo().length()!=10)
        {
            userCreationResponse.setMessage("Please provide correct mobile no");
            userCreationResponse.setCode("05");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.BAD_REQUEST);
        }
        User user = userService.onboardNewUser(userCreationRequest);

        if(user==null)
        {
            userCreationResponse.setMessage("User Not Boarded");
            return new ResponseEntity<>(userCreationResponse, HttpStatus.OK);
        }

        userCreationResponse.setMessage("User has been onboarded successfully");
        userCreationResponse.setCode("00");

        userCreationResponse.setUserIdentifier(user.getUserIdentifier());
        userCreationResponse.setUserIdentifierValue(user.getUserIdentifierValue());
        userCreationResponse.setName(user.getName());
        userCreationResponse.setEmail(user.getEmail());

        return new ResponseEntity<>(userCreationResponse,HttpStatus.CREATED);
    }

    //Internal API should not be public
    @GetMapping("/validate/user/{mobileNo}")
    public String validateUser(@PathVariable("mobileNo") String mobile)
    {
        return userService.getUserData(mobile);
    }
}

package com.codepict.Controllers;

import com.codepict.dtos.SignupDTO;
import com.codepict.dtos.UserDTO;
import com.codepict.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody SignupDTO signupDTO)
    {
        if (userService.hasUserWithEmail(signupDTO.getEmail()))
        {
              return new ResponseEntity<>("User already exist with this "+signupDTO.getEmail(), HttpStatus.NOT_ACCEPTABLE);
        }
           UserDTO createdUser=  userService.createUser(signupDTO);
           if (createdUser==null)

               return new ResponseEntity<>("User not created, try again later.", HttpStatus.BAD_REQUEST);

           return new ResponseEntity<>(createdUser,HttpStatus.CREATED);

    }
}

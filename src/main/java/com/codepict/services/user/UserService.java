package com.codepict.services.user;

import com.codepict.dtos.SignupDTO;
import com.codepict.dtos.UserDTO;

public interface UserService
{

    UserDTO createUser(SignupDTO signupDTO);

    boolean hasUserWithEmail(String email);
}

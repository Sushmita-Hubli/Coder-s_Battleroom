package com.codepict.services.user;

import com.codepict.Repositories.UserRepository;
import com.codepict.dtos.SignupDTO;
import com.codepict.dtos.UserDTO;
import com.codepict.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User user=new User();
        user.setEmail(signupDTO.getEmail());
        user.setName(signupDTO.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        User createdUser= userRepository.save(user);
        UserDTO createdUserDTO= new UserDTO();
        createdUserDTO.setId(createdUser.getId());
        return  createdUserDTO;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}

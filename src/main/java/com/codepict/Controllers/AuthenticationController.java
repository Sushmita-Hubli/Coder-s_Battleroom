package com.codepict.Controllers;

import com.codepict.Repositories.UserRepository;
import com.codepict.dtos.AuthenticationRequest;
import com.codepict.dtos.AuthenticationResponse;
import com.codepict.entity.User;
import com.codepict.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX="Bearer";
    public static final String HEADER_STRING ="Authorization";


    @PostMapping
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
      try{
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

      }catch (BadCredentialsException e)
      {
          throw new BadCredentialsException("Incorrect email or password");

      }
      catch (DisabledException e)
      {
          response.sendError(HttpServletResponse.SC_NOT_FOUND,"User is not created");

      }

      final UserDetails userDetails= userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

      Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());

      final String jwt=jwtUtil.generateToken(userDetails.getUsername());

      if (optionalUser.isPresent())
      {
          response.getWriter().write(new JSONObject().put("userId",optionalUser.get().getId()).toString());
      }
      response.addHeader("Access-Control-Expose-Headers","Authorization");
      response.setHeader("Access-Control-Allow-Headers","Authorization, X-PINGoTHER, X-Requested-With, Content-Type, Accept, X-Custom-header");
      response.setHeader(HEADER_STRING,TOKEN_PREFIX + jwt);


    }
}

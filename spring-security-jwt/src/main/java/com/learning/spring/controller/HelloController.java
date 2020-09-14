package com.learning.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.spring.model.AuthenticationRequest;
import com.learning.spring.model.AuthenticationResponse;
import com.learning.spring.service.MyUserDetailsService;
import com.learning.spring.util.JwtUtil;

@RestController
public class HelloController 
{
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@RequestMapping("/hello")
	public String hello() 
	{
		return "Hello World!";
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/authenticate")
	public ResponseEntity<?> authenticateWithToken(@RequestBody AuthenticationRequest authRequest) throws Exception 
	{
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		}
		catch(BadCredentialsException e)
		{
			throw new Exception("Invalid username or password", e);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}



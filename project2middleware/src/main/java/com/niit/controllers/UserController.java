package com.niit.controllers;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@RestController
public class UserController {
	
	public UserController(){
		System.out.println("UserController bean is created");
	}
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ResponseEntity<?> registration(@RequestBody User user)
	{
		try{
			if(!userDao.isEmailUnique(user.getEmail())){
				ErrorClazz errorClazz=new ErrorClazz(2,"Email id already exists.. so enter different email id");
				return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.CONFLICT);
			}
			userDao.registration(user);
	        return new ResponseEntity<Void>(HttpStatus.OK);	
		}
		catch(Exception e){
			ErrorClazz errorClazz=new ErrorClazz(1,"Unable to register user details"+e.getMessage());
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User user,HttpSession session)
	{
		User validUser=userDao.login(user);
		if(validUser==null){
			ErrorClazz errorClazz=new ErrorClazz(5,"Email / password is incorrect..please enter valid credentials..");
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.NOT_FOUND);
		}
		else
		{
			System.out.println("Logged In User email " + validUser.getEmail());
			System.out.println("Session ID " + session.getId());
			session.setAttribute("email", user.getEmail());
			validUser.setOnline(true);
			userDao.updateUser(validUser);
			return new ResponseEntity<User>(validUser,HttpStatus.OK);
		}
	}
	
	
	@RequestMapping(value="/logout",method=RequestMethod.PUT)
	public ResponseEntity<?> logout(HttpSession session)
	{
		String email=(String)session.getAttribute("email");
		if(email!=null)
		{
			System.out.println("logout User email " + email);
			System.out.println("Session ID " + session.getId());
			User user=userDao.getUser(email);
			user.setOnline(false);
			userDao.updateUser(user);
			
			session.removeAttribute("email");
			session.invalidate();
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else
		{
			ErrorClazz errorClazz=new ErrorClazz(6,"please login..");
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public ResponseEntity<?> updateUserProfile(@RequestBody User user,HttpSession session)
	{
		String email=(String)session.getAttribute("email");
		if(email==null)
		{
			ErrorClazz errorClazz=new ErrorClazz(7,"Unauthorized access..please login");
			return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
		}
		userDao.updateUser(user);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
}
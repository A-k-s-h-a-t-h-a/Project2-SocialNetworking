package com.niit.dao;

import com.niit.model.User;

public interface UserDao {
	
	public void registration(User user);
	public boolean isEmailUnique(String email);
	public User  login(User user);
	public void updateUser(User user);
	public User getUser(String email);
	
}
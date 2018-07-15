package com.niit.dao;

import org.springframework.stereotype.Repository;

import com.niit.model.ProfilePicture;

@Repository
public interface ProfilePicDao {

	void uploadProfilePicture(ProfilePicture profilePicture);
	ProfilePicture getProfilePicture(String email);
	
}

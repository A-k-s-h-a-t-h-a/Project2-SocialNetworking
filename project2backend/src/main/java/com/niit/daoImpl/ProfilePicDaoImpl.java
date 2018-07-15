package com.niit.daoImpl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.dao.ProfilePicDao;
import com.niit.model.ProfilePicture;

@Repository
@Transactional
public class ProfilePicDaoImpl implements ProfilePicDao{

	@Autowired
	private SessionFactory sessionFactory;

	public void uploadProfilePicture(ProfilePicture profilePicture)
	{
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(profilePicture);

	}

	public ProfilePicture getProfilePicture(String email) 
	{
		Session session = sessionFactory.getCurrentSession();
		ProfilePicture profilePicture = (ProfilePicture) session.get(ProfilePicture.class, email);
		System.out.println("DAO " + email + " " + profilePicture);
		return profilePicture;
	}
	
}

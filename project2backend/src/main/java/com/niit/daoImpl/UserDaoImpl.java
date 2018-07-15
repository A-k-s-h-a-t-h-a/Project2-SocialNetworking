package com.niit.daoImpl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.UserDao;
import com.niit.model.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void registration(User user) {
		Session session=sessionFactory.getCurrentSession();
		session.save(user);
	}
	
	public boolean isEmailUnique(String email) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from User where email=?");
		query.setString(0, email);
		User user=(User)query.uniqueResult();
		
		if(user!=null) //NOT UNIQUE
			return false;
		else
			return true; //UNIQUE
	}
	
	public User login(User user) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from User where email=:email and password=:password");
		query.setString("email", user.getEmail());
		query.setString("password", user.getPassword());
		User validUser=(User)query.uniqueResult();
		return validUser; 
	}
	
	public void updateUser(User user) {
		Session session=sessionFactory.getCurrentSession();
		session.update(user);
	}
	
	public User getUser(String email) {
		Session session=sessionFactory.getCurrentSession();
		User user=(User)session.get(User.class, email);
		return user;
	}

}
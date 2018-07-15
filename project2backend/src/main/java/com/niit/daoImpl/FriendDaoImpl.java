package com.niit.daoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.dao.FriendDao;
import com.niit.model.Friend;
import com.niit.model.User;

@Repository("friendDao")
@Transactional
public class FriendDaoImpl implements FriendDao {

	@Autowired
	private SessionFactory sessionFactory;

	
	public void addFriend(Friend friend) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(friend);
	}
	

	public List<Friend> listOfFriends(String email) 
	{
		Session session = sessionFactory.getCurrentSession();
		
		Query query1 = session.createQuery("select f.toId from Friend f where f.fromId.email= '"+ email +"' and f.status='A'");
		List<Friend> friendsList1 = query1.list();
		
		Query query2 = session.createQuery("select f.fromId from Friend f where f.toId.email= '"+ email +"' and f.status='A'");
		List<Friend> friendsList2 = query2.list();
		
		friendsList1.addAll(friendsList2);
		return friendsList1;
	}
	
	
	public List<User> suggestedUsers(String email) 
	{
		Session session = sessionFactory.getCurrentSession();
		String queryString = "select * from c_user where email in " + "(select email from c_user where email!=? " + "minus " + "(select toId_email from c_friend where fromId_email=? " + "union select fromId_email from c_friend where toId_email=?))";
		SQLQuery query = session.createSQLQuery(queryString);
		query.setString(0, email);
		query.setString(1, email);
		query.setString(2, email);
		List<User> suggestedUsers = query.list();
		return suggestedUsers;
	}

	
	public List<Friend> pendingRequests(String toEmailId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Friend where status='P' and toId.email='"+ toEmailId+"'");
		List<Friend> pendingRequests = query.list();
		return pendingRequests;
	}

	
	public void acceptRequest(Friend request) 
	{
		Session session = sessionFactory.getCurrentSession();
		request.setStatus('A');
		session.update(request);
	}
	

	public void deleteRequest(Friend request) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.delete(request);
	}

}

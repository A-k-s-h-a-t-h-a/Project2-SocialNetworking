package com.niit.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.JobDao;
import com.niit.model.Job;

@Repository
@Transactional
public class JobDaoImpl implements JobDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveJob(Job job) {
		Session session=sessionFactory.getCurrentSession();
		session.save(job);
	}
	
	public List<Job> getActiveJobs() {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Job where active=true");
		return query.list(); 
	}
	
	public List<Job> getInActiveJobs() {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from Job where active=false");
		return query.list();
	}
	
	public void updateJob(Job job) {
	  Session session=sessionFactory.getCurrentSession();
	  session.update(job);
	}
	
}
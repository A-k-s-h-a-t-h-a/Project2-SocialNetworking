package com.niit.dao;

import java.util.List;

import com.niit.model.Job;

public interface JobDao {
	
	public void saveJob(Job job);
	public List<Job> getActiveJobs();
  	public List<Job> getInActiveJobs();
	public void updateJob(Job job);
  
}

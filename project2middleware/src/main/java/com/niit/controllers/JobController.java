package com.niit.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.JobDao;
import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.Job;
import com.niit.model.User;

@RestController
public class JobController {
	
    @Autowired
	private JobDao jobDao;
    
    @Autowired
    private UserDao userDao;
    
    
    @RequestMapping(value="/addjob",method=RequestMethod.POST)
    public ResponseEntity<?> saveJob(@RequestBody Job job,HttpSession session)
    {
    	String email=(String)session.getAttribute("email");	
    	if(email==null){										//Authentication
    		ErrorClazz errorClazz=new ErrorClazz(7,"Unauthorized access.. please login");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	User user=userDao.getUser(email);
    	if(!user.getRole().equals("ADMIN")){					//Authorization
    		ErrorClazz errorClazz=new ErrorClazz(8,"Access Denied..");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	try{
    		job.setPostedOn(new Date());
    		job.setActive(true);
	    	jobDao.saveJob(job);
	    	return new ResponseEntity<Void>(HttpStatus.OK);
	    	}
    	catch(Exception e){
	    		ErrorClazz errorClazz=new ErrorClazz(4,"Unable to insert job details.."+e.getMessage());
	    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    
    @RequestMapping(value="/activejobs",method=RequestMethod.GET)
    public ResponseEntity<?> getActiveJobs(HttpSession session)
    {
    	String email=(String)session.getAttribute("email");
    	if(email==null){
    		ErrorClazz errorClazz=new ErrorClazz(7,"Unauthorized access.. please login");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	List<Job> activeJobs=jobDao.getActiveJobs();
    	if(activeJobs.size()!=0)
    	System.out.println("Active jobs available");
    	return new ResponseEntity<List<Job>>(activeJobs,HttpStatus.OK);
    }
    
    
    @RequestMapping(value="/inactivejobs",method=RequestMethod.GET)
    public ResponseEntity<?> getInActiveJobs(HttpSession session)
    {
    	String email=(String)session.getAttribute("email");
    	if(email==null){
    		ErrorClazz errorClazz=new ErrorClazz(7,"Unauthorized access.. please login");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	User user=userDao.getUser(email);
    	if(!user.getRole().equals("ADMIN")){
    		ErrorClazz errorClazz=new ErrorClazz(8,"Access Denied..");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
        List<Job> inActiveJobs=jobDao.getInActiveJobs();
        return new ResponseEntity<List<Job>>(inActiveJobs,HttpStatus.OK);
    }
    
    
    @RequestMapping(value="/updatejob",method=RequestMethod.PUT)
    public ResponseEntity<?> updateJob(HttpSession session,@RequestBody Job job)
    {
    	String email=(String)session.getAttribute("email");
    	if(email==null){
    		ErrorClazz errorClazz=new ErrorClazz(7,"Unauthorized access.. please login");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	User user=userDao.getUser(email);
    	if(!user.getRole().equals("ADMIN")){
    		ErrorClazz errorClazz=new ErrorClazz(8,"Access Denied..");
    		return new ResponseEntity<ErrorClazz>(errorClazz,HttpStatus.UNAUTHORIZED);
    	}
    	
    	jobDao.updateJob(job);
    	return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
}

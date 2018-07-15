app.factory('JobService',function($http)
{
	var jobservice= {}
	
	jobservice.addJob= function(job){
	return $http.post("http://localhost:8088/project2middleware/addjob", job)
    }
	
	jobservice.getActiveJobs= function(){
		return $http.get("http://localhost:8088/project2middleware/activejobs")
	}
	
	jobservice.getInActiveJobs= function(){
		return $http.get("http://localhost:8088/project2middleware/inactivejobs")
	}
	
	jobservice.updateActiveStatus= function(job){
		return $http.put("http://localhost:8088/project2middleware/updatejob", job)
	}
	
	return jobservice;
})
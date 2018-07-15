
app.factory('UserService',function($http)
{
	var userService = {}
	
	userService.register = function(user){
		return $http.post("http://localhost:8088/project2middleware/register",user)
	}
	
	userService.login = function(user){
		return $http.post("http://localhost:8088/project2middleware/login",user)
	}
	
	userService.logout = function(){
		return $http.put("http://localhost:8088/project2middleware/logout")
	}
     
	userService.updateProfile = function(user){
		return $http.put("http://localhost:8088/project2middleware/update",user)
	}
	
	return userService;
})

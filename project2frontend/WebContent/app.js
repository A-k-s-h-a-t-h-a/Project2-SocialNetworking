var app= angular.module("app",['ngRoute','ngCookies'])
app.config(function($routeProvider)
{
	$routeProvider
	.when('/home',{
		templateUrl:'home.html'
	})
	
	
	.when('/registration',
	{	controller:'UserCtrl',
		templateUrl:'user/registrationform.html'
	})
	.when('/login',
	{	controller:'UserCtrl',
		templateUrl:'user/login.html'
	})
	.when('/updateprofile',
	{	controller:'UserCtrl',
		templateUrl:'user/updateprofile.html'
	})
	.when('/uploadprofilepic',
	{	templateUrl:'user/profilepic.html'
	})
	
	
	.when('/addjob',
	{	controller:'JobCtrl',
		templateUrl:'job/job.html'
	})
	.when('/activejobs',
	{	controller:'JobCtrl',
		templateUrl:'job/activejobslist.html'
	})
	.when('/inactivejobs',
	{	controller:'JobCtrl',
		templateUrl:'job/inactivejobslist.html'
	})
	
	
	.when('/addblog',{
		controller:'BlogCtrl',
		templateUrl:'blog/blog.html'
	})
	.when('/blogswaitingforapproval',{
		controller:'BlogCtrl',
		templateUrl:'blog/waitingblogs.html'
	})
	.when('/blogsapproved',{
		controller:'BlogCtrl',
		templateUrl:'blog/approvedblogs.html'
	})
	.when('/getblogpost/:id',{
		controller:'BlogCtrl',
		templateUrl:'blog/showblog.html'
	})
		
	
	.when('/myfriends',{
		controller:'FriendCtrl',
		templateUrl:'friend/friendslist.html'
	})
	.when('/requestspending',{
		controller:'FriendCtrl',
		templateUrl:'friend/pendingrequests.html'
	})
	.when('/suggestedfriends',{
		controller:'FriendCtrl',
		templateUrl:'friend/suggestedusers.html'
	})
		
	
	.when('/chat',{
		controller:'ChatCtrl',
		templateUrl:'chat/chat.html'
	})
	
	
	.otherwise({
		templateUrl:'brand.html'
	})
})


app.run(function($rootScope, $cookieStore, UserService, $location)
{
	if($rootScope.loggedInUser == undefined)
		$rootScope.loggedInUser=$cookieStore.get('loggedInUser')
		
	$rootScope.logout= function()
	{
		UserService.logout().then(function(response)
		{
			$rootScope.message= "Loggedout successfully..."
			delete $rootScope.loggedInUser
			$cookieStore.remove('loggedInUser')
			$location.path('/login')
		},
		function(response)
		{
			$rootScope.error= response.data //ErrorClazz object returned from middleware
			$location.path('/login')
		})
	}	
})

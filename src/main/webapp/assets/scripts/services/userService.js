

twitterDemoModule.service('userService', [ '$http','dataService', function($http,$dataService) {
	
	this.create = function(login,success,error) {
		var userCreation = $dataService.getUserByLogin(login);
		$http.post('user',userCreation).success(success).error(error);
	};
	
	this.getDetails = function(login,success,error) {
		$http.get('user?userLogin='+login).success(success).error(error);
	};
	
	this.manageRelations = function(action,userLogin,friendLogin,success,error) {
		if(action==='follows') {			
			$http.put('user/friend?userLogin='+userLogin+"&friendLogin="+friendLogin)
			.success(success).error(error);
		} else if(action==='unfollows') {
			$http.delete('user/friend?userLogin='+userLogin+"&friendLogin="+friendLogin)
			.success(success).error(error);
		}
	};
	
	this.listRelations = function(action,login,success,error) {
		if(action==='list-friends') {			
			$http.get('user/friends?userLogin='+login)
			.success(success).error(error);
		} else if(action==='list-followers') {
			$http.get('user/followers?userLogin='+login)
			.success(success).error(error);
		}
	};
	
}]);
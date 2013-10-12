twitterDemoModule.service('lineService', [ '$http', function($http) {
	
	this.line = function(type,login,success,error) {
		$http.get(type+'?userLogin='+login+'&length=10')
		.success(success).error(error);
	};
	
	this.tagline = function(tag,success,error) {
		$http.get('tagline?tag='+tag+'&length=10')
		.success(success).error(error);
	};
	
}]);	
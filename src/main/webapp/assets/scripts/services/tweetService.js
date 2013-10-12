
twitterDemoModule.service('tweetService', [ '$http','dataService', function($http,$dataService) {
	
	this.create = function(tweetName,login,content,success,error) {
		var successFn = function(data,status) {
			$dataService.addTweetId(tweetName,data);
			success(data,status);
		};
		$http.post('tweet?authorLogin='+login,content).success(successFn).error(error);
	};
	
	this.getDetails = function(tweetId,success,error) {
		$http.get('tweet?tweetId='+tweetId).success(success).error(error);
	};
	
	this.remove = function(tweetId,tweetName,success,error) {
		var successFn = function(data,status) {
			$dataService.removeTweetId(tweetName);
			success(data,status);
		};
		$http.delete('tweet?tweetId='+tweetId).success(successFn).error(error);
	};
	
	this.favorite = function(tweetId,login,success,error) {
		$http.put('tweet/favorite?userLogin='+login+'&tweetId='+tweetId).success(success).error(error);
	};

	
}]);
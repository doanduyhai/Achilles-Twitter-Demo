
twitterDemoModule.service('dataService', function($http) {
	var repository = {
        user_creation : {
        	"jdoe": {
        		"login": "jdoe",
        	    "firstname": "John",
        	    "lastname": "DOE"
        	},
        	"hsue": {
        		"login": "hsue",
        	    "firstname": "Helen",
        	    "lastname": "SUE"
        	},
        	"rsmith": {
        		"login": "rsmith",
        	    "firstname": "Richard",
        	    "lastname": "SMITH"
        	},
        },
        tweetContents : {
        	"Tweet1" : "Tweet1 : #Achilles makes modeling with #Cassandra much easier",
        	"Tweet2" : "Tweet2 : This is a great live demo for #Achilles",
        	"Tweet3" : "Tweet3 : Have a look at #Achilles, a Persistence Manager for #Cassandra db",
        	"Tweet4" : "Tweet4 : When will #Java 8 be out ?? Can't wait for it",
        	"Tweet5" : "Tweet5 : #Achilles is to #Cassandra as #ORM is to #SQL",
        	"Tweet6" : "Tweet6 : Yet another tweet",
        	"Tweet7" : "Tweet7 : @hsue, @rsmith and @foobar, you should try #Achilles"
        },
		tweets: {}
	};
	
	this.cleanData = function() {
		repository.tweets = {};
	}
	
	this.getUserByLogin = function(login) {
		return repository.user_creation[login];
	};
	
	this.getUserCreationPayload = function(login) {
		return this.stringify(this.getUserByLogin(login));
	};
	
	this.addTweetId = function(tweetName,tweetId) {
		repository.tweets[tweetName] = tweetId;
	};
	
	this.getTweetId = function(tweetName) {
		var tweetId = repository.tweets[tweetName];
		if(tweetId) {
			return tweetId.replace(/"/g,'');
		} else {
			return "<input tweet id here>";
        }
	};
	
	this.getTweetContent = function(tweetName) {
		return repository.tweetContents[tweetName];
	};

	this.removeTweetId = function(tweetName) {
		delete repository.tweets[tweetName];
	};
	
	this.stringify = function(json) {
		return JSON.stringify(json,null,4);
	};
	
	this.createSuccess = function($scope) {
		return function(data,status) {
			$scope.success = true;
			$scope.error = false;
			$scope.httpResponse = {
				code: status,
				data: data
			};
		};
	};
	
	this.createError = function($scope) {
		return function(data,status) {
			$scope.success = false;
			$scope.error = true;
			$scope.httpResponse = {
				code: status,
				data: data
			};
		};
	};
});
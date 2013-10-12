'use strict';

twitterDemoModule.controller('TweetCtrl', [ '$scope','$routeParams','dataService','tweetService',
                                           function($scope,$routeParams,$dataService,$tweetService) {
	$scope.success=false;
	$scope.error=false;
	$scope.action = $routeParams.action;
	
	if($scope.action === 'create') {
		$scope.login = $routeParams.param1;
		$scope.tweetName = $routeParams.param2;
		$scope.user = $dataService.getUserByLogin($scope.login);
		$scope.tweetContent = $dataService.getTweetContent($scope.tweetName);
	} else if($scope.action === 'remove') {
		$scope.tweetName = $routeParams.param1;
		$scope.tweetId = $dataService.getTweetId($scope.tweetName);
	} else if( $scope.action === 'get-details') {
		$scope.tweetName = $routeParams.param1;
		$scope.tweetId = $dataService.getTweetId($scope.tweetName);
	} else if($scope.action === 'favorites') {
		$scope.login = $routeParams.param1;
		$scope.tweetName = $routeParams.param2;
		$scope.user = $dataService.getUserByLogin($scope.login);
		$scope.tweetId = $dataService.getTweetId($scope.tweetName);
	}

	$scope.createTweet = function() {
		$tweetService.create($scope.tweetName,$scope.login,$scope.tweetContent,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.removeTweet = function(tweetId) {
		$tweetService.remove(tweetId,$scope.tweetName,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.getTweetDetails = function(tweetId) {
		$tweetService.getDetails(tweetId,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.addToFavorite = function(tweetId) {
		$tweetService.favorite(tweetId,$scope.login,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
} ]);

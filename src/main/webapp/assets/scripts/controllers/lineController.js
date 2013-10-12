'use strict';

twitterDemoModule.controller('LineCtrl', [ '$scope','$routeParams','dataService','lineService',
                                           function($scope,$routeParams,$dataService,$lineService) {
	$scope.success=false;
	$scope.error=false;
	$scope.type = $routeParams.type;
	
	if($scope.type === 'userline' ||
			$scope.type === 'timeline' ||
			$scope.type === 'favoriteline' ||
			$scope.type === 'mentionline') {
		$scope.login = $routeParams.param1;
		$scope.user = $dataService.getUserByLogin($scope.login);
		
	} else if($scope.type === 'tagline') {
		$scope.tag = $routeParams.param1;
	} 

	$scope.listTweets = function() {
		$lineService.line($scope.type,$scope.login,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.listTweetsForTag = function() {
		$lineService.tagline($scope.tag,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};	
} ]);

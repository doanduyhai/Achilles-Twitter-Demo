'use strict';

twitterDemoModule.controller('UserCtrl', [ '$scope','$routeParams','dataService','userService',
                                           function($scope,$routeParams,$dataService,$userService) {
	$scope.success=false;
	$scope.error=false;
	$scope.action = $routeParams.action;
	
	if($scope.action === 'follows' || $scope.action === 'unfollows') {
		$scope.userLogin = $routeParams.param1;
		$scope.friendLogin = $routeParams.param2;
		$scope.user = $dataService.getUserByLogin($scope.userLogin);
		$scope.friend = $dataService.getUserByLogin($scope.friendLogin);
	} else if($scope.action === 'create') {
		$scope.login = $routeParams.param1;
		$scope.user_to_create = $dataService.getUserByLogin($scope.login);
		$scope.user_payload = $dataService.getUserCreationPayload($scope.login);
	} else if( $scope.action === 'get-details') {
		$scope.login = $routeParams.param1;
		$scope.user = $dataService.getUserByLogin($scope.login);
	} else if($scope.action === 'list-followers' || $scope.action === 'list-friends') {
		$scope.login = $routeParams.param1;
		$scope.user = $dataService.getUserByLogin($scope.login);
	}

	$scope.createUser = function() {
		$userService.create($scope.login,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.manageRelations = function() {
		$userService.manageRelations($scope.action,$scope.userLogin,$scope.friendLogin,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.getUserDetails = function() {
		$userService.getDetails($scope.login,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
	$scope.listRelations = function() {
		$userService.listRelations($scope.action,$scope.login,
				$dataService.createSuccess($scope),
				$dataService.createError($scope));
	};
	
} ]);

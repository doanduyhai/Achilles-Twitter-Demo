
twitterDemoModule.controller('DatabaseCtrl', [ '$scope','$http','dataService',function($scope,$http,$dataService) {
    $scope.success = false;
    $scope.error = false;

    $scope.resetDb = function() {
        $http.put('db/reset').success(function(data){
            $scope.success = true;
            $scope.error = false;
            $scope.successMessage="Database cleaned successfully";
            $dataService.cleanData();
        }).error(function(data){
            $scope.success = false;
            $scope.error = true;
            $scope.errorMessage=data;
        });
    };
}]);

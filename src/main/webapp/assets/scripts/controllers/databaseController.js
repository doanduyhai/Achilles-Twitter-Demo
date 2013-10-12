
twitterDemoModule.controller('DatabaseCtrl', [ '$scope','$http',function($scope,$http) {
    $scope.success = false;
    $scope.error = false;

    $scope.resetDb = function() {
        console.log("call reset db");
        $http.put('db/reset').success(function(data){
            $scope.success = true;
            $scope.error = false;
            $scope.successMessage="Database cleaned successfully";
        }).error(function(data){
            $scope.success = false;
            $scope.error = true;
            $scope.errorMessage=data;
        });
    };
}]);

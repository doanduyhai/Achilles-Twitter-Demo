'use strict';

var twitterDemoModule = angular.module('twitterDemoApp', ['ngRoute','ui.include','ui.bootstrap'])
    .config(function ($routeProvider, $httpProvider) {
        $httpProvider.defaults.headers.common = {'Accept': 'application/json', 'Content-Type': 'application/json'};

        $routeProvider
            .when('/', {
                templateUrl: 'assets/views/main.html'
            })
            .when('/user/:action/:param1/:param2?', {
            	templateUrl: 'assets/views/user.html'
            })
	        .when('/tweet/:action/:param1/:param2?', {
	        	templateUrl: 'assets/views/tweet.html'
	        })
	        .when('/line/:type/:param1', {
	        	templateUrl: 'assets/views/line.html'
	        });	        
        
});


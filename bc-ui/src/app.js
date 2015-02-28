(function (window, angular) {
    "use strict";

    angular.module('module.main', [
        //lib
        'ui.bootstrap',
        'ui.router',
        'restangular',

        //template
        'module.templates',
        //app
        'module.config',
        'module.common',
        'module.widgets',
        'module.home',
        'module.login'

    ]).config([
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        'RestangularProvider',
        'appConfig',
        function ($stateProvider, $locationProvider, $urlRouterProvider, RestangularProvider, appConfig) {

            $locationProvider.hashPrefix('!').html5Mode(false);

            $urlRouterProvider.otherwise(appConfig.stateName.home);

            $stateProvider.state(appConfig.stateName.home, {
                url: '/home',
                templateUrl: 'home.main.html',
                controller: 'home.MainController'
            }).state(appConfig.stateName.login, {
                url: '/login',
                templateUrl: 'login.main.html',
                controller: 'login.MainController'
            });

            RestangularProvider.setDefaultHeaders({'Content-Type': 'application/json;charset=UTF-8'});
            RestangularProvider.setBaseUrl(appConfig.rootContext);

            /*
             RestangularProvider.setRequestInterceptor(function(elem, operation) {
             if (operation === "remove") {
             return undefined;
             }
             return elem;
             });


             growlProvider.globalTimeToLive(5000);
             growlProvider.globalPosition('top-center');
            */
        }
    ]);

})(window, window.angular);
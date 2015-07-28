(function (window, angular) {
    "use strict";

    angular.module('module.main', [
        //lib
        'ui.bootstrap',
        'ui.router',
        'restangular',
        'angular-growl',
        'angularSpinner',
        'ui.codemirror',

        //template
        'module.templates',
        //app
        'module.config',
        'module.common',
        'module.widgets',
        'module.home',
        'module.login',
        'module.register',
        'module.user-admin',
        'module.channel-admin',
        'module.mocker',
        'module.rule',
        'module.mock-activity',
        'module.password-admin'

    ]).config([
        '$stateProvider',
        '$locationProvider',
        '$urlRouterProvider',
        'RestangularProvider',
        'growlProvider',
        'appConfig',
        function ($stateProvider, $locationProvider, $urlRouterProvider,
                  RestangularProvider, growlProvider, appConfig) {

            $locationProvider.hashPrefix('!').html5Mode(false);

            $urlRouterProvider.otherwise(appConfig.stateName.mockerDashboard);

            $stateProvider.state(appConfig.stateName.home, {
                url: '/home',
                templateUrl: 'home.main.html',
                controller: 'home.MainController'
            }).state(appConfig.stateName.login, {
                url: '/login',
                templateUrl: 'login.main.html',
                controller: 'login.MainController'
            }).state(appConfig.stateName.register, {
                url: '/signup',
                templateUrl: 'register.main.html',
                controller: 'register.MainController'
            }).state(appConfig.stateName.userAdmin, {
                url: '/user-admin',
                templateUrl: 'user-admin.main.html',
                controller: 'user-admin.MainController'
            }).state(appConfig.stateName.channelAdmin, {
                url: '/channel-admin',
                templateUrl: 'channel-admin.main.html',
                controller: 'channel-admin.MainController'
            }).state(appConfig.stateName.mockerCreator, {
                url: '/mocker-creator',
                templateUrl: 'mocker.creator.main.html',
                controller: 'mocker.creator.MainController'
            }).state(appConfig.stateName.mockerViewer, {
                url: '/mocker-viewer?mid',
                templateUrl: 'mocker.viewer.main.html',
                controller: 'mocker.viewer.MainController'
            }).state(appConfig.stateName.mockerSetting, {
                url: '/mocker-setting?mid',
                templateUrl: 'mocker.setting.main.html',
                controller: 'mocker.setting.MainController'
            }).state(appConfig.stateName.mockerDashboard, {
                url: '/mocker-dashboard',
                templateUrl: 'mocker.dashboard.main.html',
                controller: 'mocker.dashboard.MainController'
            }).state(appConfig.stateName.ruleEditor, {
                url: '/rule-editor?mid&rid',
                templateUrl: 'rule.editor.main.html',
                controller: 'rule.editor.MainController'
            }).state(appConfig.stateName.mockActivity, {
                url: '/mock-activity?maid',
                templateUrl: 'mock-activity.main.html',
                controller: 'mock-activity.MainController'
            }).state(appConfig.stateName.passwordAdmin, {
                url: '/password-admin?r',
                templateUrl: 'password-admin.main.html',
                controller: 'password-admin.MainController'
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
             */

             growlProvider.globalTimeToLive(5000);
             growlProvider.globalPosition('top-center');

        }
    ]).run([
        '$state',
        'Restangular',
        'userService',
        'alertService',
        'appConfig',
        function ($state, Restangular, userService, alertService, appConfig) {

            Restangular.setErrorInterceptor(
                function(resp) {
                    if (resp && resp.status === 401 &&
                        resp.data && resp.data.path &&
                        (_.endsWith(resp.data.path, '/login') ||
                        _.endsWith(resp.data.path, 'users/auth'))) {
                        return true;
                    } else if (resp && resp.status === 401) {
                        alertService.warning('请先登录。');
                        $state.go(appConfig.stateName.login);
                    } else if (resp && resp.status === 403) {
                        alertService.error('只有管理员能使用这个功能，请用管理员账号登录。');
                    } else {
                        alertService.error('无法访问MockServer，请确认是否有网络！');
                    }
                    //loggerService.error(response);
                    return false; // stop the promise chain
                }
            );

            userService.getAuthUser().then(function(resp){
                userService.setCurrentUser(resp);
            });

            //loggerService.setLevel(appConfig.loggerLevel);
        }
    ]);

})(window, window.angular);
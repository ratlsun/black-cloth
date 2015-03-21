(function (window, angular) {
    "use strict";

    angular.module('module.config', [])
        .constant('appConfig', {
            rootContext: '/bc',

            stateName: {
                home: 'home',
                login: 'login',
                register: 'register',
                user_admin: 'user_admin'
            },

            userStatus: {
                new: 'New',
                active: 'Active',
                inactive: 'Inactive'
            }

        }).run([
            '$rootScope',
            'appConfig',
            function ($rootScope, appConfig) {
                $rootScope.appConfig = appConfig;
        }]);

})(window, window.angular);
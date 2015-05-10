(function (window, angular) {
    "use strict";

    angular.module('module.config', [])
        .constant('appConfig', {
            rootContext: '/bc',

            stateName: {
                home: 'home',
                login: 'login',
                register: 'register',
                userAdmin: 'user-admin',
                channelAdmin: 'channel-admin',
                mockerCreator: 'mocker-creator',
                mockerViewer: 'mocker-viewer',
                mockerSetting: 'mocker-setting',
                mockerDashboard: 'mocker-dashboard',
                ruleEditor: 'rule-editor'
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
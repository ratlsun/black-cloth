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
                mockActivity: 'mock-activity',
                ruleEditor: 'rule-editor'
            },

            userStatus: {
                new: 'New',
                active: 'Active',
                inactive: 'Inactive'
            },

            changeType: {
                ChangeMockerRules: {
                    typeName: 'ChangeMockerRules',
                    icon: 'fa-retweet',
                    theme: 'panel-primary'
                },
                ChangeMockerName: {
                    typeName: 'ChangeMockerName',
                    icon: 'fa-certificate',
                    theme: 'panel-info'
                },
                ChangeMockerOwner: {
                    typeName: 'ChangeMockerOwner',
                    icon: 'fa-user',
                    theme: 'panel-warning'
                },
                ChangeMockerType: {
                    typeName: 'ChangeMockerType',
                    icon: {
                        toPublic: 'fa-unlock-alt',
                        toPrivate: 'fa-lock'
                    },
                    theme: 'panel-warning'
                },
                CreateMocker: {
                    typeName: 'CreateMocker',
                    icon: 'fa-plus',
                    theme: 'panel-success'
                },
                DeleteMocker: {
                    typeName: 'DeleteMocker',
                    icon: 'fa-trash',
                    theme: 'panel-danger'
                }
            }

        }).run([
            '$rootScope',
            'appConfig',
            function ($rootScope, appConfig) {
                $rootScope.appConfig = appConfig;
        }]);

})(window, window.angular);
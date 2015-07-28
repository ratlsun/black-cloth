(function (window, angular) {
    "use strict";

    angular.module('module.config', [])
        .constant('appConfig', {
            rootContext: '/',

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
                ruleEditor: 'rule-editor',
                passwordAdmin: 'password-admin'
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
                    theme: 'panel-info'
                },
                ChangeMockerType: {
                    typeName: 'ChangeMockerType',
                    icon: {
                        toPublic: 'fa-unlock-alt',
                        toPrivate: 'fa-lock'
                    },
                    theme: 'panel-info'
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
                },
                CollectMocker: {
                    typeName: 'CollectMocker',
                    icon: 'fa-eye',
                    theme: 'panel-info'
                },
                CancelCollectMocker: {
                    typeName: 'CancelCollectMocker',
                    icon: 'fa-eye-slash',
                    theme: 'panel-danger'
                },
                MockActivity: {
                    typeName: 'MockActivity',
                    icon: 'fa-youtube-play',
                    theme: 'panel-warning'
                }

            },

            alertMsg: {
                userModule: {
                    '-11': '原密码错误，无法修改密码！'
                }
            }

        }).run([
            '$rootScope',
            'appConfig',
            function ($rootScope, appConfig) {
                $rootScope.appConfig = appConfig;
        }]);

})(window, window.angular);

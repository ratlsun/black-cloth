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
                MockActivity: {
                    typeName: 'MockActivity',
                    icon: 'fa-youtube-play',
                    theme: 'panel-warning'
                }

            },

            alertMsg: {
                user: {
                    error: {
                        '-11': '原密码错误，请重新输入！',
                        '-12': '配置文件找不到！',
                        '-13': '邮件发送失败！',
                        '-14': '重置密码链接失效！',
                        '-15': '用户名不存在'
                    }                        
                }
            }

        }).run([
            '$rootScope',
            'appConfig',
            function ($rootScope, appConfig) {
                $rootScope.appConfig = appConfig;
        }]);

})(window, window.angular);
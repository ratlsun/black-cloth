(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.ListController', [
            '$scope',
            '$timeout',
            'userService',
            'pageService',
            'alertService',
            'appConfig',
            function ($scope, $timeout, userService, pageService, alertService, appConfig) {

                var convertUser = function(user){
                    if (user.status === appConfig.userStatus.inactive) {
                        user.level = '禁用';
                        user.icon = 'user-times';
                        user.canBan = false;
                        user.canActive = true;
                    } else if (user.status === appConfig.userStatus.new) {
                        user.level = '申请中';
                        user.icon = 'user-plus';
                        user.canBan = true;
                        user.canActive = true;
                    } else if (_.indexOf(user.roles, 'ADMIN') > -1) {
                        user.level = '管理员';
                        user.icon = 'user-secret';
                        user.canBan = false;
                        user.canActive = false;
                    } else if (_.indexOf(user.roles, 'USER') > -1) {
                        user.level = '普通用户';
                        user.icon = 'user';
                        user.canBan = true;
                        user.canActive = false;
                    } else {
                        user.level = '无效用户';
                        user.icon = 'question-circle';
                        user.canBan = true;
                        user.canActive = false;
                    }
                };

                $scope.users = [];
                $scope.refreshData = function(){
                    pageService.mask('user-list-spinner');
                    userService.getAllUsers().then(function(resp) {
                        _.forEach(resp, function(user){
                            convertUser(user);
                        });

                        $scope.users = resp;
                        pageService.unmask('user-list-spinner');
                    });
                };
                $scope.refreshData();

                $scope.activeUser = function(user){
                    userService.activeUser(user.id).then(function(resp){
                        user.status = resp.status;
                        convertUser(user);
                        alertService.success('激活用户［' + user.name + '］成功。');
                    });
                };

                $scope.inactiveUser = function(user){
                    userService.inactiveUser(user.id).then(function(resp){
                        user.status = resp.status;
                        convertUser(user);
                        alertService.success('禁用用户［' + user.name + '］成功。');
                    });
                }
            }
        ]);

})(window, window.angular);
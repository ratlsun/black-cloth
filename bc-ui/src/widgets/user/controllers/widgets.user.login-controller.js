(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.LoginController', [
            '$scope',
            'userService',
            'alertService',
            'appConfig',
            function ($scope, userService, alertService, appConfig) {

                $scope.user = {
                    //username: 'hale@hale.com',
                    //password: 'hale'
                };
                $scope.invalidMessage = {};

                $scope.$watch('user.username', function () {
                    $scope.invalidMessage.$form = "";
                });

                $scope.$watch('user.password', function () {
                    $scope.invalidMessage.$form = '';
                });

                $scope.login = function () {
                    userService.login($scope.user).then(function(){
                        userService.getUserByName($scope.user.username).then(function(resp){
                            userService.setCurrentUser(resp);
                            $scope.postSignIn();
                        });
                    }, function(){
                        $scope.invalidMessage.$form = '用户不存在或者密码错误！';
                    })
                };

                $scope.forgetPwd = function () {
                    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test($scope.user.username)) {
                        $scope.invalidMessage.$form = '请输入正确格式的邮箱！';
                    } else {
                        userService.getUserByName($scope.user.username).then(function(resp){
                            if (resp) {
                                $scope.userInfo = {
                                    'id': resp.id,
                                    'name': resp.name
                                };
                                alertService.info("正在发送密码重置链接到您的邮箱！");
                                userService.forgetPwd($scope.userInfo).then(function(resp){
                                    if (resp.result < 0) {
                                        $scope.invalidMessage.$form = appConfig.alertMsg.user.error[resp.result.toString()];
                                    } else {
                                        alertService.success('密码重置的邮件已发送到您的邮箱，请在2天内重置密码！');
                                    }
                                });
                            } else{
                                alertService.warning(appConfig.alertMsg.user.error['-15']);
                            };
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
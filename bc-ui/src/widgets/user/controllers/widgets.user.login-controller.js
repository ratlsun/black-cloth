(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.LoginController', [
            '$scope',
            'userService',
            'alertService',
            function ($scope, userService, alertService) {

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

                $scope.forgetPwd = function (username) {
                    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test(username)) {
                        $scope.invalidMessage.$form = '请输入正确格式的邮箱！';
                    } else {
                        alertService.info("正在发送密码重置链接！");
                        userService.forgetPwd(username).then(function(resp){
                            if (resp.result === 1) {
                                alertService.success(resp.msg);
                            } else {
                                $scope.invalidMessage.$form = resp.errorMsg;
                            }
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
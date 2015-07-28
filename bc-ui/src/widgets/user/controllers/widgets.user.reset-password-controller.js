(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.ResetPasswordController', [
            '$scope',
            'userService',
            'alertService',
            function ($scope, userService, alertService) {
                $scope.userInfo = {};
                $scope.canEdit = true;
                $scope.isReset = false;
                if ($scope.pwdCode) {
                    userService.checkPasswordCode($scope.pwdCode).then(function(resp){
                        if (resp) {
                            $scope.isReset = true;
                            $scope.userInfo = {
                                id: resp.id,
                                name: resp.name,
                                pwdCode: resp.pwdCode
                            };
                        } else{
                            alertService.warning("重置密码的链接已经失效，请访问最新的链接或者重新申请！");
                        }
                    });
                }
                
                $scope.invalidMessage = {};

                var validatePwd = function (){
                    var isValid = true;
                    $scope.invalidMessage.$form = null;
                    if ($scope.userInfo.newPwd && $scope.userInfo.confirmedPwd
                        && $scope.userInfo.newPwd !== $scope.userInfo.confirmedPwd) {
                        $scope.invalidMessage.$form = '第二次输入的密码与第一次不一致！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.$watch('userInfo.newPwd', function () {
                    validatePwd();
                });

                $scope.$watch('userInfo.confirmedPwd', function () {
                    validatePwd();
                });

                $scope.reset = function () {
                    if (!validatePwd()) {
                        return;
                    }
                    userService.resetPassword($scope.userInfo).then(function(resp){
                        if (!resp || resp.result < 0) {
                            $scope.isReset = false;
                            alertService.warning("重置密码的链接已经失效，请访问最新的链接或者重新申请！");
                        } else {
                            alertService.success('新密码已经生效，请重新登录。');
                            $scope.onGoBack();
                        }
                    });
                };

                $scope.apply = function () {
                    userService.applyResetPassword($scope.userInfo.name).then(function(resp){
                        if (!resp || resp.result < 0) {
                            $scope.invalidMessage.$form = '请输入正确的用户名！';
                        } else {
                            alertService.success('密码重置邮件已发送到您的邮箱，请在2天内重置密码。');
                            $scope.onGoBack();
                        }
                    });
                };
            }
        ]);

})(window, window.angular);
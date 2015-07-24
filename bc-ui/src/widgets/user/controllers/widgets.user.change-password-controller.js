(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.ChangePasswordController', [
            '$scope',
            '$timeout',
            '$modalInstance',
            'userService',
            'alertService',
            'appConfig',
            function ($scope, $timeout, $modalInstance, userService, alertService, appConfig) {
                $scope.currentUser = userService.getCurrentUser();
                $scope.invalidMessage = {};
                $scope.user = {
                    password: '',
                    newPwd: '',
                    confirmedPwd: ''
                };

                var validatePwd = function (){
                    var isValid = true;
                    $scope.invalidMessage.newPwd = null;
                    if ($scope.user.newPwd && $scope.user.confirmedPwd
                        && $scope.user.newPwd !== $scope.user.confirmedPwd) {
                        $scope.invalidMessage.newPwd = '第二次输入的密码与第一次不一致！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.$watch('user.newPwd', function () {
                    validatePwd();
                });

                $scope.$watch('user.confirmedPwd', function () {
                    validatePwd();
                });

                $scope.$watch('user.password', function () {
                    $scope.invalidMessage.oldPwd = null;
                });

                $scope.dismiss = function(){
                    $modalInstance.dismiss();
                };

                $scope.save = function(){
                    if (!validatePwd()) {
                        return;
                    }
                    userService.changePassword(_.assign($scope.user, $scope.currentUser)).then(function(resp){
                        if (resp.result < 0) {
                            $scope.invalidMessage.oldPwd = appConfig.alertMsg.userModule[resp.result.toString()];
                        } else{
                            alertService.success('密码修改成功');
                            $scope.dismiss();
                        }
                    });
                };
            }
        ]);

})(window, window.angular);
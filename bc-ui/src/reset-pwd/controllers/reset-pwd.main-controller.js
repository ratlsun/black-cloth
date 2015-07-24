(function (window, angular) {
    "use strict";

    angular.module('module.reset-pwd')
        .controller('reset-pwd.MainController', [
            '$scope',
            '$state',
            '$stateParams',
            'userService',
            'alertService',
            'appConfig',
            function ($scope, $state, $stateParams, userService, alertService, appConfig) {
                $scope.pwdCode = $stateParams.r;
                $scope.userInfo = {};
                $scope.canEdit = true;
                userService.getUserByPwdCode($scope.pwdCode).then(function(resp){
                    if (resp) {
                        $scope.userInfo = {
                            'id': resp.id,
                            'name': resp.name
                        };
                        $scope.username = resp.name;
                    } else{
                        $scope.canEdit = false;
                        alertService.warning(appConfig.alertMsg.userModule['-14']);
                    };
                });
                $scope.invalidMessage = {};

                var validatePwd = function (){
                    var isValid = true;
                    $scope.invalidMessage.$form = null;
                    if ($scope.newPwd && $scope.confirmedPwd
                        && $scope.newPwd !== $scope.confirmedPwd) {
                        $scope.invalidMessage.$form = '第二次输入的密码与第一次不一致！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.$watch('newPwd', function () {
                    validatePwd();
                });

                $scope.$watch('confirmedPwd', function () {
                    validatePwd();
                });

                $scope.resetPwd = function () {
                    if (!validatePwd()) {
                        return;
                    }
                    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test($scope.username)) {
                        $scope.invalidMessage.$form = '用户名格式不正确，请核对密码重置链接！';
                    } else {
                        userService.resetPwd({'id':$scope.userInfo.id, 'name':$scope.userInfo.name, 'password':$scope.newPwd, 'pwdCode':$scope.pwdCode}).then(function(resp){
                            if (resp) {
                                if (resp.result < 0) {
                                    $scope.invalidMessage.$form = appConfig.alertMsg.userModule[resp.result.toString()];
                                } else{
                                    alertService.success('密码重置成功！');
                                    $state.go(appConfig.stateName.login);
                                };
                            } else {
                                $scope.invalidMessage.$form = '用户不存在。';
                            }
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
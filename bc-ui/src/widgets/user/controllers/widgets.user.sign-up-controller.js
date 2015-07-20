(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.SignUpController', [
            '$scope',
            'alertService',
            'userService',
            function ($scope, alertService, userService) {
                $scope.newUser = {};
                $scope.activeStep = "info";
                $scope.invalidMessage = {};

                var validatePwd = function (){
                    var isValid = true;
                    $scope.invalidMessage.pwd = null;
                    if ($scope.newUser.pwd1 && $scope.newUser.pwd2
                        && $scope.newUser.pwd1 !== $scope.newUser.pwd2) {
                        $scope.invalidMessage.pwd = '第二次输入的密码与第一次不一致！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.$watch('newUser.pwd1', function () {
                    validatePwd();
                });

                $scope.$watch('newUser.pwd2', function () {
                    validatePwd();
                });

                $scope.$watch('newUser.name', function () {
                    $scope.invalidMessage.$infoForm = null;
                });

                $scope.$watch('newUser.code', function () {
                    $scope.invalidMessage.$codeForm = null;
                });

                $scope.gotoStep = function(step) {
                    $scope.activeStep = step;
                };

                $scope.submitSignInfo = function() {
                    if (!validatePwd()) {
                        return;
                    }
                    userService.createUser({
                        name: $scope.newUser.name,
                        password: $scope.newUser.pwd1
                    }).then(function(resp){
                        if (resp.result === -1) {
                            $scope.invalidMessage.$infoForm = '该邮箱账号已经存在！';
                        } else {
                            if (resp.result === 10) {
                                alertService.success(resp.msg);
                            };
                            $scope.gotoStep('code');
                        }
                    });
                };

                $scope.submitSignCode = function() {
                    userService.activeUserByCode($scope.newUser.code).then(function(resp){
                        if (resp && 'Active' === resp.status) {
                            $scope.newUser = {};
                            $scope.gotoStep('done');
                        } else {
                            $scope.invalidMessage.$codeForm = '无效的验证码！';
                        }
                    });
                };
            }
        ]);

})(window, window.angular);
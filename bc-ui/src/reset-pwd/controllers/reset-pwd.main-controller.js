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
                $scope.username = $stateParams.name;
                $scope.timeCode = $stateParams.r;
                console.info("1:" + $scope.username);
                console.info("2:" + $scope.timeCode);

                $scope.invalidMessage = {};

                $scope.resetPwd = function () {
                    if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ .test($scope.username)) {
                        $scope.invalidMessage.$form = '用户名格式不正确，请核对密码重置链接！';
                    } else {
                        userService.resetPwd({'name':$scope.username, 'password':$scope.password, 'timeCode':$scope.timeCode}).then(function(resp){
                            if (resp.result === 1) {
                                alertService.success(resp.msg);
                                $state.go(appConfig.stateName.login);
                            } else {
                                $scope.invalidMessage.$form = resp.errorMsg;
                            }
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
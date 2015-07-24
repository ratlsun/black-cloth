(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.EditPasswordController', [
            '$scope',
            '$timeout',
            '$modalInstance',
            'userService',
            'alertService',
            'appConfig',
            function ($scope, $timeout, $modalInstance, userService, alertService, appConfig) {
                $scope.currentUser = userService.getCurrentUser();
                $scope.userName = $scope.currentUser.name;
                $scope.invalidMessage = {};

                var validatePwd = function (){
                    var isValid = true;
                    $scope.invalidMessage.$infoForm = null;
                    if ($scope.user.newPwd1 && $scope.user.newPwd2
                        && $scope.user.newPwd1 !== $scope.user.newPwd2) {
                        $scope.invalidMessage.$infoForm = '第二次输入的新密码与第一次输入的新密码不一致！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.dismiss = function(){
                    $modalInstance.dismiss();
                };

                $scope.save = function(){
                    if (!validatePwd()) {
                        return;
                    }
                    userService.editPwd({
                        id: $scope.currentUser.id,
                        name: $scope.currentUser.name,
                        password: $scope.user.password,
                        newPwd: $scope.user.newPwd1
                    }).then(function(resp){
                        if (resp) {
                            if (resp.result < 0) {
                                $scope.invalidMessage.$infoForm = appConfig.alertMsg.user.error[resp.result.toString()];
                            } else{
                                alertService.success('密码修改成功');
                                $scope.dismiss();
                            }
                        } else {
                            $scope.invalidMessage.$infoForm = '用户不存在。';
                        }
                    });
                };
            }
        ]);

})(window, window.angular);
(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.EditPasswordController', [
            '$scope',
            'userName',
            '$timeout',
            '$modalInstance',
            'userService',
            'alertService',
            function ($scope, userName, $timeout, $modalInstance, userService, alertService) {

                $scope.userName = userName;
                $scope.invalidMessage = {};
                $scope.formTitle = '修改密码';

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
                        name: userName,
                        password: $scope.user.password
                    }, $scope.user.newPwd1).then(function(resp){
                        if (resp.result === 1) {
                            alertService.success(resp.msg);
                            $scope.dismiss();
                        } else {
                            $scope.invalidMessage.$infoForm = resp.errorMsg;
                        }
                    });
                };
            }
        ]);

})(window, window.angular);
(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.SettingController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                mockerService.getMockerById($scope.mockerId).then(function(resp){
                    $scope.mocker = resp;
                    $scope.mocker.newName = $scope.mocker.name;
                });

                $scope.invalidMessage = {};
                $scope.$watch('mocker.newName', function () {
                    $scope.invalidMessage.name = '';
                });
                $scope.updateName = function () {
                    if ($scope.mocker.newName === $scope.mocker.name) {
                        return;
                    }
                    var oldName = $scope.mocker.name;
                    $scope.mocker.name = $scope.mocker.newName;
                    mockerService.updateMocker($scope.mocker, 'ChangeMockerName').then(function(resp){
                        if (resp.result === -1) {
                            $scope.invalidMessage.name = '相同名字的模拟系统已经存在！';
                            $scope.mocker.name = oldName;
                        } else {
                            alertService.success('模拟系统名称更新成功。');
                            $scope.postMockerChanged({mocker: resp});
                        }
                    });
                };

                $scope.convertType = function (mtype) {
                    $scope.mocker.type = mtype;
                    mockerService.updateMocker($scope.mocker, 'ChangeMockerType').then(function(resp){
                        if (resp) {
                            alertService.success('模拟系统更改类型成功。');
                            $scope.postMockerChanged({mocker: resp});
                        }
                    });
                };

                $scope.deleteMocker = function () {
                    mockerService.deleteMocker($scope.mocker.id).then(function(){
                        alertService.success('模拟系统删除成功。');
                        $scope.postMockerDeleted();
                    });
                };
            }
        ]);

})(window, window.angular);
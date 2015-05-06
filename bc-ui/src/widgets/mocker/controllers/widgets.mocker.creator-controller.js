(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.CreatorController', [
            '$scope',
            'mockerService',
            'alertService',
            function ($scope, mockerService, alertService) {

                $scope.mocker = {
                    type: 'Private',
                    desc: ''
                };
                $scope.invalidMessage = {};

                $scope.$watch('mocker.name', function () {
                    $scope.invalidMessage.name = '';
                });

                $scope.save = function () {
                    mockerService.createMocker($scope.mocker).then(function(resp){
                        if (resp.result === -1) {
                            $scope.invalidMessage.name = '相同名字的模拟系统已经存在！';
                        } else {
                            alertService.success('模拟系统创建成功。');
                            $scope.postMockerCreated({mocker: resp});
                        }
                    })
                };
            }
        ]);

})(window, window.angular);
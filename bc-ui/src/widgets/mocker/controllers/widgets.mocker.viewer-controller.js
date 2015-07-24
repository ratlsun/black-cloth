(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ViewerController', [
            '$scope',
            'mockerService',
            function ($scope, mockerService) {

                $scope.refreshMocker = function(){
                    mockerService.getMockerById($scope.mockerId).then(function(resp){
                        $scope.mocker = resp;
                    });
                };
                $scope.refreshMocker();

            }
        ]);

})(window, window.angular);
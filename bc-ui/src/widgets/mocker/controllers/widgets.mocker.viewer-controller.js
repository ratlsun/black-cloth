(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .controller('widgets.mocker.ViewerController', [
            '$scope',
            'mockerService',
            'userService',
            function ($scope, mockerService, userService) {

                $scope.refreshMocker = function(){
                    mockerService.getMockerById($scope.mockerId).then(function(resp){
                        $scope.mocker = resp;
                        $scope.mode = userService.getCurrentUser().name === $scope.mocker.owner ? 'E' : 'V';
                    });
                };
                $scope.refreshMocker();

            }
        ]);

})(window, window.angular);
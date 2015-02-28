(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .controller('widgets.user.MainController', [
            '$scope',
            'appConfig',
            function ($scope, appConfig) {
                $scope.activeStep = "login";

                $scope.gotoStep = function(step) {
                    $scope.activeStep = step;
                    console.log(step);
                };

                $scope.gotoInfoStep = function() {
                    $scope.gotoStep('info');
                };

                $scope.submitSignInfo = function() {
                    $scope.gotoStep('code');
                };

                $scope.submitSignCode = function() {
                    $scope.gotoStep('done');
                };
            }
        ]);

})(window, window.angular);
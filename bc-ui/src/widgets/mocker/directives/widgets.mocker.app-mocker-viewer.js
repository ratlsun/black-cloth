(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerViewer', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        mockerId: '@'
                    },
                    templateUrl: 'widgets.mocker.viewer.html',
                    controller: 'widgets.mocker.ViewerController',
                    replace: true
                };
            }]);

})(window, window.angular);
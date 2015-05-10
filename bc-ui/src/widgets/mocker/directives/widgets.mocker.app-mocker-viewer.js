(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerViewer', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        mockerId: '@',
                        onRuleClick: '&'
                    },
                    templateUrl: 'widgets.mocker.viewer.html',
                    controller: 'widgets.mocker.ViewerController',
                    replace: true
                };
            }]);

})(window, window.angular);
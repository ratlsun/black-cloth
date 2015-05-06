(function (window, angular) {
    "use strict";

    angular.module('module.widgets.breadcrumb')
        .directive('appBreadcrumb', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        lastItemId: '@'
                    },
                    templateUrl: 'widgets.breadcrumb.main.html',
                    controller: 'widgets.breadcrumb.MainController',
                    replace: true
                };
            }]);

})(window, window.angular);
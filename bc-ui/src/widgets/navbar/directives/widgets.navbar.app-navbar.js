(function (window, angular) {
    "use strict";

    angular.module('module.widgets.navbar')
        .directive('appNavbar', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.navbar.main.html',
                    controller: 'widgets.navbar.MainController',
                    replace: true
                };
            }]);

})(window, window.angular);
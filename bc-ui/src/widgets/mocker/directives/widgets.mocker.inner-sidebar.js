(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('innerSidebar', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.mocker.sidebar.html',
                    replace: true
                };
            }]);

})(window, window.angular);
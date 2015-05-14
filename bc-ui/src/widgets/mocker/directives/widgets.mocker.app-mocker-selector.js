(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerSelector', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onItemClick: '&'
                    },
                    templateUrl: 'widgets.mocker.selector.html',
                    controller: 'widgets.mocker.SelectorController',
                    replace: true
                };
            }]);

})(window, window.angular);
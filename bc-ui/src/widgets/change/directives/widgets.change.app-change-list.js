(function (window, angular) {
    "use strict";

    angular.module('module.widgets.change')
        .directive('appChangeList', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onMockerClick: '&'
                    },
                    templateUrl: 'widgets.change.list.html',
                    controller: 'widgets.change.ListController',
                    replace: true
                };
            }]);

})(window, window.angular);
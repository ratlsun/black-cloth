(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerList', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onItemClick: '&',
                        mockerGroup: '@'
                    },
                    templateUrl: 'widgets.mocker.list.html',
                    controller: 'widgets.mocker.ListController',
                    replace: true
                };
            }]);

})(window, window.angular);
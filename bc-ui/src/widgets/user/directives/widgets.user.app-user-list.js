(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('appUserList', [
            function () {
                return {
                    restrict: 'E',
                    scope: {},
                    templateUrl: 'widgets.user.list.html',
                    controller: 'widgets.user.ListController',
                    replace: true
                };
            }]);

})(window, window.angular);
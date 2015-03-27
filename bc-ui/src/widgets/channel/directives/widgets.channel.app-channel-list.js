(function (window, angular) {
    "use strict";

    angular.module('module.widgets.channel')
        .directive('appChannelList', [
            function () {
                return {
                    restrict: 'E',
                    scope: {},
                    templateUrl: 'widgets.channel.list.html',
                    controller: 'widgets.channel.ListController',
                    replace: true
                };
            }]);

})(window, window.angular);
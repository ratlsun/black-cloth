(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .directive('appRuleList', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onRuleClick: '&',
                        postRuleDeleted: '&',
                        mockerId: '@',
                        viewMode: '@'
                    },
                    templateUrl: 'widgets.rule.list.html',
                    controller: 'widgets.rule.ListController',
                    replace: true
                };
            }]);

})(window, window.angular);
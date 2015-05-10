(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .directive('appRuleEditor', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        postRuleSaved: '&',
                        mockerId: '@',
                        ruleId: '@',
                        editMode: '@'
                    },
                    templateUrl: 'widgets.rule.editor.html',
                    controller: 'widgets.rule.EditorController',
                    replace: true
                };
            }]);

})(window, window.angular);
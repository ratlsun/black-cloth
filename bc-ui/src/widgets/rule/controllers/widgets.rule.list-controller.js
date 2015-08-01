(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .controller('widgets.rule.ListController', [
            '$scope',
            'ruleService',
            'alertService',
            function ($scope, ruleService, alertService) {

                var refreshRules = function () {
                    ruleService.getRulesByMockerId($scope.mockerId).then(function(resp){
                        $scope.rules = resp;
                    });
                };

                refreshRules();

                $scope.disableRule = function (rule) {
                    //@TODO
                };

                $scope.deleteRule = function (rule) {
                    ruleService.deleteRule(rule.id).then(function(){
                        alertService.success('规则删除成功。');
                        refreshRules();
                        $scope.postRuleDeleted();
                    });
                };
            }
        ]);

})(window, window.angular);
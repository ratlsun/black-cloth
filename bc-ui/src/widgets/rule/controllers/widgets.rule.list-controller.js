(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .controller('widgets.rule.ListController', [
            '$scope',
            'ruleService',
            'alertService',
            'mockerService',
            'userService',
            function ($scope, ruleService, alertService, mockerService, userService) {
                $scope.user = userService.getCurrentUser();
                mockerService.getMockerById($scope.mockerId).then(function(resp) {
                    $scope.mocker = resp;
                    $scope.isEdit = ($scope.user.name == $scope.mocker.owner) ? true : false;
                });

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
                    });
                };
            }
        ]);

})(window, window.angular);
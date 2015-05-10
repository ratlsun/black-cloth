(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .controller('widgets.rule.EditorController', [
            '$scope',
            'ruleService',
            'alertService',
            function ($scope, ruleService, alertService) {

                if ($scope.ruleId){
                    ruleService.getRuleById($scope.ruleId).then(function(resp){
                        $scope.rule = resp;
                    });
                } else {
                    $scope.rule = {
                        mockerId: $scope.mockerId,
                        request: {
                            header: {
                                url: '',
                                method: 'GET',
                                contentType: '*'
                            },
                            body: {
                                type: 'Text',
                                content: ''
                            }
                        },
                        response: {
                            header: {
                                statusCode: 200,
                                contentType: 'application/json'
                            },
                            body: {
                                content: ''
                            }
                        }
                    };
                }

                $scope.invalidMessage = {};

                $scope.save = function () {
                    if ($scope.rule.request.header.method !== 'POST' && $scope.rule.request.header.method !== 'PUT') {
                        $scope.rule.request.body.content = '';
                    }

                    if ($scope.editMode === 'C') {
                        ruleService.createRule($scope.rule).then(function(resp){
                            alertService.success('添加规则成功。');
                            $scope.postRuleSaved({rule: resp});
                        });
                    } else {
                        ruleService.updateRule($scope.rule).then(function(resp){
                            alertService.success('修改规则成功。');
                            $scope.postRuleSaved({rule: resp});
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
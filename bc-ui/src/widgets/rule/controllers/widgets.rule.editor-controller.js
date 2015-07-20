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
                                contentType: 'application/json',
                                headers: {}
                            },
                            body: {
                                content: ''
                            }
                        }
                    };
                }

                $scope.newHeader = {
                    key: '',
                    value: ''
                };

                $scope.invalidMessage = {};

                $scope.jsonBodyEditorOptions = {
                    mode: {name: 'javascript', json: true}
                };

                $scope.xmlBodyEditorOptions = {
                    mode: 'xml'
                };

                $scope.getHeaderSize = function(hs){
                    return _.size(hs);
                };

                $scope.save = function () {
                    $scope.addHeader();

                    if ($scope.rule.request.header.method !== 'POST' && $scope.rule.request.header.method !== 'PUT') {
                        $scope.rule.request.body.content = '';
                        $scope.rule.request.body.type = 'None';
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

                $scope.addHeader = function () {
                    if ($scope.newHeader.key.length > 0 && $scope.newHeader.value.length > 0) {
                        if (!$scope.rule.response.header.headers) {
                            $scope.rule.response.header.headers = {};
                        }
                        _.set($scope.rule.response.header.headers,
                            $scope.newHeader.key, $scope.newHeader.value);

                        $scope.newHeader.key = '';
                        $scope.newHeader.value = '';
                    }
                };

                $scope.removeHeader = function (hkey) {
                    delete $scope.rule.response.header.headers[hkey];
                };
            }
        ]);

})(window, window.angular);
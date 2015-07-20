(function (window, angular) {
    "use strict";

    angular.module('module.widgets.rule')
        .controller('widgets.rule.EditorController', [
            '$scope',
            'ruleService',
            'alertService',
            function ($scope, ruleService, alertService) {

                $scope.headers = [];

                if ($scope.ruleId){
                    ruleService.getRuleById($scope.ruleId).then(function(resp){
                        $scope.rule = resp;
                        _.forEach($scope.rule.response.header.headers, function(n, key){
                            $scope.headers.push({
                                key: key,
                                value: $scope.rule.response.header.headers[key]
                            });
                        });
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

                $scope.save = function () {
                    $scope.addHeader();
                    var hds = {};
                    _.forEach($scope.headers, function(o) {
                        hds[o.key] = o.value;
                    });
                    $scope.rule.response.header.headers = hds;

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
                        var i = _.findIndex($scope.headers, 'key', $scope.newHeader.key);
                        var nh = _.clone($scope.newHeader, true);
                        if (i > -1) {
                            $scope.headers[i] = nh;
                        } else {
                            $scope.headers.push(nh);
                        }

                        $scope.newHeader.key = '';
                        $scope.newHeader.value = '';
                    }
                };

                $scope.removeHeader = function (index) {
                    $scope.headers.splice(index, 1);
                };
            }
        ]);

})(window, window.angular);
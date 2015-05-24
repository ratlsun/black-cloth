(function (window, angular) {
    "use strict";

    angular.module('module.widgets.guide')
        .directive('appNoRuleGuide', [
            'ruleService',
            function (ruleService) {
                return {
                    restrict: 'A',
                    scope: {
                        mockerId: "@",
                        enableGuide: "@"
                    },
                    link: function(scope, element) {

                        if (scope.enableGuide === 'true') {
                            scope.$watch('mockerId', function(nv){
                                if (nv){
                                    ruleService.getRulesByMockerId(nv).then(function(resp) {
                                        if (!resp || resp.length === 0) {
                                            $(element).qtip({
                                                content: {
                                                    title: '欢迎使用模客',
                                                    text: '<h4>这个模拟系统里还没有规则，创建一个吧。</h4>',
                                                    button: true
                                                },
                                                position: {
                                                    my: 'top left',
                                                    at: 'bottom right',
                                                    target: element
                                                },
                                                show:{
                                                    ready: true,
                                                    event: '',
                                                    modal: {
                                                        on: true,
                                                        blur: false
                                                    }
                                                },
                                                hide: {
                                                    event: false,
                                                    delay : 100
                                                },
                                                style: {
                                                    classes: 'qtip-tipped'
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    }
                };
            }]);

})(window, window.angular);
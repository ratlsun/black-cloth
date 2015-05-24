(function (window, angular) {
    "use strict";

    angular.module('module.widgets.guide')
        .directive('appNeverMockGuide', [
            'ruleService',
            'mockActivityService',
            function (ruleService, mockActivityService) {
                return {
                    restrict: 'A',
                    scope: {
                        mockerId: "@",
                        enableGuide: "@"
                    },
                    link: function(scope, element) {
                        if (scope.enableGuide === 'true') {
                            mockActivityService.getOneHistoryMockActivity().then(function(mockResp) {
                                if (!mockResp){
                                    scope.$watch('mockerId', function(nv){
                                        if (nv){
                                            ruleService.getRulesByMockerId(nv).then(function(resp) {
                                                if (resp && resp.length > 0) {
                                                    $(element).qtip({
                                                        content: {
                                                            title: '欢迎使用模客',
                                                            text: '<h4>你已经创建了模拟系统和规则，点击这里开始模拟。</h4>',
                                                            button: true
                                                        },
                                                        position: {
                                                            my: 'top left',
                                                            at: 'bottom center',
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

                            });
                        }
                    }
                };
            }]);

})(window, window.angular);
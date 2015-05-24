(function (window, angular) {
    "use strict";

    angular.module('module.widgets.guide')
        .directive('appNoMockerGuide', [
            'mockerService',
            function (mockerService) {
                return {
                    restrict: 'A',
                    scope: {
                        enableGuide: "@"
                    },
                    link: function(scope, element) {
                        if (scope.enableGuide === 'true') {
                            mockerService.getMyMockers().then(function(resp) {
                                if (!resp || resp.length === 0) {
                                    $(element).qtip({
                                        content: {
                                            title: '欢迎使用模客',
                                            text: '<h4>还没有要模拟的系统，创建一个吧。</h4>',
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
                    }
                };
            }]);

})(window, window.angular);
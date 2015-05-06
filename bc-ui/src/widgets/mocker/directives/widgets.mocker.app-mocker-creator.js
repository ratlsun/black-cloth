(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerCreator', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        postMockerCreated: '&'
                    },
                    templateUrl: 'widgets.mocker.creator.html',
                    controller: 'widgets.mocker.CreatorController',
                    replace: true
                };
            }]);

})(window, window.angular);
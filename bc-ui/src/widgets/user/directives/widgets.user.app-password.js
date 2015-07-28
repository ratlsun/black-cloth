(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('appPassword', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        pwdCode: '@',
                        onGoBack: '&'
                    },
                    templateUrl: 'widgets.user.reset-password.html',
                    controller: 'widgets.user.ResetPasswordController',
                    replace: true
                };
            }]);

})(window, window.angular);
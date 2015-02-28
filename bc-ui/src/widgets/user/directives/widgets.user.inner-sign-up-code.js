(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('innerSignUpCode', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.user.sign-up-code.html',
                    replace: true
                };
            }]);

})(window, window.angular);
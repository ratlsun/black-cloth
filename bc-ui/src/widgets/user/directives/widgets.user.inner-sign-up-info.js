(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('innerSignUpInfo', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.user.sign-up-info.html',
                    replace: true
                };
            }]);

})(window, window.angular);
(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('innerSignUpDone', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.user.sign-up-done.html',
                    replace: true
                };
            }]);

})(window, window.angular);
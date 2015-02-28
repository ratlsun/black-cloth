(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('appSignInAndSignUpWizard', [
            function () {
                return {
                    restrict: 'E',
                    templateUrl: 'widgets.user.main.html',
                    controller: 'widgets.user.MainController',
                    replace: true
                };
            }]);

})(window, window.angular);
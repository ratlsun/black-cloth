(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('appSignUpWizard', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onGoBack: '&'
                    },
                    templateUrl: 'widgets.user.sign-up.html',
                    controller: 'widgets.user.SignUpController',
                    replace: true
                };
            }]);

})(window, window.angular);
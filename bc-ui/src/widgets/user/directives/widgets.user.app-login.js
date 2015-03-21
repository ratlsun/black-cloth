(function (window, angular) {
    "use strict";

    angular.module('module.widgets.user')
        .directive('appLogin', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onSignUp: '&',
                        postSignIn: '&'
                    },
                    templateUrl: 'widgets.user.login.html',
                    controller: 'widgets.user.LoginController',
                    replace: true
                };
            }]);

})(window, window.angular);
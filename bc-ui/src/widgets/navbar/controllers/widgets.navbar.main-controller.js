(function (window, angular) {
    "use strict";

    angular.module('module.widgets.navbar')
        .controller('widgets.navbar.MainController', [
            '$scope',
            '$state',
            'appConfig',
            'userService',
            function ($scope, $state, appConfig, userService) {

                $scope.activeMenu = $state.current.name;
                $scope.currentUser = userService.getCurrentUser();

                $scope.gotoHome = function () {
                    //$state.go(appConfig.stateName.home);
                    userService.getUserByName('ppp');
                };

                $scope.gotoMockerDashboard = function () {
                    $state.go(appConfig.stateName.mockerDashboard);
                };

                $scope.gotoMockerCreator = function () {
                    $state.go(appConfig.stateName.mockerCreator);
                };

                $scope.gotoLogin = function () {
                    $state.go(appConfig.stateName.login);
                };

                $scope.gotoRegister = function () {
                    $state.go(appConfig.stateName.register);
                };

                $scope.gotoUserAdmin = function () {
                    $state.go(appConfig.stateName.userAdmin);
                };

                $scope.gotoChannelAdmin = function () {
                    $state.go(appConfig.stateName.channelAdmin);
                };

                $scope.logout = function () {
                    userService.logout().then(function (){
                        userService.setCurrentUser(null);
                        $state.go(appConfig.stateName.login);
                    })
                };
            }
        ]);

})(window, window.angular);
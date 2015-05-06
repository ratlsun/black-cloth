(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerSetting', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        mockerId: '@',
                        postMockerChanged: '&',
                        postMockerDeleted: '&'
                    },
                    templateUrl: 'widgets.mocker.setting.html',
                    controller: 'widgets.mocker.SettingController',
                    replace: true
                };
            }]);

})(window, window.angular);
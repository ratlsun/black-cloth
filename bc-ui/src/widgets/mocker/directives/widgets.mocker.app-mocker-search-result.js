(function (window, angular) {
    "use strict";

    angular.module('module.widgets.mocker')
        .directive('appMockerSearchResult', [
            function () {
                return {
                    restrict: 'E',
                    scope: {
                        onMockerClick: '&'
                    },
                    templateUrl: 'widgets.mocker.search-result.html',
                    controller: 'widgets.mocker.SearchResultController',
                    replace: true
                };
            }]);

})(window, window.angular);
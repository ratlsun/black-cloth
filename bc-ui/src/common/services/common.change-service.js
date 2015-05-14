(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('changeService', [
            'Restangular',
            function (Restangular) {
                return {

                    getMyChanges: function () {
                        return Restangular.all('changes').getList();
                    }

                };
            }]);

})(window, window.angular);
(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('mockHitService', [
            'Restangular',
            function (Restangular) {
                return {

                    clearLog: function (acode) {
                        return Restangular.one('mock-hits', acode).remove();
                    }

                };
            }]);

})(window, window.angular);
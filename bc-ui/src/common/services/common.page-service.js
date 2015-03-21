(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('pageService', [
            'usSpinnerService',
            function (usSpinnerService) {
                return {

                    mask: function (key) {
                        usSpinnerService.spin(key);
                    },

                    unmask: function (key) {
                        usSpinnerService.stop(key);
                    }

                };
            }]);

})(window, window.angular);
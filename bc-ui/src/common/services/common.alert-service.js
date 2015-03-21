(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('alertService', [
            'growl',
            function (growl) {
                return {

                    error: function (message) {
                        growl.error(message);
                    },

                    warning: function (message) {
                        growl.warning(message);
                    },

                    info: function (message) {
                        growl.info(message);
                    },

                    success: function (message) {
                        growl.success(message);
                    }

                };
            }]);

})(window, window.angular);
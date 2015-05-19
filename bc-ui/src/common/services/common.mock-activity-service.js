(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('mockActivityService', [
            'Restangular',
            function (Restangular) {
                return {

                    startMockActivity: function (activity) {
                        return Restangular.all('mock-activity').post(activity);
                    },

                    pauseMockActivity: function (activity) {
                        return Restangular.one('mock-activity', activity.code).customPUT(null, 'pause');
                    },

                    resumeMockActivity: function (activity) {
                        return Restangular.one('mock-activity', activity.code).customPUT(activity, 'resume');
                    },

                    stopMockActivity: function (activity) {
                        return Restangular.one('mock-activity', activity.code).customPUT(null, 'stop');
                    },

                    getMyMockActivity: function () {
                        return Restangular.one('mock-activity', 'active').get();
                    }

                };
            }]);

})(window, window.angular);
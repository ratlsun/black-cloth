(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('mockActivityService', [
            'Restangular',
            function (Restangular) {
                return {

                    startMockActivity: function (activity) {
                        return Restangular.all('mock-activities').post(activity);
                    },

                    pauseMockActivity: function (activity) {
                        return Restangular.one('mock-activities', activity.code).customPUT(null, 'pause');
                    },

                    resumeMockActivity: function (activity) {
                        return Restangular.one('mock-activities', activity.code).customPUT(activity, 'resume');
                    },

                    stopMockActivity: function (activity) {
                        return Restangular.one('mock-activities', activity.code).customPUT(null, 'stop');
                    },

                    getMyMockActivity: function () {
                        return Restangular.one('mock-activities', 'active').get();
                    },

                    getMockHitByActivity: function (activity) {
                        return Restangular.all('mock-hits').getList({acode: activity.code});
                    }

                };
            }]);

})(window, window.angular);
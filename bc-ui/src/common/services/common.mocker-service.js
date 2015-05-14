(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('mockerService', [
            'Restangular',
            function (Restangular) {
                return {

                    getMyMockers: function () {
                        return Restangular.all('mockers').getList();
                    },

                    getMockerById: function (mid) {
                        return Restangular.one('mockers', mid).get();
                    },

                    createMocker: function (mocker) {
                        return Restangular.all('mockers').post(mocker);
                    },

                    updateMocker: function (mocker, operation) {
                        return Restangular.one('mockers', mocker.id).customPUT(mocker, '', {op: operation});
                    },

                    deleteMocker: function (mockerId) {
                        return Restangular.one('mockers', mockerId).remove();
                    }
                };
            }]);

})(window, window.angular);
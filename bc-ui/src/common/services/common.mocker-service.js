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

                    getByPublic: function () {
                        return Restangular.all('mockers').customGET('public');
                    },

                    getCollect: function () {
                        return Restangular.all('mockers').customGET('collect');
                    },

                    createMocker: function (mocker) {
                        return Restangular.all('mockers').post(mocker);
                    },

                    updateMocker: function (mocker, operation) {
                        return Restangular.one('mockers', mocker.id).customPUT(mocker, '', {op: operation});
                    },

                    deleteMocker: function (mockerId) {
                        return Restangular.one('mockers', mockerId).remove();
                    },

                    collectMocker: function (id, operation) {
                        return Restangular.one('mockers', id).customPUT(null, 'collect', {op: operation});
                    },

                    cancelCollectMocker: function (id, operation) {
                        return Restangular.one('mockers', id).customPUT(null, 'cancelCollect', {op: operation});
                    }
                };
            }]);

})(window, window.angular);
(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('channelService', [
            'Restangular',
            function (Restangular) {
                return {

                    getAllChannels: function (systems) {
                        if (systems && systems.length > 0){
                            return Restangular.all('channels').getList({sys: systems});
                        }
                        return Restangular.all('channels').getList();
                    },

                    getAllChannelSystems: function () {
                        return Restangular.all('channels').all('systems').getList();
                    },

                    createChannel: function (channel) {
                        return Restangular.all('channels').post(channel);
                    },

                    updateChannel: function (channel) {
                        return Restangular.one('channels', channel.id).customPUT(channel);
                    },

                    deleteChannel: function (channelId) {
                        return Restangular.one('channels', channelId).remove();
                    }
                };
            }]);

})(window, window.angular);
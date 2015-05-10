(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('ruleService', [
            'Restangular',
            function (Restangular) {
                return {

                    getRulesByMockerId: function (mockerId) {
                        return Restangular.all('rules').getList({mid: mockerId});
                    },

                    getRuleById: function (ruleId) {
                        return Restangular.one('rules', ruleId).get();
                    },

                    createRule: function (rule) {
                        return Restangular.all('rules').post(rule);
                    },

                    updateRule: function (rule) {
                        return Restangular.one('rules', rule.id).customPUT(rule);
                    },

                    deleteRule: function (ruleId) {
                        return Restangular.one('rules', ruleId).remove();
                    }
                };
            }]);

})(window, window.angular);
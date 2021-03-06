(function (window, angular) {
    "use strict";

    angular.module('module.common')
        .factory('userService', [
            'Restangular',
            function (Restangular) {
                var currentUser = {};

                return {

                    login: function (user) {
                        return Restangular.all('login').post($.param(user), {},
                            {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'});
                    },

                    logout: function () {
                        return Restangular.all('logout').customGET('');
                    },

                    setCurrentUser: function (user) {
                        if (user && user.name) {
                            currentUser.id = user.id;
                            currentUser.name = user.name;
                            currentUser.status = user.status;
                            currentUser.roles = user.roles;
                            currentUser.isAdmin = _.indexOf(user.roles, 'ADMIN') > -1;
                        } else {
                            currentUser.id = '';
                            currentUser.name = '';
                            currentUser.status = '';
                            currentUser.roles = '';
                            currentUser.isAdmin = false;
                        }
                    },

                    getCurrentUser: function () {
                        return currentUser;
                    },

                    createUser: function (user) {
                        return Restangular.all('users').post(user);
                    },

                    activeUserByCode: function (code) {
                        return Restangular.all('users').customPUT(null, 'active', {code: code});
                    },

                    activeUser: function (uid) {
                        return Restangular.one('users', uid).customPUT(null, 'active');
                    },

                    inactiveUser: function (uid) {
                        return Restangular.one('users', uid).customPUT(null, 'inactive');
                    },

                    getAuthUser: function () {
                        return Restangular.all('users').customGET('auth');
                    },

                    getUserByName: function (name) {
                        return Restangular.one('users', name).get({by: 'name'});
                    },

                    getAllUsers: function () {
                        return Restangular.all('users').getList();
                    },

                    changePassword: function (user) {
                        return Restangular.one('users', user.id).customPUT(user, 'changePwd');
                    },

                    applyResetPassword: function (name) {
                        return Restangular.one('users', name).customPUT(null, 'applyPwd');
                    },

                    checkPasswordCode: function (code) {
                        return Restangular.all('users').customGET('pcode', {
                            by: 'pcode',
                            code: code
                        });
                    },

                    resetPassword: function (user) {
                        return Restangular.one('users', user.id).customPUT(user, 'resetPwd');
                    },

                    sendCodeMail: function (uid) {
                        return Restangular.one('users', uid).customPUT(null, 'sendCode');
                    }

                };
            }]);

})(window, window.angular);

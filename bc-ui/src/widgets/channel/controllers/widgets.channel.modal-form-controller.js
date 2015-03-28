(function (window, angular) {
    "use strict";

    angular.module('module.widgets.channel')
        .controller('widgets.channel.ModalFormController', [
            '$scope',
            'channelData',
            '$timeout',
            '$modalInstance',
            'channelService',
            'pageService',
            'alertService',
            function ($scope, channelData, $timeout, $modalInstance, channelService, pageService, alertService) {

                $scope.channel = channelData;
                $scope.invalidMessage = {};
                $scope.formTitle = '新增通道';
                if ($scope.channel.port) {
                    $scope.channel.port = parseInt($scope.channel.port);
                    $scope.formTitle = '修改通道';
                }

                var validateSystem = function (){
                    var isValid = true;
                    $scope.invalidMessage.system = null;
                    if ($scope.channel.system && !/^[A-Z]$/.test($scope.channel.system)) {
                        $scope.invalidMessage.system = '所属系统名称必须是大写的单个字母！';
                        isValid = false;
                    }
                    return isValid;
                };

                var validateIp = function (){
                    var isValid = true;
                    $scope.invalidMessage.ip = null;
                    if ($scope.channel.ip && !/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test($scope.channel.ip)) {
                        $scope.invalidMessage.ip = 'IP地址不合法！';
                        isValid = false;
                    }
                    return isValid;
                };

                $scope.$watch('channel.system', function () {
                    validateSystem();
                });

                $scope.$watch('channel.ip', function () {
                    validateIp();
                });

                $scope.dismiss = function(){
                    $modalInstance.dismiss();
                };

                $scope.save = function(){
                    if (!validateSystem() || !validateIp()) {
                        return;
                    }
                    if ($scope.channel.id) {
                        channelService.updateChannel($scope.channel).then(function(resp){
                            if (resp.result === -1) {
                                $scope.invalidMessage.name = '修改后的通道名称与已有通道冲突！';
                            } else {
                                alertService.success('通道修改已保存。');
                                $modalInstance.close($scope.channel);
                            }
                        });
                    } else {
                        channelService.createChannel($scope.channel).then(function(resp){
                            if (resp.result === -1) {
                                $scope.invalidMessage.name = '该通道已经在使用中，不需要重复建立！';
                            } else {
                                alertService.success('通道创建成功。');
                                $modalInstance.close($scope.channel);
                            }
                        });
                    }
                };
            }
        ]);

})(window, window.angular);
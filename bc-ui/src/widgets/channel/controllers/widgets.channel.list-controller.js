(function (window, angular) {
    "use strict";

    angular.module('module.widgets.channel')
        .controller('widgets.channel.ListController', [
            '$scope',
            '$modal',
            '$timeout',
            'channelService',
            'pageService',
            'alertService',
            function ($scope, $modal, $timeout, channelService, pageService, alertService) {

                $scope.channels = [];
                $scope.channelSystems = [];
                $scope.refreshData = function(init){
                    pageService.mask('channel-list-spinner');
                    channelService.getAllChannelSystems().then(function(resp) {
                        resp.sort();
                        $scope.channelSystems = resp;
                        if (init || $scope.isShowAll) {
                            $scope.channelSystemsShower = resp;
                            $scope.isShowAll = true;
                        }
                        channelService.getAllChannels($scope.channelSystemsShower.join('')).then(function(cResp) {
                            $scope.channels = cResp;
                            pageService.unmask('channel-list-spinner');
                        });
                    });
                };
                $scope.refreshData(true);

                $scope.showAll = function(){
                    $scope.refreshData(true);
                };

                $scope.showSystem = function(system){
                    if ($scope.isShowAll) {
                        $scope.channelSystemsShower = [];
                        $scope.channelSystemsShower.push(system);
                        $scope.isShowAll = false;
                    } else {
                        var i = _.indexOf($scope.channelSystemsShower, system);
                        if (i > -1){
                            _.pullAt($scope.channelSystemsShower, i);
                            if ($scope.channelSystemsShower.length === 0) {
                                $scope.isShowAll = true;
                            }
                        } else {
                            $scope.channelSystemsShower.push(system);
                            $scope.channelSystemsShower.sort();
                        }
                    }
                    $scope.refreshData($scope.isShowAll);
                };

                $scope.isShowSystem = function(system){
                    if ($scope.isShowAll) {
                        return false;
                    }
                    return _.indexOf($scope.channelSystemsShower, system) > -1;
                };

                $scope.openForm = function(channel){
                    var form = $modal.open({
                        templateUrl: 'widgets.channel.modal-form.html',
                        controller: 'widgets.channel.ModalFormController',
                        resolve: {
                            channelData: function () {
                                return channel ? _.assign({}, channel) : {};
                            }
                        },
                        backdrop: false,
                        windowClass: 'browse-origin-modal'
                    });

                    form.result.then(function(newChannel) {
                        if (newChannel) {
                            $scope.refreshData();
                        }
                    });
                };

                $scope.delete = function(channel){
                    channelService.deleteChannel(channel.id).then(function(){
                        alertService.success('通道删除成功。');
                        $scope.refreshData();
                    });
                };

            }
        ]);

})(window, window.angular);
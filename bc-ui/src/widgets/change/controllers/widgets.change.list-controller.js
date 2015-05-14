(function (window, angular) {
    "use strict";

    angular.module('module.widgets.change')
        .controller('widgets.change.ListController', [
            '$scope',
            'changeService',
            'appConfig',
            function ($scope, changeService, appConfig) {

                changeService.getMyChanges().then(function(resp){
                    var changes = [];
                    var dateLine = null;
                    _.forEach(resp, function(change) {
                        change.icon = appConfig.changeType[change.type].icon;
                        change.theme = appConfig.changeType[change.type].theme;
                        if (change.ruleAddedCount + change.ruleDeletedCount + change.ruleModifiedCount > 0){
                            change.hasRules = true;
                        }
                        var date = new Date(change.endChanged);
                        if (!(dateLine && date.setHours(0, 0, 0, 0) === dateLine.setHours(0, 0, 0, 0))){
                            changes.push({
                                dateLine: change.endChanged
                            });
                            dateLine = date;
                        }
                        changes.push(change);
                    });

                    $scope.changes = changes;
                });

            }
        ]);

})(window, window.angular);
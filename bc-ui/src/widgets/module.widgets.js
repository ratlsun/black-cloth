(function (window, angular) {
  "use strict";

    angular.module('module.widgets', [
        'module.widgets.navbar',
        'module.widgets.breadcrumb',
        'module.widgets.user',
        'module.widgets.channel',
        'module.widgets.mocker',
        'module.widgets.rule',
        'module.widgets.change'
    ]);

})(window, window.angular);
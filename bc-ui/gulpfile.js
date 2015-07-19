var _ = require('lodash');
var path = require('path');
var del = require('del');
var streamqueue = require('streamqueue');

var gulp = require('gulp');
var concat = require('gulp-concat');
var gutil = require('gulp-util');  //for log

var templateCache = require('gulp-angular-templatecache');
var less = require('gulp-less');

//var eslint = require('gulp-eslint');
var ngAnnotate = require('gulp-ng-annotate');
var uglify = require('gulp-uglify');
var minifyHtml = require("gulp-minify-html");

//var deployPath = 'assets';
var distBasePath = '../bc-ss/src/main/resources/static';
var distPaths = {
    basePath: distBasePath,
    jsPath: path.join(distBasePath, 'js'),
    cssPath: path.join(distBasePath, 'css'),
    fontPath: path.join(distBasePath, 'fonts')
};

var srcPath = 'src';
var lessPath = 'less';
var libPath = 'bower_components';

gulp.task('dev', ['dev:templates', 'dev:libjs', 'dev:appjs', 'libcss', 'appcss', 'fonts'], function () {

    gulp.watch(path.join(srcPath, '/**/*.js'), ['dev:appjs']);

    gulp.watch(path.join(srcPath, '/**/*.html'), ['dev:appjs']);

    gulp.watch(path.join(lessPath, '/**/*.less'), ['appcss']);
});

/*=========================== CLEAN ===========================*/
//clean 任务单独执行，一般用不到
gulp.task('dev:clean', function (cb) {
    del([distPaths.basePath], cb);
});


/*=========================== JS & HTML ===========================*/
//将依赖的第三方js库合并成lib.js后放入dist目录
gulp.task('dev:libjs', function () {
    var jsLib = _.map([
        'jquery/dist/jquery.js',
        'lodash/lodash.js',
        'spin.js/spin.js',
        'qtip2/jquery.qtip.js',
        'codemirror/lib/codemirror.js',
        'codemirror/mode/javascript/javascript.js',
        'codemirror/mode/xml/xml.js',

        //NG-libs
        'angular/angular.js',
        'angular-bootstrap/ui-bootstrap-tpls.js',
        'angular-ui-router/release/angular-ui-router.js',
        'restangular/dist/restangular.js',
        'angular-growl-v2/build/angular-growl.js',
        'angular-spinner/angular-spinner.js',
        'angular-ui-codemirror/ui-codemirror.js'
    ], function (sPath) {
        return path.join(libPath, sPath);
    });

    return gulp.src(jsLib)
        .pipe(concat('lib.js'))
        .pipe(gulp.dest(distPaths.jsPath));
});
gulp.task('prod:libjs', function () {
    var jsLib = _.map([
        'jquery/dist/jquery.js',
        'lodash/lodash.js',
        'spin.js/spin.js',
        'qtip2/jquery.qtip.js',
        'codemirror/lib/codemirror.js',
        'codemirror/mode/javascript/javascript.js',
        'codemirror/mode/xml/xml.js'
    ], function(sPath) {
        return path.join(libPath, sPath);
    });

    var ngJsLib = _.map([
        'angular/angular.js',
        'angular-bootstrap/ui-bootstrap-tpls.js',
        'angular-ui-router/release/angular-ui-router.js',
        'restangular/dist/restangular.js',
        'angular-growl-v2/build/angular-growl.js',
        'angular-spinner/angular-spinner.js',
        'angular-ui-codemirror/ui-codemirror.js'
    ], function(sPath) {
        return path.join(libPath, sPath);
    });

    var stream = streamqueue({ objectMode: true });
    stream.queue(gulp.src(jsLib));
    stream.queue(gulp.src(ngJsLib).pipe(ngAnnotate()));

    return stream.done()
        .pipe(uglify())
        .pipe(concat('lib.js'))
        .pipe(gulp.dest(distPaths.jsPath));

});

//将angular的所有template html转成js并且合并后放到dist目录下
gulp.task('dev:templates', function () {
    gulp.src('index.html')
        .pipe(gulp.dest(distPaths.basePath));

    return gulp.src(path.join(srcPath, '/**/*.html'))
        .pipe(templateCache({
            module: 'module.templates',
            standalone: true,
            base: function (templateFile) {
                return path.basename(templateFile.path);
            },
            filename: 'app.tmpl'
        }))
        .pipe(gulp.dest(distPaths.jsPath));
});
gulp.task('prod:templates', function() {
    gulp.src('index.html')
        .pipe(gulp.dest(distPaths.basePath));

    return gulp.src(path.join(srcPath, '/**/*.html'))
        .pipe(minifyHtml({
            empty: true,
            spare: true,
            quotes: true
        }))
        .pipe(templateCache({
            module: 'module.templates',
            standalone: true,
            base: function (templateFile) {
                return path.basename(templateFile.path);
            },
            filename: 'app.tmpl'
        }))
        .pipe(gulp.dest(distPaths.jsPath));
});

//将app自己的js文件合并成app.js后放入dist目录
gulp.task('dev:appjs', ['dev:templates'], function () {
    var files = [
        path.join(srcPath, '/**/*.js'),
        path.join(distPaths.jsPath, '*.tmpl')
    ];

    return gulp.src(files)
        .pipe(concat('app.js'))
        .pipe(gulp.dest(distPaths.jsPath))
});
gulp.task('prod:appjs', ['prod:templates'], function () {
    var files = [
        path.join(srcPath, '/**/*.js'),
        path.join(distPaths.jsPath, '*.tmpl')
    ];

    return gulp.src(files)
        .pipe(uglify())
        .pipe(concat('app.js'))
        .pipe(gulp.dest(distPaths.jsPath))
});


/*=========================== CSS & FONTS ===========================*/
//将less文件编译成css文件放入dist目录下
gulp.task('appcss', function () {
    return gulp.src(path.join(lessPath, 'app.less'))
        //.pipe(sourcemaps.init())
        .pipe(less())
        //.pipe(sourcemaps.write())
        .pipe(gulp.dest(distPaths.cssPath))
        .on('error', gutil.log);
});

//将依赖的第三方css文件放入dist目录下
gulp.task('libcss', function () {
    var cssLib = _.map([
        'bootstrap/dist/css/bootstrap.min.css',
        'font-awesome/css/font-awesome.min.css',
        'angular-growl-v2/build/angular-growl.min.css',
        'qtip2/jquery.qtip.min.css',
        'codemirror/lib/codemirror.css'
    ], function (sPath) {
        return path.join(libPath, sPath);
    });

    return gulp.src(cssLib)
        .pipe(gulp.dest(distPaths.cssPath));
});

//将依赖的第三方字体文件放入dist目录下
gulp.task('fonts', function () {
    return gulp.src(path.join(libPath, 'font-awesome/fonts/*'))
        .pipe(gulp.dest(distPaths.fontPath));
});


/*=========================== Production Deployment ===========================*/
gulp.task('prod', ['prod:templates', 'prod:libjs', 'prod:appjs', 'libcss', 'appcss', 'fonts'], function () {

});
//gulp.task('prod:clean', function () {
//    return del([deployPath]);
//});
//
//gulp.task('prod:deploy', ['dev:templates', 'dev:libjs', 'dev:appjs', 'prod:clean'], function () {
//    gulp.src('index.html')
//        .pipe(gulp.dest(deployPath));
//
//    return gulp.src(path.join(distPath, '/**/*'))
//        .pipe(gulp.dest(deployPath))
//});


// Default will run dev
gulp.task('default', ['dev']);
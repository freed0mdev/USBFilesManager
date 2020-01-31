var exec = require('cordova/exec');

exports.selectDirPath = function (path, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "selectDirPath", [path]);
};

exports.copyFile = function (source, destination, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "copyFile", [source, destination]);
};

exports.deleteFile = function (uri, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "deleteFile", [uri]);
};

exports.getFiles = function (dirUri, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "getFiles", [dirUri]);
};



exports.saveFileToUSB = function (fileName, sourceFileURL, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "saveFileToUSB", [fileName, sourceFileURL]);
};

exports.copyFileFromUSB = function (uri, fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "copyFileFromUSB", [uri, fileName]);
};
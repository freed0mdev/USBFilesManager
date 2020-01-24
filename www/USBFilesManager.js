var exec = require('cordova/exec');

exports.saveFileToUSB = function (fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "saveFileToUSB", [fileName]);
};

exports.getBackupsFromUSB = function (uri, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "getBackupsFromUSB", [uri]);
};

exports.copyBackupFromUSB = function (uri, fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "copyBackupFromUSB", [uri, fileName]);
};
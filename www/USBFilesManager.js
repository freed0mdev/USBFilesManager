var exec = require('cordova/exec');

exports.saveFileToUSB = function (path, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "saveFileToUSB", [path]);
};

exports.getBackupsFromUSB = function (uri, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "getBackupsFromUSB", [uri]);
};

exports.moveBackupFromUSB = function (uri, fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "moveBackupFromUSB", [uri, fileName]);
};
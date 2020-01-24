var exec = require('cordova/exec');

exports.saveFileToUSB = function (fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "saveFileToUSB", [fileName]);
};

exports.getFilesFromUSB = function (onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "getFilesFromUSB");
};

exports.getFilesFromUSBByUri = function (uri, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "getFilesFromUSBByUri", [uri]);
};

exports.copyFileFromUSB = function (uri, fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "copyFileFromUSB", [uri, fileName]);
};

exports.deleteFileFromUSB = function (fileName, onSuccess, onFail) {
    exec(onSuccess, onFail, "USBFilesManager", "deleteFileFromUSB", [fileName]);
};
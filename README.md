---
title: USB Files Manager
description: Read/write files on the USB device via SAF.
---

# cordova-plugin-usbfilesmanager

This plugin implements a File API allowing read/write access to files residing on the USB device via SAF.

This plugin defines global `cordova.plugins.USBFilesManager` object.

Although in the global scope, it is not available until after the `this.platform.ready()` event.

    this.platform.ready().then(() => {
        if (cordova.plugins['USBFilesManager']) {
            cordova.plugins['USBFilesManager'].saveFileToUSB(FILE_NAME, res => {}, err => {});
            cordova.plugins['USBFilesManager'].getBackupsFromUSB(res => {}, err => {});
            cordova.plugins['USBFilesManager'].getBackupsFromUSBByUri(URI, res => {}, err => {});
            cordova.plugins['USBFilesManager'].copyBackupFromUSB(URI, FILE_NAME, res => {}, err => {});
        }
    });

## Installation

    ionic cordova plugin add https://github.com/yokodima/USBFilesManager.git

## Supported Platforms

- Android

## Save file to Chosen Directory

Here is an example of a saving file to the USB storage.

```js
    const FILE_NAME = 'example.zip';
    cordova.plugins['USBFilesManager'].saveFileToUSB(FILE_NAME, res => {}, err => {});
```

Returns:
```
    result.put("error", errorCopy);
    result.put("uri", uri);
```
## Get directory files list

Here is an example of a getting list files from chosen directory.

```js
    cordova.plugins['USBFilesManager'].getBackupsFromUSB(res => {}, err => {});
```
Returns:
```
    resultFile.put("isFile", file.isFile());
    resultFile.put("name", file.getName());
    resultFile.put("url", file.getUri());
    resultFile.put("type", file.getType());
    result.put(file.getName(), resultFile);
```
## Get directory files list by Uri

Here is an example of a getting list files from recently chosen directory uri.

```js
    const URI = 'content://com.externalstorage....';
    cordova.plugins['USBFilesManager'].getBackupsFromUSBByUri(URI, res => {}, err => {});
```
Returns:
```
    resultFile.put("isFile", file.isFile());
    resultFile.put("name", file.getName());
    resultFile.put("url", file.getUri());
    resultFile.put("type", file.getType());
    result.put(file.getName(), resultFile);
```
## Copy file from Chosen Directory

Here is an example of a saving file to the USB storage.

```js
    const URI = uri returned from the getBackupsFromUSB method;
    cordova.plugins['USBFilesManager'].copyBackupFromUSB(URI, FILE_NAME, res => {}, err => {});
```
Returns:
```
    result.put("error", error);
    result.put("fileName", fileName);
    result.put("fileUri", fileUri);
    result.put("url", targetPath);
```
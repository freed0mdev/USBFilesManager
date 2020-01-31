package cordova.plugin.usbfilesmanager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import android.support.v4.provider.DocumentFile;
import android.provider.DocumentsContract;
import android.content.Context;
import java.net.URLConnection;
import android.os.AsyncTask;

import java.lang.Exception;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Channels;

public class USBFilesManager extends CordovaPlugin {
    private static final String ACTION_SELECT_DIR_PATH = "selectDirPath";
    private static final String ACTION_COPY_FILE = "copyFile";
    private static final String ACTION_DELETE_FILE = "deleteFile";
    private static final String ACTION_GET_FILES = "getFiles";

    private static final String ACTION_SAVE_FILE_TO_USB = "saveFileToUSB";
    private static final String ACTION_COPY_FILE_FROM_USB = "copyFileFromUSB";
    private static final String inputFileName = null;
    private static final CallbackContext callback;
    private static final int PICK_DIR_REQUEST = 1;
    private static final int PICK_FOLDER_REQUEST_FOR_SAVE = 2;

    @Override
    public boolean execute(
            String action,
            JSONArray args,
            CallbackContext callbackContext
    ) {
        try {
            if (action.equals(USBFilesManager.ACTION_SELECT_DIR_PATH)) {
                this.selectDirPath(callbackContext, args.getString(0));
                return true;
            } else if (action.equals(USBFilesManager.ACTION_COPY_FILE)) {
//                this.copyFile(callbackContext, args.getString(0), args.getString(1));
                return true;
            } else if (action.equals(USBFilesManager.ACTION_DELETE_FILE)) {
                this.deleteFile(callbackContext, args.getString(0));
                return true;
            } else if (action.equals(USBFilesManager.ACTION_GET_FILES)) {
                this.getFilesListByUri(callbackContext, args.getString(0));
                return true;



            } else if (action.equals(USBFilesManager.ACTION_SAVE_FILE_TO_USB)) {
                this.saveFileToTargetDirectory(callbackContext, args.getString(0));
                return true;
            } else if (action.equals(USBFilesManager.ACTION_COPY_FILE_FROM_USB)) {
                this.copyFileFromUSB(callbackContext, args.getString(0), args.getString(1));
                return true;
            }
        } catch (JSONException err) {
            this.callback.error("Execute failed: " + err.toString());
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USBFilesManager.PICK_DIR_REQUEST && this.callback != null) {
            if (resultCode == Activity.RESULT_OK) {
                this.callback.success(data.getData().toString());
            } else {
                this.callback.error("Directory URI was null.");
            }
        }



        else if (requestCode == USBFilesManager.PICK_FOLDER_REQUEST_FOR_SAVE && this.callback != null) {
            if (resultCode == Activity.RESULT_OK) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = new JSONObject();
                            Uri uri = data.getData();



                            GetFileFromURL getFileFromURL = new GetFileFromURL();
                            getFileFromURL.execute(uri);

//                            URL url = new URL("http://54.156.240.184:50420/backups/5e130c0a9f274b377d7005a4/backup-31012020092346");
//                            URLConnection connection = url.openConnection();
//                            InputStream in = connection.getInputStream();
//
//                            OutputStream fos = cordova.getActivity().getContentResolver().openOutputStream(newFile.getUri());
//
//                            byte[] buf = new byte[512];
//                            while (true) {
//                                int len = in.read(buf);
//                                if (len == -1) {
//                                    break;
//                                }
//                                fos.write(buf, 0, len);
//                            }
//                            in.close();
//                            fos.flush();
//                            fos.close();
//
//                            result.put("uri", uri);
//                            this.callback.success(result);
                        } catch (Exception err) {
                            this.callback.error("Failed to copy file: " + err.toString());
                        }
                    }
                }.start();
            } else {
                this.callback.error("Folder URI was null.");
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            this.callback.success("RESULT_CANCELED");
        } else {
            this.callback.error(resultCode);
        }
    }

    private void selectDirPath(CallbackContext callbackContext, String initialPath) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Intent chooser = Intent.createChooser(intent, "Open folder");

        if (initialPath != null && !initialPath.isEmpty()) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialPath);
        }

        cordova.startActivityForResult(this, intent, USBFilesManager.PICK_DIR_REQUEST);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        this.callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    private void saveFileToTargetDirectory(CallbackContext callbackContext, String fileName) {
        this.inputFileName = fileName;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Intent chooser = Intent.createChooser(intent, "Open folder");
        cordova.startActivityForResult(this, chooser, USBFilesManager.PICK_FOLDER_REQUEST_FOR_SAVE);

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        this.callback = callbackContext;
        callbackContext.sendPluginResult(pluginResult);
    }

    private void getFilesListByUri(CallbackContext callbackContext, String uri) {
        try {
            JSONArray result = new JSONArray();
            DocumentFile filesDir = DocumentFile.fromTreeUri(cordova.getActivity(), Uri.parse(uri));
            DocumentFile[] documents = filesDir.listFiles();

            for (final DocumentFile file : documents) {
                JSONObject resultFile = new JSONObject();
                if (file.isFile()) {
                    resultFile.put("isFile", file.isFile());
                    resultFile.put("name", file.getName());
                    resultFile.put("url", file.getUri());
                    resultFile.put("type", file.getType());
                    resultFile.put("size", file.length());
                    resultFile.put("dirUri", uri);
                    result.put(resultFile);
                }
            }

            callbackContext.success(result);
        } catch (Exception err) {
            callbackContext.error("Failed to read file: " + err.toString());
        }
    }

    private void copyFileFromUSB(CallbackContext callbackContext, String fileUri, String fileName) {
        try {
            InputStream in = null;
            OutputStream out = null;
            String error = null;

            String targetPath = cordova.getActivity().getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/" + fileName;

            try {
                in = new FileInputStream(new File(fileUri));
                out = new FileOutputStream(new File(targetPath));

                copy(in, out);
            } catch (FileNotFoundException fnfe1) {
                error = fnfe1.getMessage();
            } catch (Exception e) {
                error = e.getMessage();
            }

            JSONObject result = new JSONObject();
            result.put("error", error);
            result.put("fileName", fileName);
            result.put("url", targetPath);
            callbackContext.success(result);
        } catch (Exception err) {
            callbackContext.error("Failed to copy file from USB: " + err.toString());
        }
    }

    private void deleteFile(CallbackContext callbackContext, String fileUri) {
        Boolean deleted = false;
        String error = null;

        try {
            JSONObject result = new JSONObject();
            try {
                deleted = DocumentFile.fromSingleUri(cordova.getActivity().getApplicationContext(), Uri.parse(fileUri)).delete();
            } catch (Exception e) {
                error = e.getMessage();
            }
            result.put("error", error);
            result.put("deleted", deleted);
            callbackContext.success(result);
        } catch (Exception err) {
            callbackContext.error("Failed to remove file: " + err.toString());
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

    private static String getFileMimeType(String fileName) {
        String mimeType = "application/" + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        return mimeType;
    }

    class GetFileFromURL extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DocumentFile pickedDir = DocumentFile.fromTreeUri(cordova.getActivity(), params.getString(0));
                String mimeType = getFileMimeType(this.inputFileName);
                DocumentFile newFile = pickedDir.createFile(mimeType, this.inputFileName);

                URL url = new URL("http://54.156.240.184:50420/backups/5e130c0a9f274b377d7005a4/backup-31012020092346");
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();

                OutputStream fos = cordova.getActivity().getContentResolver().openOutputStream(newFile.getUri());

                byte[] buf = new byte[512];
                while (true) {
                    int len = in.read(buf);
                    if (len == -1) {
                        break;
                    }
                    fos.write(buf, 0, len);
                }
                in.close();
                fos.flush();
                fos.close();

                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            this.callback.success("OK");
        }
    }
}
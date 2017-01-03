# cordova-plugin-app-update
This is a plugin for cordova(only Android). What plugin did:
 1. download the file from server and put it into download file (your device)
 2. add notification in notification bar
 3. after download complete show the install dialog

What should you do:
 1. copy update_app.xml into your project/res/layout. create layout file if no
 2. add permission \<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> to AndroidManifest.xml
 3. invoke the method updateFromServer(url, serverVersion, successCallback, errorCallback) use javascripr in your js file
 4. url: the url that where apk file will download
 5. serverVersion: the new version is need

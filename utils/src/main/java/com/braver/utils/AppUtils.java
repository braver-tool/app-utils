/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 17/11/21, 03:10 PM
 *
 */

package com.braver.utils;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;

import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.WIFI_SERVICE;

public class AppUtils {
    private static final String EMAIL_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z.]{2,64}";
    private static final String AT_LEAST_ONE_CAPITAL_LETTER_PATTERN = ".*[A-Z]+.*";
    private static final String AT_LEAST_ONE_SPECIAL_CHAR_PATTERN = ".*[@#$%^&+!=]+.*";
    private static final String AT_LEAST_ONE_NUMBER_PATTERN = ".*[0-9]+.*";
    private static final String URL_REGEX_PATTERN = "^(https?://)?(www\\.)?([-a-z0-9]{1,63}\\.)*?[a-z0-9][-a-z0-9]{0,61}[a-z0-9]\\.[a-z]{2,6}(/[-\\w@\\+\\.~#\\?&/=%]*)?$";
    private static final String PHONE_NUMBER_PATTERN = "^[1-9][0-9]{9}";

    /**
     * Method is return boolean values based on app is foreground or Background
     *
     * @param context - Current Activity
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo;
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                status = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return status;
    }

    /**
     * Method is return boolean values based on app is foreground
     *
     * @return boolean
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = null;
        if (activityManager != null) {
            appProcesses = activityManager.getRunningAppProcesses();
        }
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method is return boolean ~ validate email pattern
     *
     * @return boolean
     */
    public static boolean isValidEmailAddress(String userEmail) {
        return userEmail.matches(EMAIL_PATTERN);
    }

    /**
     * Method is return boolean ~ validate password ~ Case Sensitive
     * Should be minimum 8 characters
     * Should have at least one Capital letter
     * Should have at least one Number
     * Should have at least one Special Character
     *
     * @return boolean
     */
    public static boolean isValidPassword(String userPassword) {
        boolean isValidPassword = true;
        if (!userPassword.matches(AT_LEAST_ONE_CAPITAL_LETTER_PATTERN)) {
            isValidPassword = false;
        } else if (!userPassword.matches(AT_LEAST_ONE_NUMBER_PATTERN)) {
            isValidPassword = false;
        } else if (!userPassword.matches(AT_LEAST_ONE_SPECIAL_CHAR_PATTERN)) {
            isValidPassword = false;
        }
        return isValidPassword;
    }

    /**
     * Method is return boolean ~ validate web url
     *
     * @return boolean
     */
    public static boolean isValidUrl(String webUrl) {
        return webUrl.matches(URL_REGEX_PATTERN);
    }

    /**
     * Method is return boolean ~ validate phone number
     *
     * @return boolean
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    /**
     * Method used to get the device width and height
     *
     * @param context activity context
     * @return point value
     */
    public static Point getMeasurementDetail(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Method used to get String id(UUID)
     *
     * @return string id
     */
    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Method used to get App Version
     *
     * @param context - Current fragment
     */
    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * Method used to get Device version
     */
    public static String getDeviceID(Context context) {
        String deviceID = " ";
        try {
            deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            Log.d("getDeviceID--->Utils", e.getMessage());
        }
        return deviceID;
    }

    /**
     * Method used to validate GPS enable and location permission
     *
     * @param context - Current Activity
     * @return GPS is enable and location then return true else if return false
     */
    public static boolean isDeviceGPSEnabledAndLocationPermission(Context context) {
        boolean isGPSEnabled = false;
        if (context != null) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager != null) {
                isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(context);
            }
            if (isGPSEnabled) {
                isGPSEnabled = (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return isGPSEnabled;
    }

    /**
     * Method used to validate GPS enable or not
     *
     * @param context - Current Activity
     * @return GPS is enable then return true else if return false
     */
    public static boolean isDeviceGPSEnabled(Context context) {
        boolean isGPSEnabled = false;
        if (context != null) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager != null) {
                isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(context);
            }
        }
        return isGPSEnabled;
    }

    /**
     * Method used to validate GPS enable or not
     *
     * @param context - Current Activity
     * @return GPS is enable then return true else if return false
     */
    private static boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null) return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null) return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    /**
     * Method used to validate GPS enable or not
     *
     * @param context - Current Activity
     * @return Method used to get boolean result, that might have user's device supports and hasEnrolledFingerprints
     */
    public static boolean isDeviceHaveFingerPrints(Context context) {
        boolean isFingerPrints = false;
        try {
            FingerprintManager fingerprintManager;
            fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
            if (fingerprintManager != null) {
                if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    isFingerPrints = true;
                }
            }
        } catch (Exception e) {
            Log.d("##isHaveFingerPrints", "-------->" + e.getMessage());
        }
        return isFingerPrints;
    }

    /**
     * Method used to validate GPS enable or not
     *
     * @param context - Current Activity
     * @return Method used to get boolean result, user's device has security protection
     */
    public static boolean isDeviceHaveKeyguard(Context context) {
        boolean isKeyguardService = false;
        try {
            KeyguardManager keyguardManager = (KeyguardManager) Objects.requireNonNull(context).getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager != null && keyguardManager.isKeyguardSecure()) {
                isKeyguardService = true;
            }
        } catch (Exception e) {
            Log.d("##isHaveKeyguard", "-------->" + e.getMessage());
        }
        return isKeyguardService;
    }

    /**
     * Method used to validate get device have pin or pattern or fingerScan protected
     *
     * @param context - Current Activity
     * @return Method used to get boolean result, user's device has security protection
     */
    public static boolean isDeviceHaveSelfSecurity(Context context) {
        boolean isSelfSecurity = false;
        try {
            if ((isDeviceHaveFingerPrints(context) || isDeviceHaveKeyguard(context))) {
                isSelfSecurity = true;
            }
        } catch (Exception e) {
            Log.d("##isSelfSecurity", "-------->" + e.getMessage());
        }
        return isSelfSecurity;
    }

    /**
     * @param context - Application Context
     * @return - IP address when user using Wi-Fi or Mobile data for Internet access
     * Method used to get IP Address
     */
    public static String getIPAddress(Context context) {
        try {
            String deviceIP = "";
            ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = CM.getAllNetworkInfo();
            for (NetworkInfo netInfo : networkInfo) {
                if (netInfo.getTypeName().equalsIgnoreCase("WIFI") && netInfo.isConnected()) {
                    deviceIP = getDeviceIPFromWifi(context);
                } else if (netInfo.getTypeName().equalsIgnoreCase("MOBILE") && netInfo.isConnected()) {
                    deviceIP = getDeviceIPFromMobileData();
                }
            }
            return deviceIP;
        } catch (Exception ex) {
            Log.e("##IP##getIPAddress---->", "------->" + ex.getMessage());
        }
        return "";
    }

    /**
     * @param context - Application Context
     * @return - IP address when user using Wi-Fi for Internet access
     * Method used to get IP Address
     */
    private static String getDeviceIPFromWifi(Context context) {
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            @SuppressWarnings("deprecation")
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            return ip;
        } catch (Exception ex) {
            Log.e("##IP##FromWifi", "------->" + ex.getMessage());
        }
        return "";
    }

    /**
     * @return - IP address when user using Mobile data for Internet access
     * Method used to get IP Address
     */
    private static String getDeviceIPFromMobileData() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        return Formatter.formatIpAddress(sAddr.hashCode());
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("##IP##fromMobileData", "------->" + ex.getMessage());
        }
        return "";
    }

    /**
     * This method used to get Internet Provider Name
     *
     * @param context - current activity
     */
    public static String getCarrierName(Context context) {
        String carrierName = "";
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr != null ? connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI) : null;
        NetworkInfo mobile = connectivityMgr != null ? connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) : null;
        if (wifi != null) {
            if (wifi.isConnected()) {
                carrierName = wifi.getExtraInfo();
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                carrierName = mobile.getExtraInfo();
            }
        }
        return carrierName;
    }

    /**
     * Method used to convert String list into String value
     *
     * @param strList - string value
     * @return string value
     */
    public static String convertStringListToString(List<String> strList) {
        String convertedString = "";
        if (strList.size() > 0) {
            String str = Arrays.toString(strList.toArray());
            convertedString = str.replace("[", "").replace("]", "");
            convertedString = convertedString.replaceAll("\\s", "");
        }
        return convertedString;
    }

    /**
     * Method used to convert String  into String list
     *
     * @param str - string value
     * @return string list value
     */
    public static List<String> convertStringToStringList(String str) {
        List<String> convertedStringList = new ArrayList<>();
        if (!str.isEmpty()) {
            convertedStringList = Arrays.asList(str.split(","));
        }
        return convertedStringList;
    }

    /**
     * This method is a string return method
     * Method used get file name from local file path
     */
    private static String getFileName(String filePath) {
        String fileName = "";
        try {
            fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        } catch (Exception e) {
            Log.d("getFileName()---->Utils", e.getMessage());
        }
        return fileName;
    }

    /**
     * Method used to download file from url and save local file path
     *
     * @param url     - attachment file document URL
     * @param context - Current Activity
     */
    public static String downloadFileFromUrl(URL url, Context context) {
        URLConnection connection;
        try {
            int count;
            connection = url.openConnection();
            connection.connect();
            File filePath = context.getFilesDir();
            String fileName = getFileName(String.valueOf(url));
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            String sourceFilePath = filePath.getPath() + "/" + fileName;
            OutputStream output = new FileOutputStream(sourceFilePath);
            byte[] data = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return sourceFilePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method is used to return string value from the JSON file which is in Asset folder
     *
     * @param context - Current Activity
     */
    public static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("file_name.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * @param url - String Data
     * @return - VideoID
     * Method used to return video id from YouTubeVideoLink
     */
    public static String getVideoIdFromYTLink(String url) {
        try {
            String videoID = "";
            if (!url.contains("we.tl")) {
                if (url.contains("watch?v=")) {
                    String[] vidId = url.split("\'?v=");
                    videoID = vidId[1];
                    if (videoID.contains("&")) {
                        String[] vidIds = videoID.split("&");
                        videoID = vidIds[0];
                    }
                } else {
                    String[] vidId = url.split(".be/");
                    videoID = vidId[1];
                }
            }
            return videoID;
        } catch (Exception e) {
            Log.d("##getVideoIdFromLink", "---------->");
            return "";
        }
    }

    /**
     * @param context - current screen
     * @param url     - String data
     *                Method used to navigate web browser
     */
    public static void navigateToBrowserWithUrl(Context context, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            Log.d("##navigateToBrowser", "------->" + e.getMessage());
        }
    }

}
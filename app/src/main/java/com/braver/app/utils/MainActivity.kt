/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.app.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.braver.utils.*
import java.util.*

class MainActivity : AppCompatActivity(), DialogUtils.OnDialogWidgetClick {
    private lateinit var prefUtils: PreferenceUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefUtils = PreferenceUtils.getInstance(this@MainActivity)
        usageOfUtils()
    }

    override fun onPositiveClick() {
        Log.d("##", "onPositiveClick------->")
    }

    override fun onNegativeClick() {
        Log.d("##", "onNegativeClick------->")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the main; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_permission -> {
                checkSamplePermission()
            }
            R.id.action_alert_dialog_1 -> {
                DialogUtils.showAlertMsgDialogWithSingleWidget(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit. At autem culpa dignissimos eaque eos facilis impedit inventore laudantium modi, molestias neque numquam obcaecati officiis porro quisquam vel veritatis, vitae! Aut culpa cumque dolorum magni perspiciatis, recusandae ut voluptatibus. Consequuntur doloribus ducimus expedita minus ratione tempore voluptate? A harum nulla ratione?",
                    this@MainActivity,
                    this
                )
            }
            R.id.action_alert_dialog_2 -> {
                DialogUtils.showAlertMsgDialogWithTwoWidget(
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit. At autem culpa dignissimos eaque eos facilis impedit inventore laudantium modi, molestias neque numquam obcaecati officiis porro quisquam vel veritatis, vitae! Aut culpa cumque dolorum magni perspiciatis, recusandae ut voluptatibus. Consequuntur doloribus ducimus expedita minus ratione tempore voluptate? A harum nulla ratione?",
                    this@MainActivity,
                    this
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 3646) {
            var isCameraPermission = true
            for (grantResult in grantResults) {
                isCameraPermission =
                    isCameraPermission and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            Log.d("##", "isCameraPermission------->$isCameraPermission")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkSamplePermission() {
        val permissionList: MutableList<String> = ArrayList()
        permissionList.add(Manifest.permission.CAMERA)
        val isCameraPermission =
            PermissionUtils.isSpecialPermissionGranted(this@MainActivity, permissionList)
        if (isCameraPermission) {
            Log.d("##", "isCameraPermission------->$isCameraPermission")
        } else {
            PermissionUtils.showSpecialPermissionRequestDialog(
                this@MainActivity,
                permissionList,
                3646
            )
        }
    }

    private fun usageOfUtils() {
        //SharedPreferences
        prefUtils.setStringValue("DATA", "SAMPLE")
        //Check Network Availability
        val isNetwork = AppUtils.isNetworkAvailable(this@MainActivity)
        Log.d("##", "isNetwork------->$isNetwork")
        //Check App State
        val isAppFore = AppUtils.isAppOnForeground(this@MainActivity)
        Log.d("##", "isAppFore------->$isAppFore")
        //Check Valid Email Address
        val isValidEmail = AppUtils.isValidEmailAddress("braver.777@yopmail.com")
        Log.d("##", "isValidEmail------->$isValidEmail")
        //Check Valid Password
        val isValidPass = AppUtils.isValidPassword("tEst321@")
        Log.d("##", "isValidPass------->$isValidPass")
        //Check Valid URL
        val isValidUrl = AppUtils.isValidUrl("https://www.androiddeveloper.co.in/")
        Log.d("##", "isValidUrl------->$isValidUrl")
        //Check Valid Phone
        val isValidPhone = AppUtils.isValidPhoneNumber("9876543210")
        Log.d("##", "isValidPhone------->$isValidPhone")
        //Get Device's Screen Size
        val deviceSizeInfo = AppUtils.getMeasurementDetail(this@MainActivity)
        Log.d("##", "deviceSizeInfo~Width------->${deviceSizeInfo.x}")
        Log.d("##", "deviceSizeInfo~Height------->${deviceSizeInfo.y}")
        //Check is Tab
        val isTab = AppUtils.isTablet(this@MainActivity)
        Log.d("##", "isTab------->$isTab")
        //Get Random UUID
        val randomID = AppUtils.getRandomUUID()
        Log.d("##", "randomID------->$randomID")
        //Get App Version
        val appVersion = AppUtils.getAppVersion(this@MainActivity)
        Log.d("##", "appVersion------->$appVersion")
        //Get DeviceID
        val deviceID = AppUtils.getDeviceID(this@MainActivity)
        Log.d("##", "deviceID------->$deviceID")
        //Check device have finger prints
        val isHaveFingerPrints = AppUtils.isDeviceHaveFingerPrints(this@MainActivity)
        Log.d("##", "isHaveFingerPrints------->$isHaveFingerPrints")
        //Check device have keyguard
        val isHaveKeyguard = AppUtils.isDeviceHaveKeyguard(this@MainActivity)
        Log.d("##", "isHaveKeyguard------->$isHaveKeyguard")
        //Get IP Address
        val ipAddress = AppUtils.getIPAddress(this@MainActivity)
        Log.d("##", "ipAddress------->$ipAddress")
        //Call AES Encryption
        val encText = CryptoUtils.callEncryptMethod("Pass123", "Braver-Tool")
        Log.d("##", "encText------->$encText")
        //Call AES Decryption
        val decryptText = CryptoUtils.callDecryptMethod("Pass123", encText)
        Log.d("##", "decryptText------->$decryptText")
    }
}
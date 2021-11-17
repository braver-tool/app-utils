/* * *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM *  * Copyright (c) 2021 . All rights reserved. *  * Last modified 17/11/21, 03:10 PM * */package com.braver.utils;import android.app.Dialog;import android.content.Context;import android.util.Log;import android.view.Gravity;import android.view.Window;import android.view.WindowManager;import android.widget.TextView;import java.util.Objects;public class DialogUtils {    public interface OnDialogWidgetClick {        void onPositiveClick();        void onNegativeClick();    }    /**     * Method used to shown alert message on popup window     *     * @param alertMessage - String data, that contains alert message     * @param context      - current activity     */    public static void showAlertMsgDialogWithSingleWidget(String alertMessage, Context context, OnDialogWidgetClick onDialogWidgetClick) {        try {            final Dialog dialog = new Dialog(context);            dialog.setContentView(R.layout.alert_dialog_single_widget);            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);            Window window = dialog.getWindow();            WindowManager.LayoutParams wlp;            if (window != null) {                wlp = window.getAttributes();                wlp.gravity = Gravity.CENTER;                window.setAttributes(wlp);            }            TextView alertDialogOkTextView = dialog.findViewById(R.id.alertDialogOkTextView);            TextView alertMsgTextView = dialog.findViewById(R.id.alertMsgTextView);            alertMsgTextView.setText(alertMessage);            alertDialogOkTextView.setOnClickListener(v -> {                onDialogWidgetClick.onPositiveClick();                dialog.dismiss();            });            dialog.show();        } catch (Exception e) {            Log.d("##showAlertMsgDialog", "-------------->" + e.getMessage());        }    }    /**     * Method used to shown alert message on popup window     *     * @param alertMessage - String data, that contains alert message     * @param context      - current activity     */    public static void showAlertMsgDialogWithTwoWidget(String alertMessage, Context context, OnDialogWidgetClick onDialogWidgetClick) {        try {            final Dialog dialog = new Dialog(context);            dialog.setContentView(R.layout.alert_dialog_two_widget);            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);            Window window = dialog.getWindow();            WindowManager.LayoutParams wlp;            if (window != null) {                wlp = window.getAttributes();                wlp.gravity = Gravity.CENTER;                window.setAttributes(wlp);            }            TextView alertDialogOkTextView = dialog.findViewById(R.id.alertDialogPositiveTextView);            TextView alertDialogCancelTextView = dialog.findViewById(R.id.alertDialogNegativeTextView);            TextView alertMsgTextView = dialog.findViewById(R.id.alertMsgTextView);            alertMsgTextView.setText(alertMessage);            alertDialogOkTextView.setOnClickListener(v -> {                onDialogWidgetClick.onPositiveClick();                dialog.dismiss();            });            alertDialogCancelTextView.setOnClickListener(v -> {                onDialogWidgetClick.onNegativeClick();                dialog.dismiss();            });            dialog.show();        } catch (Exception e) {            Log.d("##showAlertMsgDialog", "-------------->" + e.getMessage());        }    }}
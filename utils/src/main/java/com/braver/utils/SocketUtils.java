/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Step 1:
 * You can call #initSocket() method on your project's application class OnCreate delegate with your socket url
 * Step 2:
 * Add getSocket() method on application class
 * Step 3:
 * call connect delegate on your activity's onCreate or where else you want ~ like mSocket.connect();
 * Step 4:
 * You call #2 in any place on your project, using 'mSocket' variable you cal call 'Emit','on' and 'Emitter.Listener' methods easily.
 * Step 5:
 * call disConnect delegate on your activity's onDestroy or where else you want ~ like mSocket.disconnect();
 */
public class SocketUtils {

    private static Socket mSocket;

    public static Socket getSocket() {
        return mSocket;
    }

    public static void initSocket(String socketUrl) {
        try {
            mSocket = IO.socket(socketUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Sample for Socket.IO

    // For Socket ON and Emitter.Listener - put like below on onCreate/onResume on your activity

    private void onCreate() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on("new-response", onNewResponse);
    }

    private final Emitter.Listener onConnect = args -> {
        try {
            JSONObject data = (JSONObject) args[0];
            Gson gson = new GsonBuilder().create();
            String jsonData = gson.toJson(data);
            // do whatever with the response
        } catch (Exception e) {

        }
    };
    private final Emitter.Listener onNewResponse = args -> {
        try {
            JSONObject data = (JSONObject) args[0];
            Gson gson = new GsonBuilder().create();
            String jsonData = gson.toJson(data);
            // do whatever with the response
        } catch (Exception e) {

        }
    };

    // For Socket Emit

    private void sendTextOnSocket() {
        String json = "some-thing";
        try {
            JSONObject jsonObj = new JSONObject(json);
            mSocket.emit("new-message", jsonObj);
        } catch (JSONException e) {

        }
    }
}

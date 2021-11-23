/*
 *
 *  * Created by https://github.com/braver-tool on 16/11/21, 10:30 AM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 23/11/21, 03:40 PM
 *
 */

package com.braver.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * To Know more about Retrofit APIs on Android development ~
 * Please visit My Page ~ https://github.com/braver-tool/AndroidRestfulAPI
 */
public class ApiUtils {


    /**
     * @return --> retrofit builder
     */
    public static Retrofit getRetrofitBuilder(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(getSafeOkHttpClient()).build();
    }

    /**
     * Method returns HTTP builder with LoggingInterceptor
     *
     * @return - OkHttpClient
     */
    @SuppressLint("TrustAllX509TrustManager")
    private static OkHttpClient getSafeOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.connectTimeout(2, TimeUnit.MINUTES);
                builder.readTimeout(2, TimeUnit.MINUTES);
                builder.writeTimeout(2, TimeUnit.MINUTES);
                builder.protocols(Util.immutableListOf(Protocol.HTTP_1_1));
                builder.hostnameVerifier((hostname, session) -> true);
                builder.addNetworkInterceptor(logging);
                builder.addInterceptor(chain -> {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);
                    Log.e("##ApiUtils", "------------>" + response.code());
                    return response;
                });
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .addNetworkInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);
                        Log.e("##ApiUtils", "------------>" + response.code());
                        return response;
                    })
                    .protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
        }
    }

    /**
     * Method returns HTTP builder with LoggingInterceptor
     *
     * @return - OkHttpClient
     */
    @SuppressLint("TrustAllX509TrustManager")
    private static OkHttpClient getSafeOkHttpClientWithCommonHeaders(String key, String value) {
        //Example : key is 'Authorization' and the value is '<TOKEN>'
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.connectTimeout(2, TimeUnit.MINUTES);
                builder.readTimeout(2, TimeUnit.MINUTES);
                builder.writeTimeout(2, TimeUnit.MINUTES);
                builder.protocols(Util.immutableListOf(Protocol.HTTP_1_1));
                builder.hostnameVerifier((hostname, session) -> true);
                builder.addNetworkInterceptor(logging);
                builder.addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().header(key, value).build();
                    okhttp3.Response response = chain.proceed(request);
                    Log.e("##ApiUtils", "------------>" + response.code());
                    return response;
                });
                return builder.build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .addNetworkInterceptor(logging)
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder().header(key, value).build();
                        okhttp3.Response response = chain.proceed(request);
                        Log.e("##ApiUtils", "------------>" + response.code());
                        return response;
                    })
                    .protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();
        }
    }

    // Example API Call
    /*
    private interface SampleApiService {
        @Headers({"Content-Type:application/json;"})
        @POST("{<End-Point-of-url>}")
        Call<SampleApiResponse> callPostApi(@Body SampleApiRequest postRequest);
    }

    private static SampleApiService sampleApiService() {
        return getRetrofitBuilder("<Base-Url>").create(SampleApiService.class);
    }

    private static void callPostApiMethod() {
        SampleApiRequest sampleApiRequest = new SampleApiRequest();
        Call<SampleApiResponse> call = sampleApiService().callPostApi(sampleApiRequest);
        call.enqueue(new Callback<SampleApiResponse>() {
            @Override
            public void onResponse(Call<SampleApiResponse> call, Response<SampleApiResponse> response) {
                Log.d("callPostApiMethod", "----------->" + response.code());
            }

            @Override
            public void onFailure(Call<SampleApiResponse> call, Throwable t) {
                Log.d("callPostApiMethod", "----------->Post Error  :" + t.getMessage());

            }
        });
    }

    */

}

package com.example.appproject.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appproject.MyApplication;
import com.example.appproject.R;
import com.example.appproject.login.LoginActivity;
import com.example.appproject.model.listeners.BooleanListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class General {

    public static void enableDisableClickView(View view, boolean enabled) {
        view.setClickable(enabled);
        if ( view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableClickView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static void progressBarOn(Activity activity, ViewGroup container, ProgressBar progressBar, Boolean showBackButton) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.VISIBLE);
            General.enableDisableClickView(container, false);
            if( ((AppCompatActivity) activity).getSupportActionBar() != null){
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            }
        });
    }

    public static void progressBarOff(Activity activity, ViewGroup container, ProgressBar progressBar, Boolean showBackButton) {
        Model.instance.getMainThread().post(()->{
            progressBar.setVisibility(View.GONE);
            General.enableDisableClickView(container, true);
            if(((AppCompatActivity) activity).getSupportActionBar() != null){
                ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(showBackButton);
            }
        });
    }

    public static void loadImage(String url, ImageView imageView, int placeholder, BooleanListener listener){
        if(url == null || url.equals(""))
            return;

        OkHttpClient client;
        if(url.contains("poll-it.cs.colman.ac.il") || url.contains("10.10.248.124")){
            client = General.getOkClientWithAuth();
        }
        else{
            client = General.getOkHttpClient();
        }
        Picasso picasso = new Picasso.Builder(MyApplication.getContext()).downloader(new OkHttp3Downloader(client)).build();
        picasso.load(url)
                .placeholder(placeholder)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("TAG", "success loading image");
                        listener.onComplete(true);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("TAG", "error loading image: " + e.getMessage());
                        listener.onComplete(false);
                    }
                });
    }

    public static OkHttpClient getOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getOkClientWithAuth() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + MyApplication.getAccessToken())
                        .build();
                return chain.proceed(newRequest);
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

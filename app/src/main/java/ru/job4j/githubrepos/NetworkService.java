package ru.job4j.githubrepos;

import android.util.Log;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    //private String BASE_URL = "https://api.github.com";
    private String BASE_URL = "https://api.github.com//api/v3/";
    private Retrofit mRetrofit;

    private NetworkService() {

        HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
        interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientLog = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptorLog.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        clientLog.addInterceptor(interceptorLog);
        OkHttpClient clientErrorIntercept = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);
                        if (response.code() >= 400 && response.code() <= 599) {

                            Log.i("MyError", "" + response.code());
                            return response;
                        }
                        return response;
                    }
                })
                .build();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic("dmk78", "Ek193burg"));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();

// Set the custom client when building adapter
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .client(clientLog.build())
                .client(clientErrorIntercept)
                .build();


    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JsonPlaceHolderApi getJSONApi() {
        return mRetrofit.create(JsonPlaceHolderApi.class);
    }
}

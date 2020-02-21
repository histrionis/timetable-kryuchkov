package ru.daniilkryuchkov.timetablekryuchkov;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;

    private static final String BASE_URL = "https://sample.fitnesskit-admin.ru/";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONFitnessKitApi getJSONApi() {
        return mRetrofit.create(JSONFitnessKitApi.class);
    }
}

package heybook.team1.com.heybookv2.API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nokta on 2.01.2017.
 */

public class ApiClient {

    private final static String BASE_URL = "http://heybook.online";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        OkHttpClient client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

}

package heybook.team1.com.heybookv2.API;

import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.Model.User;
import retrofit2.Call;
<<<<<<< HEAD
import retrofit2.Callback;
import retrofit2.Response;
=======
import retrofit2.http.Body;
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Nokta on 2.01.2017.
 */

public interface ApiClientInterface {
    @GET("/api.php?request=books")
    Call<Book> getBook();

    @GET("/api.php?request=users")
    Call<User> getUser();


    @GET("/api.php?request=books")
    Call<Book> getAllBooks();

<<<<<<< HEAD
    @FormUrlEncoded
    @POST("/test.php")
    public void insertUser(
            @Field("user_title") String user_title,
            @Field("mail") String mail,
            @Field("password") String password,
            Callback<Response> callback);
=======
    // Login Request
    @FormUrlEncoded
    @POST("/test.php")
    Call<String> setUser(@Field("user_title") String user_title,
                                @Field("mail") String mail,
                                @Field("password") String password);

>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490

}

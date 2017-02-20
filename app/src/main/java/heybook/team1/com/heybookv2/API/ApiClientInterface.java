package heybook.team1.com.heybookv2.API;

import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
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

    // Login Request
    @FormUrlEncoded
    @POST("/test.php")
    Call<String> setUser(@Field("user_title") String user_title,
                                @Field("mail") String mail,
                                @Field("password") String password);


}

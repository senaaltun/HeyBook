package heybook.team1.com.heybookv2.API;

import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Favorite;
import heybook.team1.com.heybookv2.Model.Login;
import heybook.team1.com.heybookv2.Model.LoginData;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Sena Altun on 2.01.2017.
 */

public interface ApiClientInterface {
    @GET("/api.php?request=books")
    Call<Book> getBook();

    @GET("/api.php?request=login")
    Call<User> getUser();


    @GET("/api.php?request=books")
    Call<Book> getAllBooks();


    @FormUrlEncoded
    @POST("/api.php?request=register")
    Call<RegisterModel> setUser(@Field("user_title") String user_title,
                                @Field("mail") String mail,
                                @Field("password") String password);
    @FormUrlEncoded
    @POST("/api.php?request=user_favorites-add")
    Call<Favorite> setFavorite(@Field("user_id") String user_id,
                               @Field("book_id") String book_id);
    @FormUrlEncoded
    @POST("/api.php?request=login")
    Call<LoginData> login(@Field("mail") String mail,
                          @Field("password") String password);




}

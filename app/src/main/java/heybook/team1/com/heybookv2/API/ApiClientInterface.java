package heybook.team1.com.heybookv2.API;

import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.User;
import retrofit2.Call;
import retrofit2.http.GET;

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

}

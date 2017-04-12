package heybook.team1.com.heybookv2.Model;

/**
 * Created by senaaltun on 08/04/2017.
 */

public class Favorite{
    String user_id;
    String book_id;

    public Favorite(String user_id, String book_id){
        this.user_id = user_id;
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }
}

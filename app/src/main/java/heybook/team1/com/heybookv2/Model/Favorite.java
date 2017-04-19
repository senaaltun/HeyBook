package heybook.team1.com.heybookv2.Model;

/**
 * Created by senaaltun on 08/04/2017.
 */

public class Favorite{
    private String bookName;
    private String bookAuthor;
    private String bookDuration;
    private String bookImage;

    public Favorite(String bookName, String bookAuthor, String bookDuration,String bookImage) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookDuration = bookDuration;
        this.bookImage = bookImage;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookDuration() {
        return bookDuration;
    }

    public void setBookDuration(String bookDuration) {
        this.bookDuration = bookDuration;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}

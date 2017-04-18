package heybook.team1.com.heybookv2.Model;

import java.io.Serializable;

/**
 * Created by Sena Altun on 2.01.2017.
 */

public class Data implements Serializable {
    private String book_id;
    private String category_id;
    private String publisher_id;
    private String author_id;
    private String narrator_id;
    private String book_title;
    private String description;
    private String publishtime;
    private String price;
    private String photo;
    private String thumb;
    private String audio;
    private String duration;
    private String size;
    private String demo;
    private String hit;
    private String regtime;
    private String category_title;
    private String category_books;
    private String author_title;
    private String author_books;
    private String narrator_title;
    private String narrator_books;
    private String publisher_title;
    private String publisher_books;

    public Data(String book_id,String book_title, String photo, String author_title) {
        this.book_title = book_title;
        this.photo = photo;
        this.author_title = author_title;
        this.book_id = book_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getNarrator_id() {
        return narrator_id;
    }

    public void setNarrator_id(String narrator_id) {
        this.narrator_id = narrator_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_books() {
        return category_books;
    }

    public void setCategory_books(String category_books) {
        this.category_books = category_books;
    }

    public String getAuthor_title() {
        return author_title;
    }

    public void setAuthor_title(String author_title) {
        this.author_title = author_title;
    }

    public String getAuthor_books() {
        return author_books;
    }

    public void setAuthor_books(String author_books) {
        this.author_books = author_books;
    }

    public String getNarrator_title() {
        return narrator_title;
    }

    public void setNarrator_title(String narrator_title) {
        this.narrator_title = narrator_title;
    }

    public String getNarrator_books() {
        return narrator_books;
    }

    public void setNarrator_books(String narrator_books) {
        this.narrator_books = narrator_books;
    }

    public String getPublisher_title() {
        return publisher_title;
    }

    public void setPublisher_title(String publisher_title) {
        this.publisher_title = publisher_title;
    }

    public String getPublisher_books() {
        return publisher_books;
    }

    public void setPublisher_books(String publisher_books) {
        this.publisher_books = publisher_books;
    }
}

package heybook.team1.com.heybookv2.Model;

/**
 * Created by senaaltun on 02/04/2017.
 */

public class VitrinBookEntity {
    public String imageUrl;
    public String title;

    public VitrinBookEntity(String imageUrl, String title){
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

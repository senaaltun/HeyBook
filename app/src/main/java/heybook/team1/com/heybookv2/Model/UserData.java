package heybook.team1.com.heybookv2.Model;

/**
 * Created by Nokta on 10.01.2017.
 */

public class UserData {
    private String user_id;
    private String user_title;
    private String mail;
    private String password;
    private String subscribe;
    private String photo;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_title() {
        return user_title;
    }

    public void setUser_title(String user_title) {
        this.user_title = user_title;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

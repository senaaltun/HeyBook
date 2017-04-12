package heybook.team1.com.heybookv2.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by senaaltun on 08/04/2017.
 */

public class Login implements Serializable {
    String user_id;
    String valid_status;
    String valid_hash;
    String user_title;
    String mail;
    String password;
    String subscribe;
    String disabled;
    String photo;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getValid_status() {
        return valid_status;
    }

    public void setValid_status(String valid_status) {
        this.valid_status = valid_status;
    }

    public String getValid_hash() {
        return valid_hash;
    }

    public void setValid_hash(String valid_hash) {
        this.valid_hash = valid_hash;
    }

    public String getUser_title() {
        return user_title;
    }

    public void setUser_title(String user_title) {
        this.user_title = user_title;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Login(String mail, String password){
        this.mail = mail;
        this.password=password;
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
}

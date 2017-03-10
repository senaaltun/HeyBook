package heybook.team1.com.heybookv2.Model;

/**
 * Created by SenaAltun on 15/02/2017.
 */

public class RegisterModel {
    private String user_title;
    private String mail;
    private String password;

    public RegisterModel(String user_title,String mail,String password){
        this.user_title = user_title;
        this.mail=mail;
        this.password=password;
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
}

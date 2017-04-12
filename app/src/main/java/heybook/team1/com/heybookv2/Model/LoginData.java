package heybook.team1.com.heybookv2.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by senaaltun on 08/04/2017.
 */

public class LoginData implements Serializable {
    private String response;
    private String message;
    //private Login data;

    public LoginData(String response, String message) {
        this.response = response;
        this.message = message;
        //this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }*/
}

package heybook.team1.com.heybookv2.Model;

import java.util.List;

/**
 * Created by Nokta on 10.01.2017.
 */

public class User {
    private String response;
    private String message;
    private List<UserData> data;

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

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }
}

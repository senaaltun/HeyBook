package heybook.team1.com.heybookv2.Model;

import java.util.List;

/**
 * Created by Nokta on 22.12.2016.
 */

public class Book {
    private String response;
    private String message;
    private List<Data> data;


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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}

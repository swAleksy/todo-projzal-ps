package ps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Todo {
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("deadline")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date deadline;

    @JsonProperty("isCompleted")
    private boolean isCompleted;

    public Todo(){
    }

    public Todo(int id, String title, Date deadline, boolean isCompleted){
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void printData(){
        System.out.println("Id: " + id);
        System.out.println("title: " + title);
        System.out.println("deadline: " + deadline);
        System.out.println("completed: " + isCompleted);
    }
}

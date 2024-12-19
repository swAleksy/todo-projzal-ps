package ps.model;

import java.util.Date;

public class Todo {
    private int id;
    private String title;
    private Date deadline;
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

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public void printData(){
        System.out.println(id);
        System.out.println(title);
        System.out.println(deadline);
        System.out.println(isCompleted);
    }
}

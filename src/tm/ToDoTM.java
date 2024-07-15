package tm;

public class ToDoTM {
    private String todo_id;
    private String description;
    private String user_id;

    public ToDoTM(String todo_id, String description, String user_id) {
        this.todo_id = todo_id;
        this.description = description;
        this.user_id = user_id;
    }

    public ToDoTM() {
    }

    @Override
    public String toString() {
        return description;
    }

    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

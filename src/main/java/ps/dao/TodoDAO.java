package ps.dao;
import ps.database.Database;
import ps.model.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoDAO {

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        String query = "SELECT * FROM todos";

        try (Connection connection = Database.getConnection();
        PreparedStatement statement  = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                int id = resultSet.getInt("idtodos");
                String title = resultSet.getString("title");
                java.sql.Date deadline = resultSet.getDate("deadline");
                boolean completed = resultSet.getBoolean("completed");

                todos.add(new Todo(id, title, deadline, completed));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    public Todo getTodoById(int idtodos) {
        Todo todo = null;
        String query = "SELECT * FROM todos WHERE idtodos = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter for the query
            statement.setInt(1, idtodos);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) { // Use 'if' instead of 'while'
                    int id = resultSet.getInt("idtodos");
                    String title = resultSet.getString("title");
                    java.sql.Date deadline = resultSet.getDate("deadline");
                    boolean completed = resultSet.getBoolean("completed");

                    todo = new Todo(id, title, deadline, completed); // Create the Todo object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todo;
    }


    public void addTodo(String title, Date deadline) throws SQLException{
        String query = "INSERT INTO todos (title, deadline, iscomplete) VALUES (?,?,false)";

        try (Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, title);
            statement.setDate(2, new java.sql.Date(deadline.getTime()));
            statement.executeUpdate();
        }
    }

    public void updateTodo(int id, String title, Date deadline, boolean isCompleted) throws SQLException{
        String query = "UPDATE todos SET title = ?, deadline = ?, iscompleted = ? WHERE idtodos = ?";
        try (Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, title);
            statement.setDate(2, new java.sql.Date(deadline.getTime()));
            statement.setBoolean(3, isCompleted);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    public void deleteTodo(int id) throws SQLException{
        String query = "DELETE FROM todos WHERE idtodos = ?";

        try (Connection connection = Database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}

import ps.dao.TodoDAO;
import ps.model.Todo;

import java.sql.SQLException;
import java.util.List;

public class TodoTest {
    public static void main(String[] args) throws SQLException {
        TodoDAO todoDAO = new TodoDAO();
        List<Todo> todos = todoDAO.getAllTodos();

        if (todos.isEmpty()) {
            System.out.println("Brak zadań do wyświetlenia.");
        } else {
            System.out.println("Lista zadań:");
            todos.forEach(System.out::println);
        }
    }
}

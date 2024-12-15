package ps.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ps.dao.TodoDAO;
import ps.model.Todo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/todos")
public class TodoServlet extends HttpServlet {
    private final TodoDAO todoDAO = new TodoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try(PrintWriter out = resp.getWriter()) {
            List<Todo> todos = todoDAO.getAllTodos();
            out.println("[");
            for (int i = 0; i < todos.size(); i++){
                Todo todo = todos.get(i);
                out.println("{");
                out.println("\"id\": " + todo.getId() + ",");
                out.println("\"title\": \"" + todo.getTitle() + "\",");
                out.println("\"deadline\": \"" + todo.getDeadline() + "\",");
                out.println("\"isCompleted\": " + todo.isCompleted());
                out.println("}");
                if (i < todos.size() - 1) out.println(",");
            }
            out.println("]");
        }
    }
}

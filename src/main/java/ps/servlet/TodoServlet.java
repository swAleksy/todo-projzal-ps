package ps.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ps.dao.TodoDAO;
import ps.model.Todo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/todos")
public class TodoServlet extends HttpServlet {
    private final TodoDAO todoDAO = new TodoDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Received POST request at /api/todos");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String body = req.getReader().lines().reduce("", (acc, line) -> acc + line);
            System.out.println("Request Body: " + body);

            Todo newTodo = parseJsonToTodo(body);

            todoDAO.addTodo(newTodo);

            System.out.println("Todo created: " + newTodo.getTitle());

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println("{\"message\": \"Todo created successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("{\"error\": \"Failed to create Todo\"}");
        }
    }


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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String body = req.getReader().lines().reduce("", (acc, line) -> acc + line);
            Todo updatedTodo = parseJsonToTodo(body);

            boolean isUpdated = todoDAO.updateTodo(updatedTodo);

            if (isUpdated) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200
                resp.getWriter().println("{\"message\": \"Todo updated successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                resp.getWriter().println("{\"error\": \"Todo not found\"}");
            }

        } catch (ParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            resp.getWriter().println("{\"error\": \"Invalid date format. Expected yyyy-MM-dd.\"}");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            resp.getWriter().println("{\"error\": \"An error occurred while updating the todo.\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                resp.getWriter().println("{\"error\": \"Missing 'id' parameter\"}");
                return;
            }

            int id = Integer.parseInt(idParam);
            boolean isDeleted = todoDAO.deleteTodoById(id);

            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_OK); // 200
                resp.getWriter().println("{\"message\": \"Todo deleted successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
                resp.getWriter().println("{\"error\": \"Todo not found\"}");
            }
        } catch (NumberFormatException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            resp.getWriter().println("{\"error\": \"Invalid 'id' parameter\"}");
        }
    }

    private Todo parseJsonToTodo(String json) throws ParseException {
        String[] fields = json.replace("{", "").replace("}", "").split(",");
        Todo todo = new Todo();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Oczekiwany format daty

        for (String field : fields) {
            String[] keyValue = field.split(":");
            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim().replace("\"", "");

            switch (key) {
                case "id":
                    todo.setId(Integer.parseInt(value));
                    break;
                case "title":
                    todo.setTitle(value);
                    break;
                case "deadline":
                    todo.setDeadline(dateFormat.parse(value));
                    break;
                case "isCompleted":
                    todo.setCompleted(Boolean.parseBoolean(value));
                    break;
            }
        }
        return todo;
    }
}

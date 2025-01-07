package ps.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ps.dao.TodoDAO;
import ps.model.Todo;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/api/todos/*")
public class TodoServlet extends HttpServlet {
    private final TodoDAO todoDAO = new TodoDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_KEY = "test12345";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAutorized(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        try {
            Todo newTodo = objectMapper.readValue(req.getReader(), Todo.class);
            if (newTodo.getDeadline() == null || newTodo.getTitle() == null) {
                writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Missing title or deadline\"}");
                return;
            }
            newTodo.setCompleted(false);
            todoDAO.addTodo(newTodo);
            writeJsonResponse(resp, HttpServletResponse.SC_CREATED, "{\"message\": \"Todo created successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Failed to create Todo\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAutorized(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        try {
            List<Todo> todos = todoDAO.getAllTodos();
            String json = objectMapper.writeValueAsString(todos);
            writeJsonResponse(resp, HttpServletResponse.SC_OK, json);
        } catch (Exception e) {
            e.printStackTrace();
            writeJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\": \"Failed to fetch Todos\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAutorized(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Missing ID in path\"}");
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            Todo existingTodo = todoDAO.getTodoById(id);
            if (existingTodo == null) {
                writeJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "{\"error\": \"Todo not found\"}");
                return;
            }

            Todo updatedTodo = objectMapper.readValue(req.getReader(), Todo.class);
//            updatedTodo.printData();
            updatedTodo.setId(id); // id musi sie zgadzac
            if (updatedTodo.getTitle() == null || updatedTodo.getTitle().isEmpty()) {
                updatedTodo.setTitle(existingTodo.getTitle());
            }
            if (updatedTodo.getDeadline() == null) {
                updatedTodo.setDeadline(existingTodo.getDeadline());
            }
            System.out.println("maped todo: " + objectMapper.writeValueAsString(updatedTodo));

            boolean isUpdated = todoDAO.updateTodo(updatedTodo);
            if (isUpdated) {
                writeJsonResponse(resp, HttpServletResponse.SC_OK, "{\"message\": \"Todo updated successfully\"}");
            } else {
                writeJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "{\"error\": \"Todo not found\"}");
            }

        } catch (NumberFormatException e) {
            writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Invalid ID format\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            writeJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\": \"Database error\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAutorized(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Missing ID in path\"}");
                return;
            }

            int id = Integer.parseInt(pathInfo.substring(1));
            boolean isDeleted = todoDAO.deleteTodoById(id);

            if (isDeleted) {
                writeJsonResponse(resp, HttpServletResponse.SC_OK, "{\"message\": \"Todo deleted successfully\"}");
            } else {
                writeJsonResponse(resp, HttpServletResponse.SC_NOT_FOUND, "{\"error\": \"Todo not found\"}");
            }
        } catch (NumberFormatException e) {
            writeJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Invalid ID format\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            writeJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\": \"Database error\"}");
        }
    }

    private void writeJsonResponse(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(message);
    }

    private boolean isAutorized(HttpServletRequest req){
        String apiKey = req.getHeader("Authorization");
        return apiKey.equals(API_KEY);
    }
}

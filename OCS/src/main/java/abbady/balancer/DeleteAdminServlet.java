package abbady.balancer;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;



@WebServlet("/deleteUser")
public class DeleteAdminServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect("user.jsp");
            return;
        }

        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("admin.jsp?error=Username is required");
            return;
        }

        try {
            Connection conn = DBConnection.openConnection();

            String checkSql = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            if (!checkStmt.executeQuery().next()) {
                response.sendRedirect("admin.jsp?error=User not found");
                DBConnection.closeConnection(conn);
                return;
            }

            String sql = "DELETE FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();

            stmt.close();
            DBConnection.closeConnection(conn);

            if (rowsDeleted > 0) {
                response.sendRedirect("admin.jsp?success=User deleted successfully");
            } else {
                response.sendRedirect("admin.jsp?error=Failed to delete user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?error=Error while deleting user: " + e.getMessage());
        }
    }
}

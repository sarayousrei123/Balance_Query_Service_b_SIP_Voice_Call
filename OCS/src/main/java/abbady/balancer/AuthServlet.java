package abbady.balancer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServlet;


@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
    // private static final String DB_URL = "jdbc:postgresql://db:5432/balancer_all";
    // private static final String DB_USER = "postgres";
    // private static final String DB_PASSWORD = "123";
// genrations
// 1.1.0

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            try (Connection conn = DBConnection.openConnection()) {
                String sql = "SELECT username, role FROM users WHERE username = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String role = rs.getString("role");
                            String dbUsername = rs.getString("username");
                            
                            // Create session
                            HttpSession session = request.getSession();
                            session.setAttribute("username", dbUsername);
                            session.setAttribute("role", role);
                            session.setMaxInactiveInterval(60 * 60); // 60 minutes
                            
                            // Check user role and redirect accordingly
                            if (role != null && role.equals("admin")) {
                                response.sendRedirect("admin.jsp");
                            } else {
                                response.sendRedirect("user.jsp");
                            }
                        } else {
                            // Invalid credentials
                            request.setAttribute("error", "Invalid username or password");
                            request.getRequestDispatcher("login.jsp").forward(request, response);
                        }
                    }
                }
            }
        } catch (IOException | SQLException | ServletException e) {
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

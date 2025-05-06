package abbady.balancer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;


@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        String msisdn = request.getParameter("msisdn");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String balance = request.getParameter("balance");
       String newRole = request.getParameter("role");

        if (msisdn == null || msisdn.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            balance == null || balance.trim().isEmpty() ||
            newRole == null || newRole.trim().isEmpty()) 
        {
            response.sendRedirect("admin.jsp?error=MustEnterAllFaild");
            return;
        }

        try {
            Connection conn = DBConnection.openConnection();

            String checkSql = "SELECT msisdn FROM users WHERE msisdn = ? OR username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, msisdn);
            checkStmt.setString(2, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                response.sendRedirect("admin.jsp?error=AlreadyUsed");
                DBConnection.closeConnection(conn);
                return;
            }

            String sql = "INSERT INTO users (msisdn, username, password, balance, role) VALUES (?, ?, ?, ?, ?::user_role)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, msisdn);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setDouble(4, Double.parseDouble(balance)); 
           stmt.setString(5, newRole);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect("admin.jsp");
            } else {
                response.sendRedirect("admin.jsp?error=ErrorForThisUserCreate");
            }

            rs.close();
            stmt.close();
            DBConnection.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?error=ErrorForThisUserCreate: " + e.getMessage());
        }
    }
}
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


@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
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

        String username = request.getParameter("username");
        String newMsisdn = request.getParameter("newMsisdn");
        String newBalance = request.getParameter("newBalance");
        String newPassword = request.getParameter("newPassword");

        if (username == null || username.trim().isEmpty() || 
            newMsisdn == null || newMsisdn.trim().isEmpty() ||
            newBalance == null || newBalance.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty()) {
            response.sendRedirect("admin.jsp?error=All fields are required");
            return;
        }

        try {
            double balance = Double.parseDouble(newBalance);
            if (balance < 0) {
                response.sendRedirect("admin.jsp?error=Balance cannot be negative");
                return;
            }

            Connection conn = DBConnection.openConnection();

            // Check if user exists
            String checkSql = "SELECT username FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            if (!checkStmt.executeQuery().next()) {
                response.sendRedirect("admin.jsp?error=User not found");
                DBConnection.closeConnection(conn);
                return;
            }

            // Check if phone number already exists for another user
            String checkMsisdnSql = "SELECT msisdn FROM users WHERE msisdn = ? AND username != ?";
            PreparedStatement checkMsisdnStmt = conn.prepareStatement(checkMsisdnSql);
            checkMsisdnStmt.setString(1, newMsisdn);
            checkMsisdnStmt.setString(2, username);
            if (checkMsisdnStmt.executeQuery().next()) {
                response.sendRedirect("admin.jsp?error=Phone number already exists");
                DBConnection.closeConnection(conn);
                return;
            }

            // Update user data
            String sql = "UPDATE users SET msisdn = ?, balance = ?, password = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newMsisdn);
            stmt.setDouble(2, balance);
            stmt.setString(3, newPassword);
            stmt.setString(4, username);

            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            DBConnection.closeConnection(conn);

            if (rowsUpdated > 0) {
                response.sendRedirect("admin.jsp?success=User information updated successfully");
            } else {
                response.sendRedirect("admin.jsp?error=Failed to update user information");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("admin.jsp?error=Invalid balance format. Please enter a valid number");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?error=Error while updating user: " + e.getMessage());
        }
    }
}
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


@WebServlet("/user_dashboard")
public class UserDashboardServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String balance = "N/A";

        try {
            Connection conn = DBConnection.openConnection();

            String sql = "SELECT balance, msisdn FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                balance = rs.getString("balance");
                String msisdn = rs.getString("msisdn");
                session.setAttribute("msisdn", msisdn);
                session.setAttribute("balance", balance);
            }

            rs.close();
            stmt.close();
            DBConnection.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("user.jsp").forward(request, response);
    }
}

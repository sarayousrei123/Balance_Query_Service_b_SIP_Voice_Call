package abbady.balancer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@WebServlet("/balance")
public class BalanceAPI extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msisdn = request.getParameter("msisdn");

        String balance = "0";

        try (Connection conn = DBConnection.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM users WHERE TRIM(msisdn) = ?");
            stmt.setString(1, msisdn.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                balance = rs.getString("balance");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.write("{\"msisdn\": \"" + msisdn + "\", \"balance\": \"" + balance + "\"}");
                out.close();
            } else {
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.write("No record found for " + msisdn);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//protected void doPost(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//    String msisdn = request.getParameter("msisdn");
//    String balanceStr = request.getParameter("balance");
//
//    try {
//       
//
//        balanceStr = balanceStr.trim();
//        BigDecimal balance = new BigDecimal(balanceStr);
//
//        try (Connection conn = DBConnection.openConnection()) {
//            PreparedStatement stmt = conn.prepareStatement(
//                "INSERT INTO users (msisdn, balance) VALUES (?, ?) "
//                + "ON CONFLICT (msisdn) DO UPDATE SET balance = users.balance + EXCLUDED.balance"
//            );
//            stmt.setString(1, msisdn.trim());
//            stmt.setBigDecimal(2, balance);
//            int rowsAffected = stmt.executeUpdate();
//
//          
//        }
//    } catch (NumberFormatException e) {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    } catch (Exception e) {
//        e.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//    }
//}
//
//
//   protected void doPut(HttpServletRequest request, HttpServletResponse response)
//        throws ServletException, IOException {
//    String msisdn = request.getParameter("msisdn");
//    String balanceStr = request.getParameter("balance");
//
//    try {
//       
//        balanceStr = balanceStr.trim();
//        BigDecimal balanceToAdd = new BigDecimal(balanceStr);
//
//        BigDecimal currentBalance = BigDecimal.ZERO;
//
//        try (Connection conn = DBConnection.openConnection()) {
//            PreparedStatement selectStmt = conn.prepareStatement(
//                    "SELECT balance FROM users WHERE TRIM(msisdn) = ?");
//            selectStmt.setString(1, msisdn.trim());
//            ResultSet rs = selectStmt.executeQuery();
//
//            if (rs.next()) {
//                currentBalance = rs.getBigDecimal("balance");
//            } else {
//                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                response.getWriter().write("User not found.");
//                return;
//            }
//
//            BigDecimal newBalance = currentBalance.add(balanceToAdd);
//
//            PreparedStatement updateStmt = conn.prepareStatement(
//                    "UPDATE users SET balance = ? WHERE msisdn = ?");
//            updateStmt.setBigDecimal(1, newBalance);
//            updateStmt.setString(2, msisdn.trim());
//            int rowsUpdated = updateStmt.executeUpdate();
//
//            
//        }
//    } catch (NumberFormatException e) {
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//    } catch (Exception e) {
//        e.printStackTrace();
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//    }
//}
//
//
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String msisdn = request.getParameter("msisdn");
//
//        try (Connection conn = DBConnection.openConnection()) {
//            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE msisdn = ?");
//            stmt.setString(1, msisdn.trim());
//            stmt.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
}

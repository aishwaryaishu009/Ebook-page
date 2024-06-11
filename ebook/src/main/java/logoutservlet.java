import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/logoutservlet")
public class logoutservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/ebook";
        String user = "root";
        String pass = "mani"; // Change to your MySQL password

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to reset is_current_user to 0 for all users
            String resetSql = "UPDATE users SET is_current_user = 0";
            pstmt = conn.prepareStatement(resetSql);
            pstmt.executeUpdate();

            // Redirect to logout success page
            response.sendRedirect("login.html");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Redirect to error page
            response.sendRedirect("error.html");
        } finally {
            // Close resources
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}

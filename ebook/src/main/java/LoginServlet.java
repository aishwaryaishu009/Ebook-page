import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/ebook";
        String user = "root";
        String pass = "mani"; // Change to your MySQL password

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to check if the username and password exist in the database
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // User authenticated, set is_current_user to 1
                String updateSql = "UPDATE users SET is_current_user = 1 WHERE username = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, username);
                updateStmt.executeUpdate();

                // Redirect to homepage or any other page
                response.sendRedirect("homepage.html");
            } else {
                // Authentication failed, display error message
                out.println("<h2>Login failed. Please check your username and password.</h2>");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Redirect to error page
            response.sendRedirect("error.html");
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            out.close();
        }
    }
}

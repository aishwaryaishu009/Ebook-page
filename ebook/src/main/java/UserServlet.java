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

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to retrieve user details of the currently logged-in user
            String sql = "SELECT * FROM users WHERE is_current_user = 1";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // Display user details
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>User Details</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>User Details</h2>");
            out.println("<div>");

            if (rs.next()) {
                out.println("<p><strong>ID:</strong> " + rs.getInt("id") + "</p>");
                out.println("<p><strong>Name:</strong> " + rs.getString("name") + "</p>");
                out.println("<p><strong>Username:</strong> " + rs.getString("username") + "</p>");
                out.println("<p><strong>Email:</strong> " + rs.getString("email") + "</p>");
                out.println("<p><strong>Phone:</strong> " + rs.getString("phone") + "</p>");
                out.println("<p><strong>Date of Birth:</strong> " + rs.getDate("dob") + "</p>");
                out.println("<p><strong>Signup Date:</strong> " + rs.getDate("signup_date") + "</p>");
                // You might not want to display the password for security reasons
            } else {
                out.println("<p>User not found</p>");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Handle exceptions
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            out.close();
        }
    }
}

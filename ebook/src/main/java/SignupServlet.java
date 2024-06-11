import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve form data
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob");
        String signupDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/ebook";
        String user = "root";
        String pass = "mani";

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, pass);

            // SQL query to insert user details into the users table
            String sql = "INSERT INTO users (name, username, email, phone, dob, signup_date, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Create a prepared statement
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, dob);
            statement.setString(6, signupDate);
            statement.setString(7, password);

            // Execute the query
            statement.executeUpdate();

            // Close the database connection
            conn.close();

            // Redirect to success page
            response.sendRedirect("success.html");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // Redirect to error page
            response.sendRedirect("error.html");
        }
    }
}

package crud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DisplayWeather")
public class DisplayWeatherServletData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JDBC credentials (same as MyServlet)
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/mydata";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASS = "tiger";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Weather Data</title></head><body>");
        out.println("<h2>All Weather Records</h2>");
        out.println("<a href='index.jsp'>Back to Weather Form</a><br><br>");

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM weather ORDER BY id DESC";
            ResultSet rs = stmt.executeQuery(sql);

            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>City</th>");
            out.println("<th>Date</th>");
            out.println("<th>Temperature (°C)</th>");
            out.println("<th>Condition</th>");
            out.println("<th>Humidity (%)</th>");
            out.println("<th>Wind Speed</th>");
            out.println("<th>Action</th>"); // New column
            out.println("</tr>");

            while (rs.next()) {
            		int id = rs.getInt("id");
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("city") + "</td>");
                out.println("<td>" + rs.getString("date") + "</td>");
                out.println("<td>" + rs.getInt("temperature") + "</td>");
                out.println("<td>" + rs.getString("condition") + "</td>");
                out.println("<td>" + rs.getInt("humidity") + "</td>");
                out.println("<td>" + rs.getDouble("wind_speed") + "</td>");
             // ADD Delete link
                out.println("<td><a href='DeleteData?id=" + id + "' onclick='return confirm(\"Are you sure?\");'>Delete</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error fetching data: " + e.getMessage() + "</p>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<p>PostgreSQL Driver not found!</p>");
        }

        out.println("</body></html>");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // forward POST to GET
    }
}
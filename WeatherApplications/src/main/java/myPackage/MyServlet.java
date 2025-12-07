package myPackage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MyServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String apiKey = "56ab6d4bfb438af99d7a49199286f381";
		String cityParam = request.getParameter("city");

		// Check if city is provided
		if (cityParam == null || cityParam.isEmpty()) {
			request.setAttribute("error", "City parameter is missing.");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		String city = URLEncoder.encode(cityParam, StandardCharsets.UTF_8);
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

		HttpURLConnection connection = null;
		InputStreamReader reader = null;

		try {
			URL url = new URL(apiUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// Check the response code first
			int status = connection.getResponseCode();
			if (status >= 200 && status < 300) {
				reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
			} else {
				reader = new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);
			}

			StringBuilder responseContent = new StringBuilder();
			Scanner scanner = new Scanner(reader);
			while (scanner.hasNext()) {
				responseContent.append(scanner.nextLine());
			}
			scanner.close();

			// Parse JSON only if status is OK
			if (status >= 200 && status < 300) {
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);

				long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
				String date = new Date(dateTimestamp).toString();

				double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
				int temperatureCelsius = (int) (temperatureKelvin - 273.15);

				int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
				double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();

				String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main")
						.getAsString();

				request.setAttribute("date", date);
				request.setAttribute("city", cityParam);
				request.setAttribute("temperature", temperatureCelsius);
				request.setAttribute("weatherCondition", weatherCondition);
				request.setAttribute("humidity", humidity);
				request.setAttribute("windSpeed", windSpeed);
				request.setAttribute("weatherData", responseContent.toString());

				// --- JDBC Insert ---
				saveWeatherToDatabase(cityParam, date, temperatureCelsius, weatherCondition, humidity, windSpeed);

			} else {
				// API returned an error
				request.setAttribute("error",
						"Could not fetch weather for '" + cityParam + "'. API returned status: " + status);
			}

		} catch (IOException e) {
			e.printStackTrace();
			request.setAttribute("error", "Error connecting to weather API: " + e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (reader != null) {
				reader.close();
			}
		}

		// Forward to JSP
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	private void saveWeatherToDatabase(String city, String date, int temperature, String condition, int humidity,
			double windSpeed) {

		String url = "jdbc:postgresql://localhost:5432/mydata";
		String un = "postgres";
		String pwd = "tiger";
		String Driver = "org.postgresql.Driver";

		try {

			Class.forName(Driver);
			Connection conn = DriverManager.getConnection(url, un, pwd);

			String sql = "INSERT INTO weather (city, date, temperature, condition, humidity, wind_speed) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);

			stmt.setString(1, city); // city name
			stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis())); // current date/time
			stmt.setInt(3, temperature); // temperature in Celsius
			stmt.setString(4, condition); // weather description
			stmt.setInt(5, humidity); // humidity
			stmt.setDouble(6, windSpeed); // wind speed

			stmt.executeUpdate();

			System.out.println("Data inserted successfully...");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

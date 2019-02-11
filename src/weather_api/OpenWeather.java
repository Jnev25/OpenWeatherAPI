package weather_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.json.*;


/**
 * Simple program that takes in user input in the form of country, city, and if country is the US takes a state. Program will connect 
 * to api and return current weather for that city.  Country should be entered  as two letters, but can take usa for United States. 
 * City has no restrictions on input. States have to be entered as two letters.
 * @author Jason Neville
 *
 */
public class OpenWeather {

	public static void main(String[] args) {
	
	String myKey = "ada5a3a8c812cfc368f908ece70dcf37";
	String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
	String midUrl = "&appid=";
	Scanner in = new Scanner(System.in);
	String country = "";
	String city = "";
	String state = "";
	double tempK;
	double tempF;
	
	System.out.println("Please enter country name in the form \"us\" or \"usa\" for United States.");
	System.out.println("All other countries should be entered as two letters.");
	System.out.println("States in the US should be entered as two letters.");
	
	while (country == "") {
		System.out.print("Enter a country: ");         //use "us" or "usa" for United States, two letter acronyms for any other country
		String input = in.next();
		if (input.length() == 2 || input.equals("usa".toLowerCase())) {
			country = input.toLowerCase();
		}
		else {
			System.out.println("Please enter a valid country.");
		}
	}
	while (city == "") {
		System.out.print("Enter a city: ");
		String input = in.next();
		if (!input.equals("")) {
			city = input.toLowerCase();
		}
		else {
			System.out.println("Please enter a valid city.");
		}
	}
	
	if (country.equals("us") || country.equals("usa")) {
		
		while (state == "") {
			System.out.print("Enter a state:");
			String input = in.next();
			if (input.length() == 2) {
				state = input;
			}
			else {
				System.out.println("Please enter a valid state.");
			}
		}	
	}
	
	in.close();
	
	String fullUrl = "";
	
	if (country.equals("us") || country.equals("usa")) {
		fullUrl = baseUrl + city + "," + state + "," + country + midUrl + myKey;
		System.out.println(fullUrl);
	}
	
	else {
		fullUrl = baseUrl + city + "," + country + midUrl + myKey;
	}
	
	URL url;
	HttpURLConnection con = null;
	
	
	try {
		url = new URL(fullUrl);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application-json");
		con.connect();
		
		
		if(con.getResponseCode()==201 || con.getResponseCode()==200)
        {
            System.out.println("Connected to Api.");
            System.out.println("Getting weather for " + city.substring(0, 1).toUpperCase() + city.substring(1) + ".");
        }
		else {
			System.err.println("Could not connect to api.");
			con.disconnect();
			System.exit(0);
		}
		
		BufferedReader output = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String line = "";
		StringBuffer response = new StringBuffer();
		while ((line = output.readLine()) != null) {
			response.append(line);
		}
	
		
		output.close();
		JSONObject json = new JSONObject(response.toString());
		JSONObject main = json.getJSONObject("main");
		tempK = main.getDouble("temp");
		
		tempF = (tempK - 273.15) * (9/5) + 32;
		System.out.println("The temperature in " + city.substring(0, 1).toUpperCase() + city.substring(1) + " is " + String.format("%.2f", tempF) + " degrees farhenheit.");
		
	}
	catch (MalformedURLException e) {
		e.printStackTrace();
	}
	catch (IOException t) {
		t.printStackTrace();
	}
	catch (JSONException j) {
		j.printStackTrace();
	}
	finally {
		con.disconnect();
	}
	
	
	}
}

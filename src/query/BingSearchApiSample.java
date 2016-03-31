package query;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import utils.PropertiesLoader;

public class BingSearchApiSample {

final static int QUERY = 10;

	public static void querySender(PrintWriter writer, String persona, int offset) throws Exception {
	
		Properties prop = PropertiesLoader.loadPropertiesFile();
		
		final String accountKey = prop.getProperty("accountKey");

		final String bingUrlPattern = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%%27%s%%27&$format=JSON&$skip="+offset;

		final String query = URLEncoder.encode(persona, Charset.defaultCharset().name());
		final String bingUrl = String.format(bingUrlPattern, query);

		final String accountKeyEnc = Base64.encodeBase64URLSafeString(((accountKey + ":" + accountKey).getBytes()));
		//qui err
		final URL url = new URL(bingUrl);
		final URLConnection connection = url.openConnection();
		connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String inputLine;
			final StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);	
			}
			final JSONObject json = new JSONObject(response.toString());
			final JSONObject d = json.getJSONObject("d");
			final JSONArray results = d.getJSONArray("results");
			final int resultsLength = results.length();
			for (int i = 0; i < resultsLength; i++) {
				final JSONObject aResult = results.getJSONObject(i);
				writer.println(aResult.get("Url")+ " " + persona);
			}
		}
	}
	public static void main(String[]args) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("dataset.txt", "UTF-8");
		

		try (BufferedReader br = new BufferedReader(new FileReader("persone.txt")))
		{

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				String persona = sCurrentLine;


				for(int i=0; i<QUERY; i++) { //query a persona

					try {
						querySender(writer, persona.trim().toLowerCase(), i*50);//offset
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.close();

	}
}
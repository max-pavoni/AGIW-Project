package parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import utils.PropertiesLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;

public class Html2Json {


	//dobbiamo leggere gli html e trasformarli in Page (JSON)

	public static void visit(String path) {
		File root = new File(path);
		File[] files = root.listFiles();
		if(files==null) return;

		for(File f : files){
			if(f.isDirectory()){
				visit(f.getAbsolutePath());
			}
			else{
				Page p = htmlToPage(f);
				page2JSON(p);
			}
		}
	}


	public static void page2JSON (Page p) {
		
		Properties prop = PropertiesLoader.loadPropertiesFile();

		try {
			//	Nome dello script da creare, spazio bianco sostituito con '_'
			String filepath = prop.getProperty("peoplePath") + p.getNome_Persona().replace(" ","_")+".sh";
			
			//	Il file index.txt non viene considerato
			if(!filepath.equals(prop.getProperty("peoplePath") + "index.txt.sh")){
				//	Pattern del comando curl seguito dai campi dell'oggetto Page p, una volta sostituite le virgolette
				FileWriter writer_Json = new FileWriter(filepath, true );
				writer_Json.write("curl -POST 'http://localhost:9200/people/person/' -d '");
				writer_Json.write("{\n"+
						"title : " + "\""+p.getTitle().replace("\"", "").replace("\'", "") +"\",\n"+ 
						"body : "+ "\"" + p.getBody().replace("\"", "").replace("\'", "") + "\",\n" +
						"path : "+ "\"" + p.getUrl().replace("\"", "").replace("\'", "") + "\"\n" +
						"}'\n");
				//writer_Json.write("'\n");
				writer_Json.close();
			}
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}




	@SuppressWarnings("finally")
	public static Page htmlToPage(File f) {

		Document doc;
		Page p = new Page();
		try {
			doc = Jsoup.parse(f, "UTF-8");
			String title = doc.getElementsByTag("title").text();
			String body = doc.body().text();
			p.setBody(body);
			p.setTitle(title);
			p.setUrl(f.getAbsolutePath());
			p.setNome_Persona(f.getName().replace(".html", "").trim().replace("[", "").replace("]", "").replaceAll("\\d", ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		finally{
			return p;
		}
	}

	public static void main(String[] args) {

		Properties prop = PropertiesLoader.loadPropertiesFile();
		
		visit(prop.getProperty("peoplePath"));

	}
}





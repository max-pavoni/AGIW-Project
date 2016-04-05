package parse;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.PropertiesLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Html2Json {

	private static Properties prop = PropertiesLoader.loadPropertiesFile();
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


	//	public static void page2JSON (Page p) {
	//
	//		Properties prop = PropertiesLoader.loadPropertiesFile();
	//
	//		try {
	//			//	Nome dello script da creare, spazio bianco sostituito con '_'
	//			String filepath = prop.getProperty("peoplePath") + p.getNome_Persona().replace(" ","_")+".sh";
	//
	//			//	Il file index.txt non viene considerato
	//			if(!filepath.equals(prop.getProperty("peoplePath") + "index.txt.sh")){
	//				//	Pattern del comando curl seguito dai campi dell'oggetto Page p, una volta sostituite le virgolette
	//				FileWriter writer_Json = new FileWriter(filepath, true );
	//				writer_Json.write("curl -POST 'http://localhost:9200/people/page/' -d '");
	//				writer_Json.write("{\n"+
	//						"title : " + "\""+p.getTitle().replace("\"", "").replace("\'", "").replace("\\", "").replace("/", "") +"\",\n"+ 
	//						"body : "+ "\"" + p.getBody().replace("\"", "").replace("\'", "").replace("\\", "") + "\",\n" +
	//						"path : "+ "\"" + p.getUrl().replace("\"", "").replace("\'", "") + "\"\n" +
	//						"}'\n"); 
	//				//writer_Json.write("'\n");
	//				writer_Json.close();
	//			}
	//		}
	//		catch (Exception e){
	//			System.out.println(e.getMessage());
	//		}
	//	}
	public static void page2JSON (Page p) {

		Properties prop = PropertiesLoader.loadPropertiesFile();
		JSONObject obj = new JSONObject();

		try {
			//  Nome dello script da creare, spazio bianco sostituito con '_'
			String filepath = prop.getProperty("peoplePath") + p.getNome_Persona().replace(" ","_")+".sh";
			if(!p.getBody().contains("PDF-")){
				obj.put("title", p.getTitle().replace("\'", ""));
				obj.put("body", p.getBody().replace("\'", ""));
				obj.put("path", p.getUrl());


				//  Il file index.txt non viene considerato
				if(!filepath.equals(prop.getProperty("peoplePath") + "index.txt.sh")){
					//  Pattern del comando curl seguito dai campi dell'oggetto Page p, una volta sostituite le virgolette
					FileWriter writer_Json = new FileWriter(filepath, true );
					writer_Json.write("curl -POST 'http://localhost:9200/people/page/' -d '");
					writer_Json.write(obj.toString() + "'\n");
					writer_Json.close();
				}
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
			if(isItalian(f.getName().replace(".html", "").trim()) || doc.getElementsByTag("html").attr("xml:lang").toLowerCase().equals("it-it") ||doc.getElementsByTag("html").attr("xml:lang").toLowerCase().equals("it") ||
					doc.getElementsByTag("html").attr("lang").toLowerCase().equals("it-it") || 
					doc.getElementsByTag("html").attr("lang").toLowerCase().equals("it") ||
					(doc.getElementsByTag("meta").attr("name").equals("language") && doc.getElementsByTag("meta").attr("content").toLowerCase().equals("italian"))){

				String title = doc.getElementsByTag("title").text();
				String body = doc.body().text();
				p.setBody(body);
				p.setTitle(title);
				String path = f.getAbsolutePath();
				path= path.substring(path.indexOf("people"));
				p.setUrl("http://localhost:80/" + path);
				p.setNome_Persona(f.getName().replace(".html", "").trim().replace("[", "").replace("]", "").replaceAll("\\d", ""));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		finally{
			return p;
		}
	}
	public static boolean isItalian(String nome){
		Properties prop = PropertiesLoader.loadPropertiesFile();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(prop.getProperty("peoplePath")+"index.txt"));
			String line;
			while ((line=br.readLine())!=null){
				try{
					if(line.contains(nome)&& line.contains(".it/")){
						return true;
					}
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
			}
			br.close();
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;

	}


	public static void main(String[] args) {

		Properties prop = PropertiesLoader.loadPropertiesFile();

		visit(prop.getProperty("peoplePath"));

	}
}





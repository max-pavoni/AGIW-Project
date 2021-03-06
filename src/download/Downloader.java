package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import utils.PropertiesLoader;

public class Downloader {

	public static void main(String[] args) throws IOException{
		BufferedReader br1;

		Properties prop = PropertiesLoader.loadPropertiesFile();

		
		br1 = new BufferedReader(new FileReader("dataset.txt"));
		PrintWriter index_Writer = new PrintWriter(prop.getProperty("peoplePath") + "index.txt", "UTF-8");;
		URL url = null;
		InputStream is = null;
		BufferedReader br;
		String line;
		String sCurrentLine;
		String nome_cartella_Persona;
		String link = null;
		File f=null;
		int cont = 1;
		int pagine_saltate = 0;
		PrintWriter writer;


		//apro lo stream sull'index
		while((sCurrentLine = br1.readLine())!=null)
			try {
				link=sCurrentLine.split(" ")[0];
				url = new URL (link); 
				is = url.openStream();



				// throws an IOException
				br = new BufferedReader(new InputStreamReader(is));
				nome_cartella_Persona = sCurrentLine.substring(link.length()).trim();
				f = new File(prop.getProperty("peoplePath") + nome_cartella_Persona.toUpperCase());

				if(!f.exists()){
					cont=1;
					f.mkdir();
				}

				//apro lo stream su un nuovo file html in cui verrà salvata la paginaprop.getProperty("peoplePath") + nome_cartella_Persona.toUpperCase() + "/" + nome_cartella_Persona.toUpperCase() +"[" + cont + "].htm
				writer = new PrintWriter(prop.getProperty("peoplePath") + nome_cartella_Persona.toUpperCase() + "/" + nome_cartella_Persona.toUpperCase() +"[" + cont + "].html", "UTF-8" );
				
				

				//leggo il documento html, e scrivo ogni riga sul file html generato prima
				while ((line = br.readLine()) != null) {
					writer.println(line);
				}
				
				File fp = new File(prop.getProperty("peoplePath") + nome_cartella_Persona.toUpperCase() + "/" + nome_cartella_Persona.toUpperCase() +"[" + cont + "].html");

				if(fp.length()==0){
					fp.delete();
					is.close();
					br.close();
					writer.close();
				}
				else{
					//scrivo una nuova riga sull'index
					index_Writer.println(nome_cartella_Persona.toUpperCase()+"["+cont+"] "+link);
					index_Writer.flush();
					cont++;

					is.close();
					br.close();
					writer.close();
				}
				
				
			} catch (MalformedURLException mue) {
				mue.getMessage();
			} catch (IOException ioe) {
				ioe.getMessage();
				pagine_saltate++;
			} finally {
				try {
					if (is != null) is.close();
				} catch (IOException ioe) {
					// nothing to see here
				}
			}
		br1.close();
		//alla fine chiudo lo stream sull'index
		index_Writer.close();
		System.out.println("PAGINE SALTATE: " + pagine_saltate);

	}
}





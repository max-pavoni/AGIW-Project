package parse;

public class Page {
	private String title;
	private String url;
	private String body;
	private String nome_Persona;
	public Page(){
		
	}
	public Page(String title, String url, String body, String nome_Persona){
		this.title = title;
		this.url = url;
		this.body = body;
		this.nome_Persona = nome_Persona;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getNome_Persona() {
		return nome_Persona;
	}
	public void setNome_Persona(String nome_Persona) {
		this.nome_Persona = nome_Persona;
	}
}

package Analizadores;

public class FormaLargaWithAc {
	
	String formaLarga;
	String acronimo;
	public FormaLargaWithAc() {		
	}
	
	public void clean() {
		this.acronimo="";
		this.formaLarga="";
	}
	public String getFormaLarga() {
		return formaLarga;
	}
	public void setFormaLarga(String formaLarga) {
		this.formaLarga = formaLarga;
	}
	public String getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	
	
}
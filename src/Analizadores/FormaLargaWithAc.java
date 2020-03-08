package Analizadores;

public class FormaLargaWithAc {
	
	String formaLarga;
	//String acronimo;
	private Acronimo acronimo;
	
	public FormaLargaWithAc() {
		
	}
	public FormaLargaWithAc(Acronimo ac, String lf) {
		this.acronimo=ac;
		this.formaLarga=lf;
	}
	
	public void clean() {
		this.acronimo=new Acronimo();
		this.formaLarga="";
	}
	public String getFormaLarga() {
		return formaLarga;
	}
	public void setFormaLarga(String formaLarga) {
		this.formaLarga = formaLarga;
	}
	public Acronimo getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(Acronimo acronimo) {
		this.acronimo = acronimo;
	}
	
	
}

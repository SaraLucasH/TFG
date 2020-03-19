package Analizadores;

public class FormaLargaWithAc {
	
	private FormaLarga formaLarga;
	//String acronimo;
	private Acronimo acronimo;
	
	public FormaLargaWithAc() {
		
	}
	public FormaLargaWithAc(Acronimo ac, FormaLarga lf) {
		this.acronimo=ac;
		this.formaLarga=lf;
	}
	
	public void clean() {
		this.acronimo=new Acronimo();
		this.formaLarga=new FormaLarga();
	}
	public FormaLarga getFormaLarga() {
		return formaLarga;
	}
	public void setFormaLarga(FormaLarga formaLarga) {
		this.formaLarga = formaLarga;
	}
	public Acronimo getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(Acronimo acronimo) {
		this.acronimo = acronimo;
	}
	
	
}

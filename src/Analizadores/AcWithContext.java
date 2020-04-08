package Analizadores;

public class AcWithContext {
	private Acronimo ac;
	private String fraseInmediata;
	
	public AcWithContext() {
		this.clean();
	}
	
	public void clean() {
		this.ac=new Acronimo();
		this.fraseInmediata="";
	}
	
	public AcWithContext(Acronimo ac, String fraseInmediata) {
		this.ac = ac;
		this.fraseInmediata = fraseInmediata;
	}

	public Acronimo getAc() {
		return ac;
	}

	public void setAc(Acronimo ac) {
		this.ac = ac;
	}

	public String getFraseInmediata() {
		return fraseInmediata;
	}

	public void setFraseInmediata(String fraseInmediata) {
		this.fraseInmediata = fraseInmediata;
	}
}

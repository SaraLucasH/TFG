package Analizadores;

public class AcWithContext {
	private Acronimo ac;
	private String fraseInmediata;
	
	public AcWithContext(Acronimo ac, String fraseInmediata) {
		this.ac = ac;
		this.fraseInmediata = fraseInmediata;
	}

	public Acronimo getAc() {
		return ac;
	}

	public String getFraseInmediata() {
		return fraseInmediata;
	}

}

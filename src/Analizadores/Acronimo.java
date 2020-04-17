package Analizadores;

public class Acronimo {
	
	private int startOffset;
	private int endOffset;
	private String acronimo;
	private boolean nested;
	
	public Acronimo(int startOffset, int endOffset, String acronimo) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.acronimo = acronimo;
		this.nested=false;
	}
	
	public boolean isNested() {
		return nested;
	}

	public void setNested(boolean nested) {
		this.nested = nested;
	}

	public int getStartOffset() {
		return startOffset;
	}
	
	public int getEndOffset() {
		return endOffset;
	}
	
	public String getAcronimo() {
		return acronimo;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + acronimo.hashCode();  
        return result;
    }
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Acronimo aux = (Acronimo) obj;
		return aux.getAcronimo().equals(this.getAcronimo())&& aux.getStartOffset()==this.getStartOffset();
	}
}

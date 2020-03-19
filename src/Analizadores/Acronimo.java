package Analizadores;

public class Acronimo {
	
	private int startOffset;
	private int endOffset;
	private String acronimo;
	private boolean nested;
	
	
	public Acronimo(int startOffset, int endOffset, String acronimo) {
		super();
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.acronimo = acronimo;
		this.nested=false;
	}
	
	public Acronimo() {
		
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
	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}
	public int getEndOffset() {
		return endOffset;
	}
	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}
	public String getAcronimo() {
		return acronimo;
	}
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
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

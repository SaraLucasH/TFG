package Analizadores;

public class FormaLarga {
	private String lf;
	private int startOffset;
	private int endOffset;
	private boolean nested;
	
	public FormaLarga(String lf, int startOffset, int endOffset) {
		this.lf = lf;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.nested=false;
	}

	public boolean isNested() {
		return nested;
	}

	public void setNested(boolean nested) {
		this.nested = nested;
	}
	
	public String getLf() {
		return lf;
	}

	public void setLf(String lf) {
		this.lf = lf;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}
	
	public static int getStart(FormaLarga lf,String checked) {
		int pos=lf.getLf().indexOf(checked);
		if(pos!=-1 && lf.getLf().indexOf(checked, lf.getLf().indexOf(checked)+1)==-1){
			//Pertenece y solo hay una ocurrencia
			return pos+lf.getStartOffset();
		}
		return lf.getStartOffset();
	}
	
	public static int getEnd(FormaLarga lf,String checked) {
		int pos=lf.getLf().lastIndexOf(checked);
		if(pos!=-1){
			//Pertenece y solo hay una ocurrencia
			return pos+lf.getStartOffset()+checked.length();
		}		
		return lf.getEndOffset();
	}
	
}

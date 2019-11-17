package Diccionarios;
public class main {

	public static void main(String []args){
		CargaDiccionario c= new CargaDiccionario();
		c.cargaDiccionarioLF_SFTXT("C:/Users/saral/Documents/SARA/URJC/CUARTO/TFG/SOTO_ArchivosAdjuntos/allAcronyms.txt");
		//ExtraerDatosTsv e= new ExtraerDatosTsv();
		//e.transformarATxtDiccionario("C:/Users/saral/Documents/SARA/URJC/CUARTO/TFG/BARR2/BARR_ibereval_sample_set/BARR_ibereval_sample_relations.tsv");
		//e.transformarATxtDiccionario("C:/Users/saral/Documents/SARA/URJC/CUARTO/TFG/BARR2/BARR_ibereval_training2_set/BARR_ibereval_training2_relations.tsv");
	}
}

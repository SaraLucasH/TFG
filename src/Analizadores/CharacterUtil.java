package Analizadores;

/**
 * Clean accents
 * 
 * @author Soto
 *
 */
public class CharacterUtil {

	public static char clean(char c) {
		char result = c;
		switch (c) {
		case 'á':
			result = 'a';
			break;
		case 'é':
			result = 'e';
			break;
		case 'í':
			result = 'i';
			break;
		case 'ó':
			result = 'o';
			break;
		case 'ú':
			result = 'u';
			break;
		}
		return result;
	}

}

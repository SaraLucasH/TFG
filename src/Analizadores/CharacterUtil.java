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
		case '�':
			result = 'a';
			break;
		case '�':
			result = 'e';
			break;
		case '�':
			result = 'i';
			break;
		case '�':
			result = 'o';
			break;
		case '�':
			result = 'u';
			break;
		}
		return result;
	}

}

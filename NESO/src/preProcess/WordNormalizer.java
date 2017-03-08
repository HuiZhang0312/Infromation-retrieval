package preProcess;

import classes.Stemmer;

public class WordNormalizer {
	public Stemmer s;

	public char[] lowercase(char[] chars) {
		// Transform the uppercase characters in the word to lowercase
		for (int i = 0; i < chars.length; i++) {
			chars[i] = Character.toLowerCase(chars[i]);
		}
		return chars;
	}

	public String stem(char[] chars) {
		// Use the stemmer in Classes package to do the stemming on input word,
		// and return the stemmed word
		s = new Stemmer();
		s.add(chars, chars.length);
		s.stem();
		String str = s.toString();
		return str;
	}
}

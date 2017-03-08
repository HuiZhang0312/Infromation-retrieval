package preProcess;

public class WordTokenizer {
	public char[] texts;
	// Use a pointer to record the last postion of the word
	int position = 0;

	public WordTokenizer(char[] texts) {
		// This constructor will tokenize the input texts (usually it is a char
		// array for a whole document)
		this.texts = texts;
	}

	public char[] nextWord() {
		// Read and return the next word of the document
		// or return null if it is the end of the document
		StringBuffer buffer = new StringBuffer();

		// The method below is to find the word in the text
		while (position < texts.length) {
			// Keep finding until it finds a letter
			if (!Character.isLetter(texts[position]))
				position++;
			else {
				// If the char at this position is a letter, find all the
				// letters following it and put the chars into StringBuffer
				while (Character.isLetter(texts[position])) {
					buffer = buffer.append(texts[position]);
					position++;
					if(position == texts.length){
						break;
					}
				}
				// The StringBuffer will return a word every time
				return buffer.toString().toCharArray();
			}
		}
		return null;
	}

}

package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.text.Font;

public class Word {
	
	static String[] words = null;
    static List<String> wordList = new ArrayList<String>();
    static {try {
        @SuppressWarnings("resource")
		BufferedReader buffer = new BufferedReader(new FileReader(new File("").getAbsolutePath() + "/res/words.txt"));
        String line;
        while ((line = buffer.readLine()) != null) {
            line = line.trim();
            if ((line.length()!=0)) {
                wordList.add(line);
            }
        }
        words = (String[])wordList.toArray(new String[wordList.size()]);
    } catch (Exception e) {
		e.printStackTrace();
	}};
	static String word;
	static String arrow = "⇦⇨⇧⇩";

	public Word(){
		word = words[new Random().nextInt(words.length)];
	}
	
	public Word(String w){
		word = w;
	}
	
	@SuppressWarnings("static-access")
	public static String randomWord(MyController myController){
		if ((new Random().nextBoolean() || myController.wordMode == 2) && myController.wordMode != 1) {
			myController.wordText.setFont(new Font(32));
			word = "";
			for (int i = 0; i < 8; ++i) {
				word += arrow.charAt(new Random().nextInt(4));
			}
			return word;
		} else {
			myController.wordText.setFont(new Font(25));
			return words[new Random().nextInt(words.length)];
		}
	}

}
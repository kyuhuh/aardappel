package nl.windesheim.capturetheclue.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter on 3/15/2016.
 */
public class WordManager {

    private ArrayList<String> wordlist;
    Random rand;
    List<String> alphabet =
            new ArrayList<String>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "z"));


    public WordManager() {
        rand = new Random();
        wordlist = new ArrayList<String>();
        wordlist.add("treehouse");
        wordlist.add("hairextension");
        wordlist.add("aboriginal");
    }

    public String getRandomWord() {
        int i = wordlist.size();
        int n = rand.nextInt(i);
        return wordlist.get(n);
    }

    public List<String> getWordLetters(String word, int length) {
        int wordLength = word.length();
        ArrayList<String> letters = new ArrayList<>();

        for (int i = 0; i < wordLength; i++) {
            String x = String.valueOf(word.charAt(i));
            letters.add(x);
        }

        if (wordLength <= length) {
            int difference = length - wordLength;
            while (difference > 0) {
                letters.add(alphabet.get(rand.nextInt(23)));
                difference--;
            }
        }
        return shuffle(letters);
    }

    public List<String> shuffle(List<String> letters) {

        Collections.shuffle(letters);
        return letters;
    }
}

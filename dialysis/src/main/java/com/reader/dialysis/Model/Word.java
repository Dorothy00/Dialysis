package com.reader.dialysis.Model;

import java.util.List;

/**
 * Created by dorothy on 15/5/16.
 */
public class Word {
    private String word;
    private List<Definition> results;
    private Pronunciation pronunciation;

    public class Definition{
        String definition;
        String partOfSpeech;
        List<String> examples;

        public String getDefinition() {
            return definition;
        }

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public List<String> getExamples() {
            return examples;
        }
    }

    public class Pronunciation{
        String all;

        public String getAll() {
            return all;
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<Definition> getResults() {
        return results;
    }

    public void setResults(List<Definition> results) {
        this.results = results;
    }

    public Pronunciation getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(Pronunciation pronunciation) {
        this.pronunciation = pronunciation;
    }
}

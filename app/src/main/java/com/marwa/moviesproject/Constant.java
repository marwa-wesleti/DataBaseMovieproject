package com.marwa.moviesproject;

public class Constant {
    public String extractFirstSentence(String paragraph) {
        int endIndex = paragraph.indexOf('.');
        if (endIndex != -1) {
            return paragraph.substring(0, endIndex + 1);
        } else {
            return paragraph;
        }
    }
}

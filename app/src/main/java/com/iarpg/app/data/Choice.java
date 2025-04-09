package com.iarpg.app.data;

public class Choice {

    private String choice1;
    private String choice2;
    private String consequence1;
    private String consequence2;
    private String correctChoice;

    public Choice(String choice1, String choice2, String consequence1, String consequence2, String correctChoice) {
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.consequence1 = consequence1;
        this.consequence2 = consequence2;
        this.correctChoice = correctChoice;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getConsequence1() {
        return consequence1;
    }

    public String getConsequence2() {
        return consequence2;
    }

    public String getCorrectChoice() {
        return correctChoice;
    }

}

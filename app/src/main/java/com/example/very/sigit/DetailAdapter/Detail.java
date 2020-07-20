package com.example.very.sigit.DetailAdapter;

public class Detail {
    private String question;
    private String answer;
    private String satuan;
    private String condition;

    public Detail(String question, String answer, String satuan, String condition) {
        this.question = question;
        this.answer = answer;
        this.satuan = satuan;
        this.condition = condition;
    }

    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }
    public String getSatuan() {
        return satuan;
    }
    public String getCondition() {
        return condition;
    }
}
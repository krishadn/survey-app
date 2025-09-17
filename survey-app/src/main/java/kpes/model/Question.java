package kpes.model;


public class Question {

    private int qid;
    private String content;
    private int position;


    public Question(int qid, String content, int position) {
        
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Question cannot be blank");
        }

        this.qid = qid;
        this.content = content;
        this.position = position;
    }

    public Question(String content, int position) {
        
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Question cannot be blank");
        }

        this.qid = 0;
        this.content = content;
        this.position = position;
    }

    public int getQid() {
        return qid;
    }

    public String getContent() {
        return content;
    }

    public int getPosition() {
        return position;
    }

    public void setContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Question cannot be blank");
        }
        
        this.content = content;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%d: %s", qid, content);
    }

}

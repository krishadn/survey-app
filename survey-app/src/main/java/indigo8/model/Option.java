package indigo8.model;

public class Option {

    private int optid;
    private String content;
    private int tally;
    private int qid;

    public Option(int optid, String content, int tally, int qid) {
        this.optid = optid;
        this.content = content;
        this.tally = tally;
        this.qid = qid;

    }

    public Option(String content, int tally, int qid) {

        this.content = content;
        this.tally = tally;
        this.qid = qid;

    }


    public int getOptid() {
        return optid;
    }

    public String getContent() {
        return content;
    }

    public int getTally() {
        return tally;
    }

    public int getQid() {
        return qid;
    }

    public void setContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Question cannot be blank");
        }

        this.content = content;
    }

    public void setTally(int tally) {
        this.tally = tally;
    }

    @Override
    public String toString() {
        return content;
    }

    
}

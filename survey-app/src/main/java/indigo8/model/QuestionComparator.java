package indigo8.model;

import java.util.Comparator;

public class QuestionComparator implements Comparator<Question> {

    public int compare(Question q1, Question q2) {
        return Integer.compare(q1.getPosition(), q2.getPosition());
    }

}

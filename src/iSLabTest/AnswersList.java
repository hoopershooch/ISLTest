package iSLabTest;

import iSLabTest.AnsweredQuestion; 
import java.util.ArrayList;

public class AnswersList {
	private ArrayList<AnsweredQuestion> questions;
    public ArrayList<AnsweredQuestion> getQuestions() {
		return questions;
	}
	public AnswersList(){
    	this.questions = new ArrayList<AnsweredQuestion>(); 
    };
    public boolean addAnsweredQuestion(AnsweredQuestion aq){
    	return this.questions.add(aq);
    }


}

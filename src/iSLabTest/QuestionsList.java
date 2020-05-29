package iSLabTest;

import iSLabTest.QuestionareQuestion; 
import java.util.ArrayList;

public class QuestionsList {
    ArrayList <QuestionareQuestion> questions;
    public QuestionsList(){
    	this.questions = new ArrayList <QuestionareQuestion> ();
    };
    public boolean addQuestion(QuestionareQuestion question){
    	return this.questions.add(question);
    };
}

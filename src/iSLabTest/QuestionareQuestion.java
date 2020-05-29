package iSLabTest;

import java.util.ArrayList;
import iSLabTest.QuestionareAnswer;

public class QuestionareQuestion {
	protected String number;
	protected String text;
	protected int rigthAnswersCount;
	ArrayList <QuestionareAnswer> answvars;
	public QuestionareQuestion(){
		this.rigthAnswersCount=0;
		this.answvars = new ArrayList<QuestionareAnswer>();
	}
	public void incRigthCount(){
	    this.rigthAnswersCount++;
	}
	public boolean addAnswVar(QuestionareAnswer answVar){
		return this.answvars.add(answVar);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number){
		this.number = number;
	}
	public String getText() {
		return text;
	}
	public void setText(String text){
		this.text = text;
	};
}

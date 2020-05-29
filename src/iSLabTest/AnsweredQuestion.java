package iSLabTest;

import iSLabTest.AnsweredAnswer; 
import java.util.ArrayList;

public class AnsweredQuestion {
	protected String number;
	protected String text;
	protected boolean sucess; 
	ArrayList<AnsweredAnswer> answvars;
	public AnsweredQuestion(){
	    this.answvars= new ArrayList<AnsweredAnswer>();
	};
    public String getNumber() {
    	return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSucess() {
		return sucess;
	}
	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}
	public boolean addAnswVar(AnsweredAnswer answvar){
		return this.answvars.add(answvar);
	};
}

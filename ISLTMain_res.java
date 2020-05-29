/**
 * 
 */

/**
 * @author Stepanov
 *
 */
import java.util.*;
import java.io.*;
//import javax.xml.*; 
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

class  QuestionareAnswer{
  protected String number;
  protected String text;
  protected boolean corrflag;  
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
  public boolean isCorrflag() {
	  return corrflag;
  }
  public void setCorrflag(boolean corrflag) {
	  this.corrflag = corrflag;
  };
};
class  QuestionareQuestion{
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
  public void setNumber(String number) {
	  this.number = number;
  }
  public String getText() {
	  return text;
  }
  public void setText(String text) {
	  this.text = text;
  };
};

class QuestionsList {
    ArrayList <QuestionareQuestion> questions;
    public QuestionsList(){
    	this.questions = new ArrayList <QuestionareQuestion> ();
    };
    public boolean addQuestion(QuestionareQuestion question){
    	return this.questions.add(question);
    };
};
//------------------------------
class  AnsweredAnswer{
    protected String number;
    protected String text;
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
};

class  AnsweredQuestion{
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
};

class AnswersList{
	ArrayList<AnsweredQuestion> questions;
    public AnswersList(){
    	this.questions = new ArrayList<AnsweredQuestion>(); 
    };
    public boolean addAnsweredQuestion(AnsweredQuestion aq){
    	return this.questions.add(aq);
    }
};

public class ISLTMain {
	AnswersList al;
	QuestionsList ql;
	Scanner sc;
	File qfile,ansfile;
	DocumentBuilderFactory dbf;
    DocumentBuilder db;
    Document qdoc, ansdoc; 
//---------------------------	
	public ISLTMain(){
		this.al = new AnswersList();
		this.ql = new QuestionsList();
		this.sc = new Scanner(System.in); 	
	};
//--------------------------	
	public void Go(){
		System.out.println("��� ������������ ��������� - ��������!");
		System.out.println("��������� ������ ��������� �������� - ��� ������ ������� ������ ������� ����� ������ � ��������� Enter");
		System.out.println("������� ������� Enter ��� ������ ������.");
		if (this.sc.nextLine().isEmpty()){
			System.out.println("�������� - ��������� �������...");
            qfile = new File("Questions.xml");
            if (qfile.exists()){
    			System.out.println("������ ���� questions.xml...");
    			try{
      			  this.dbf = DocumentBuilderFactory.newInstance();
      			  this.db = this.dbf.newDocumentBuilder();  
                  this.qdoc = this.db.parse(qfile);
	    		    
	    		  Node qroot = qdoc.getDocumentElement();
	    		  NodeList nodes = qroot.getChildNodes();
 
	    		  for (int i = 0; i < nodes.getLength(); i++) {
	    		    Node q = nodes.item(i);
                    QuestionareQuestion qq = new QuestionareQuestion();    			
     
                    
                    if ((q.getNodeType()!= Node.TEXT_NODE) & 
	    		    	(q.hasAttributes())&
	    		    	(q.getNodeName().toLowerCase().equals("question"))   	){

	    		    	    int corrq = 0;
                            NamedNodeMap am = q.getAttributes();
    		        		if (am!=null) {
                                for (int j=0; j < am.getLength(); j++){
//    	    		        		System.out.println("�������� "+am.item(j).getNodeName()+" ;"+am.item(j).getNodeValue());
    	    		        		Node qattr = am.item(j); 
    	    		        		if ((qattr.getNodeName().equalsIgnoreCase("text"))&
                                        (qattr.getNodeType() != Node.TEXT_NODE)){
    	    		        			qq.setText(qattr.getNodeValue());
    	    		        			corrq++;
    	    		        		};
   	        				
    	    		        		if ((qattr.getNodeName().equalsIgnoreCase("number"))&
                                        (qattr.getNodeType() != Node.TEXT_NODE)){
        	    		        		  qq.setNumber(qattr.getNodeValue());
      	    		        			  corrq++;
       	    		        		};
                                }  		    	
    		        		}
                            if (corrq == 2){
        		        		NodeList qnl = q.getChildNodes();
                                if (qnl.getLength()>0){
                                	for (int j=0;j<qnl.getLength();j++){
                                		Node qans=qnl.item(j);
                                		QuestionareAnswer qa = new QuestionareAnswer();
                        				int corrans = 0;
                                		if  (qans.hasAttributes()){
                                    		NamedNodeMap qansattrs = qans.getAttributes();
                                    		if (qansattrs!=null){
                                    			for (int k=0; k < qansattrs.getLength();k++){
                                    				Node qansattr = qansattrs.item(k);
/*
                                    				System.out.println("�������� ������" + Integer.toString(k)+ " ;"+
                                    				                 "��� : " + Integer.toString(qansattr.getNodeType())+
                                    				                 "Name : " + qansattr.getNodeName()+
                                    				                 "Val :"+ qansattr.getNodeValue());
*/                                    				                 
                                    				if ((qansattr.getNodeName().equalsIgnoreCase("number"))&
                                    					(qansattr.getNodeType()!=Node.TEXT_NODE)){
                                    					qa.setNumber(qansattr.getNodeValue());
                                                        corrans++;                                   				
                                            		};
                                    				if ((qansattr.getNodeName().equalsIgnoreCase("text"))&
                                        				(qansattr.getNodeType()!=Node.TEXT_NODE)){
                                        				qa.setText(qansattr.getNodeValue());
                                                        corrans++;                                   				
                                    				};
                                    				if ((qansattr.getNodeName().equalsIgnoreCase("right"))&
                                        				(qansattr.getNodeType()!=Node.TEXT_NODE)){
                                    					if (qansattr.getNodeValue().equalsIgnoreCase("true")){
                                    						qa.setCorrflag(true);
                                    						qq.incRigthCount();
                                    					}
                                    					else{
                                    						qa.setCorrflag(false);                                						
                                    					};
                                    					corrans++;
                                       				};
                                    			}
                                   				if (corrans==3){
                                   					qq.addAnswVar(qa);
                                   				}
                                    		}
                                		}
                                	}
                                }
                            }
                            if (!qq.answvars.isEmpty()){
                            	this.ql.addQuestion(qq);
                            }
	    		    }
	    		    
	    		  }
    			}
    			catch (ParserConfigurationException ex) {
    	            ex.printStackTrace(System.out);
    			} catch (SAXException ex) {
    	            ex.printStackTrace(System.out);
    	        } catch (IOException ex) {
    	            ex.printStackTrace(System.out);
    	        }    
    		    if (!this.ql.questions.isEmpty()){
//-----------------------����� �������� � ��������� ������� ------------------------
		    		for (int i=0; i<this.ql.questions.size();i++){
                        AnsweredQuestion aq = new AnsweredQuestion();        		    		

                        aq.setNumber(this.ql.questions.get(i).number);
		    			aq.setText(this.ql.questions.get(i).text);    		    			
		    			System.out.println("������ N "+this.ql.questions.get(i).number+" : "+
    		    				                       this.ql.questions.get(i).text);
    		    		
    		    		QuestionareQuestion q = this.ql.questions.get(i);
    		    		for (int j=0;j < q.answvars.size();j++){
    		    			System.out.println("������� ������ N "+q.answvars.get(j).number+" : "+ q.answvars.get(j).text);
    		    		}
    		    		String ansl;
                        int matchcount=0;
                        int corranswerscount = 0;
                        int wronganswerscount = 0;                            
                        int repeatsarr[];
                        String varsarr[];        		    		
                        do{
                            matchcount=0;
                            corranswerscount = 0;
                            wronganswerscount = 0;                            

                        	//-----------------------
                        	do{
        		    			ansl = this.sc.nextLine();        		    			
        		    		    if (ansl.isEmpty())	System.out.println("������� �������� ������ ����� ������ � ������� Enter.");
        		    		}
        		    		while (ansl.isEmpty());        		    		
        		    		varsarr = ansl.trim().split(" ");
        		    		repeatsarr = new int [varsarr.length];
        		    		matchcount=1;
        		    		//-----------------------
                            for (int j=0;j < varsarr.length;j++){
                            	for (int k=0;k < varsarr.length;k++){
                                	if (varsarr[j].equalsIgnoreCase(varsarr[k])){
                                		repeatsarr[j]++;
                                	}
            		    		}
                            	if (repeatsarr[j]>1) {
    	        		    		System.out.println("���� ���������� ��������� ������� �� �����������! ��������� ����.");
    	        		    		matchcount=0;
                            		break;	                            		
                            	}
                            }                            
                        	//System.out.println(":"+ansl+":");
        		    		//-----------------------	        		    	    
	                        if (matchcount>0){
	                        	for (int j=0;j < varsarr.length;j++){
	                        	    matchcount=0;
	                        		for (int k=0;k < q.answvars.size();k++){
	                                	if (q.answvars.get(k).number.equalsIgnoreCase(varsarr[j])){
	                                		AnsweredAnswer aa = new AnsweredAnswer();
	                                		aa.setNumber(q.answvars.get(k).number);
	                                		aa.setText(q.answvars.get(k).text);
	                                		aq.addAnswVar(aa);
	                                		if (q.answvars.get(k).corrflag){
	                                			corranswerscount++;
	                                		}
	                                		else{
	                                			wronganswerscount++;
	                                		}
	                                		matchcount++;
	                                		break;
	                                	}
	                        		}
	                            	if (matchcount==0){
	    	        		    		System.out.println("������� �������� ������� ������ �� �������������� ����!��������� ����.");
	                            		break;	                            		
	                            	} 
	                            }                            
	                        }
                        }while (matchcount==0);
                        
                        if ((this.ql.questions.get(i).rigthAnswersCount==corranswerscount)&
                        	(wronganswerscount==0)){
        		    		System.out.println("���������!");
                        	aq.setSucess(true);
                        }else{
        		    		System.out.println("�����������!");                            	
        		    		aq.setSucess(false);
                        }
                        this.al.addAnsweredQuestion(aq);
		    		}
		    		int summaryrigths=0;
		    		for (int i=0;i<this.al.questions.size();i++){
		    			if (this.al.questions.get(i).sucess) summaryrigths++;
		    		};
		    		System.out.println("����� �� �������� ��������� �� "+ 
		    		                   Integer.toString(summaryrigths)+" ������� �� "+
		    				           Integer.toString(this.al.questions.size())+".");
		    		System.out.println("���������� �����������...");
//-----------------���������� �����������------------------    		    		
     		    	try{
	     		    	this.ansdoc = this.db.newDocument();
	     		    	Element qr = this.ansdoc.createElement("Questions");
	     		    	qr.appendChild( this.ansdoc.createTextNode("\n"));
	     		    	for (int i=0; i < this.al.questions.size();i++){

	     		    		Element aque = this.ansdoc.createElement("Question");
	     		    		aque.setAttribute("number", this.al.questions.get(i).getNumber());
	     		    		aque.setAttribute("text",  this.al.questions.get(i).getText());
	     		    		if (this.al.questions.get(i).isSucess()){
		     		    		aque.setAttribute("success",  "true");	     		    			
	     		    		}
	     		    		else{
		     		    		aque.setAttribute("success",  "false");	     		    			
	     		    		}
	     		    		for(int j=0;j < al.questions.get(i).answvars.size();j++){
	     		    			Element aquae = this.ansdoc.createElement("Answer");
	     		    			aquae.setAttribute("number", al.questions.get(i).answvars.get(j).getNumber());
	     		    			aquae.setAttribute("text", al.questions.get(i).answvars.get(j).getText());
	    	     		    	aque.appendChild( this.ansdoc.createTextNode("\n"));//
	     		    			aque.appendChild(aquae);
//	    	     		    	if (j!=(al.questions.get(i).answvars.size()-1)) aque.appendChild( this.ansdoc.createTextNode("\n"));//     		    		
	     		    			aque.appendChild( this.ansdoc.createTextNode("\n"));//
	     		    		}
		     		    	qr.appendChild( this.ansdoc.createTextNode("\n"));	     		    		
	     		    		qr.appendChild(aque);
//		     		    	if (i!=(this.al.questions.size()-1)) qr.appendChild( this.ansdoc.createTextNode("\n"));
	     		    		qr.appendChild( this.ansdoc.createTextNode("\n"));
	     		    	}
	     		    	this.ansdoc.appendChild(qr);
//-----------------���������� ����������� � ����------------------

	     		       Transformer transformer = TransformerFactory.newInstance().newTransformer();
	     	           DOMSource source = new DOMSource(this.ansdoc);
	     	           
	     	           File rf = new File(System.getProperty("user.dir") + File.separator + "Answers.xml");
	     	           if (rf.exists()){
	    		    		System.out.println("���� ����������� ���������� - �� ����� �����������.");	     	        	   
	     	           }
    	           
	     	           StreamResult result = new StreamResult(rf);
	     	            transformer.transform(source, result);
	     	 	     		    	
    		    		System.out.println("���������� ���������...");	     		    		
    		    		System.out.println("������� Enter ��� ������...");
    		    		this.sc.nextLine();
	     		    	
     		    	
     		    	}catch (DOMException ex) {
        	            ex.printStackTrace(System.out);
        	        }catch (TransformerConfigurationException ex) {
        	            ex.printStackTrace(System.out);
        	        }catch (TransformerException ex) {
        	            ex.printStackTrace(System.out);
        	        };    
    		    	
	    		}
    		    else{
	              	System.out.println("��������, �� ���� � ��������� �� �������� ���������� ��������.");    		    	
	    			this.sc.nextLine();    		    
    		    }
            }
            else{
            	System.out.println("���� � ��������� questions.xml �� ������!");            	
    			this.sc.nextLine();            
            };
		}
		else{
			System.out.println("����!");
			this.sc.nextLine();
		}
	
	}
    public static void main(String[] args) {
       ISLTMain mc = new ISLTMain();
       mc.Go();
    };
};

package iSLabTest;

import java.util.*;
import java.io.*;
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


public class ISLTMain {
	private ArrayList <String> errl;	
	private AnswersList al;
	private QuestionsList ql;
	private	Scanner sc;
	private	File qfile;
	private	DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document qdoc, ansdoc; 
//---------------------------------------	
	public boolean AcquireFiles(String[] mainargs){
		this.errl.clear();
		int argLength = mainargs.length;
		if (argLength==0){
		  this.qfile = new File("Questions.xml");
		}else if ((argLength>0)&((argLength<2))){
			this.qfile = new File(mainargs[0]);
		}	
	
		if (this.qfile.exists()){
			if (this.qfile.canRead()){
				return true;
			}
			else{
				this.errl.add("Невозможно чтение файла <"+this.qfile.getName()+">! Невозможно провести тест!");
	            return false;			
			}
		}
		else{
			this.errl.add("Не удалось найти файл <"+this.qfile.getName()+">! Невозможно провести тест!");
            return false;			
		}
	}
//-------------------------------------
	public boolean ParseQuestions(){
		this.errl.clear();
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
    		    	(q.getNodeName().toLowerCase().equals("question"))	){
                	
    		    	    int corrq = 0;
                        NamedNodeMap am = q.getAttributes();
		        		if (am!=null) {
                            for (int j=0; j < am.getLength(); j++){
//	    		        		System.out.println("аттрибут "+am.item(j).getNodeName()+" ;"+am.item(j).getNodeValue());
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
                                				System.out.println("аттрибут ответа" + Integer.toString(k)+ " ;"+
                                				                 "Тип : " + Integer.toString(qansattr.getNodeType())+
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
            if (this.ql.questions.size()>0){
            	return true;
            } else {
            	this.errl.add("Файл вопросов не содержит корректно заданных вопросов!");
            	return false;
            }
		}catch (ParserConfigurationException ex) {
		    this.errl.add("При разборе файла вопросов произошла следующая ошибка"+ex.getMessage());
            return false;
		} catch (SAXException ex) {
		    this.errl.add("При разборе файла вопросов произошла следующая ошибка"+ex.getMessage());
            return false;
        } catch (IOException ex) {
		    this.errl.add("При разборе файла вопросов произошла следующая ошибка"+ex.getMessage());
            return false;
        }    
	}
	
//--------------------------------------
	public void AskQuestionsGetAnswers(){
		for (int i=0; i<this.ql.questions.size();i++){
            AnsweredQuestion aq = new AnsweredQuestion();        		    		

            aq.setNumber(this.ql.questions.get(i).number);
			aq.setText(this.ql.questions.get(i).text);    		    			
			System.out.println("Вопрос N "+this.ql.questions.get(i).number+" : "+
    				                       this.ql.questions.get(i).text);
    		
    		QuestionareQuestion q = this.ql.questions.get(i);
    		for (int j=0;j < q.answvars.size();j++){
    			System.out.println("Вариант ответа N "+q.answvars.get(j).getNumber()+" : "+ q.answvars.get(j).getText());
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
	    		    if (ansl.isEmpty())	System.out.println("Введите варианты ответа через пробел и нажмите Enter.");
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
    		    		System.out.println("Ввод одинаковых вариантов ответов не допускается! Повторите ввод.");
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
                        	if (q.answvars.get(k).getNumber().equalsIgnoreCase(varsarr[j])){
                        		AnsweredAnswer aa = new AnsweredAnswer();
                        		aa.setNumber(q.answvars.get(k).getNumber());
                        		aa.setText(q.answvars.get(k).getText());
                        		aq.addAnswVar(aa);
                        		if (q.answvars.get(k).isCorrflag()){
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
        		    		System.out.println("Вводите варианты ответов только из представленных выше!Повторите ввод.");
                    		break;	                            		
                    	} 
                    }                            
                }
            }while (matchcount==0);
            
            if ((this.ql.questions.get(i).rigthAnswersCount==corranswerscount)&
            	(wronganswerscount==0)){
	    		System.out.println("Правильно!");
            	aq.setSucess(true);
            }else{
	    		System.out.println("Неправильно!");                            	
	    		aq.setSucess(false);
            }
            this.al.addAnsweredQuestion(aq);
		}
		int summaryrigths=0;
		for (int i=0;i<this.al.getQuestions().size();i++){
			if (this.al.getQuestions().get(i).sucess) summaryrigths++;
		};
		System.out.println("Итого вы ответили правильно на "+ 
		                   Integer.toString(summaryrigths)+" вопроса из "+
				           Integer.toString(this.al.getQuestions().size())+".");
		
	}
//--------------------------------------
	public boolean FillResults(){
		//-----------------заполнение результатов------------------
		this.errl.clear();
    	try{
	    	this.ansdoc = this.db.newDocument();
	    	Element qr = this.ansdoc.createElement("Questions");
	    	qr.appendChild( this.ansdoc.createTextNode("\n"));
	    	for (int i=0; i < this.al.getQuestions().size();i++){
	    		Element aque = this.ansdoc.createElement("Question");
	    		aque.setAttribute("number", this.al.getQuestions().get(i).getNumber());
	    		aque.setAttribute("text",  this.al.getQuestions().get(i).getText());
	    		if (this.al.getQuestions().get(i).isSucess()){
	    		aque.setAttribute("success",  "true");	     		    			
	    		}
	    		else{
	    		aque.setAttribute("success",  "false");	     		    			
	    		}
	    		for(int j=0;j < al.getQuestions().get(i).answvars.size();j++){
	    			Element aquae = this.ansdoc.createElement("Answer");
	    			aquae.setAttribute("number", al.getQuestions().get(i).answvars.get(j).getNumber());
	    			aquae.setAttribute("text", al.getQuestions().get(i).answvars.get(j).getText());
	    			aque.appendChild( this.ansdoc.createTextNode("\n"));//
	    			aque.appendChild(aquae);
	    			aque.appendChild( this.ansdoc.createTextNode("\n"));//
	    		}
	    		qr.appendChild( this.ansdoc.createTextNode("\n"));	     		    		
	    		qr.appendChild(aque);
	    		qr.appendChild( this.ansdoc.createTextNode("\n"));
	    	}
	    	this.ansdoc.appendChild(qr);
	       	return true;
    	}catch (DOMException ex) {
		    this.errl.add("При заполнении результатов теста произошла следующая ошибка"+ex.getMessage());
            return false;
    	}
	}
//--------------------------------------
	public boolean SaveResultsToFile(){
		//-----------------сохранение результатов в файл------------------
		this.errl.clear();
 		try{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        DOMSource source = new DOMSource(this.ansdoc);
	        
	        File rf = new File(System.getProperty("user.dir") + File.separator + "Answers.xml");
	        if (rf.exists()){
	 		System.out.println("Файл результатов существует - он будет перезаписан.");	     	        	   
	        }

	        StreamResult result = new StreamResult(rf);
	        transformer.transform(source, result);
			return true;	        
		}catch (TransformerConfigurationException ex) {
		    this.errl.add("При сохранении результатов теста произошла следующая ошибка"+ex.getMessage());
            return false;
		}catch (TransformerException ex) {
		    this.errl.add("При сохранении результатов теста произошла следующая ошибка"+ex.getMessage());
            return false;
        }
	}
//--------------------------------------
	public void OutErrMess(String preMessage){
		System.out.println(preMessage);
		for (int i=0; i<this.errl.size();i++){
			System.out.println(this.errl.get(i));
		}
	}
//--------------------------------------
	public void Go(String [] mainargs){
		System.out.println("Вас приветствует программа - опросник!");
		System.out.println("Программа задаст несколько вопросов - для ответа вводите номера ответов через пробел и нажимайте Enter");
		System.out.println("Нажмите клавишу Enter как будете готовы.");
		if (this.sc.nextLine().isEmpty()){
			System.out.println("Начинаем - загружаем вопросы...");
			if (AcquireFiles(mainargs)){
				if (ParseQuestions()){
					AskQuestionsGetAnswers();
					if (FillResults()){
						if (SaveResultsToFile()){
							System.out.println("Результаты сохранены...");	     		    		
							System.out.println("Нажмите Enter для выхода...");
							this.sc.nextLine();
						}else{
							OutErrMess("При сохранении данных теста в файл возникли следующие ошибки:");							
						}
					}else{
						OutErrMess("При заполнении данных теста возникли следующие ошибки:");						
					}
				}else{
					OutErrMess("При разборе данных из файла вопросов возникли следующие ошибки:");					
				}
			}else{
				OutErrMess("При работе с файлом вопросов возникли следующие ошибки:");
			}
		}else{
			System.out.println("Пока");
			this.sc.nextLine();			
		}
	}
//---------------------------------------
	public ISLTMain(){
		this.errl = new ArrayList<String>();		
		this.al = new AnswersList();
		this.ql = new QuestionsList();
		this.sc = new Scanner(System.in); 	
	};
	public static void main(String[] args) {
		ISLTMain mc = new ISLTMain();
		mc.Go(args);
	}
}

package edu.cpplib.headings;

import java.io.IOException;
import java.util.ArrayList;


/*
 * Check content order, but do not check if headings are capital and bold here.
 * 
 * */
public class ContentOrder {
		
	public static ArrayList<Heading> getValidHeadings(ArrayList<Heading> firstLines){
		ArrayList<Heading> headings = new ArrayList<Heading>();
		for(Heading h : firstLines){
			if(h.isHeading()){
				headings.add(h);
			}
		}
		return headings;
	}

	
	public static String check(ArrayList<Heading> firstLines) throws IOException{
		
		ArrayList<Heading> headings = getValidHeadings(firstLines);
		String result = "";
		
//		for(Heading h : headings){
//			h.print();
//		}
		
		for(Heading h : headings){
			h.buildHeadingsReachable();
		}

		System.out.println("required contents existence: ");
		result += "Missing required contents:<br>(if there is nothing to show which means all required contents are existed)<br>";
		
		boolean[] requiredContents = new boolean[4];
		int sig = 0, abs = 1, toc = 2, ref = 3;
		
		for(Heading h : headings){
			if(h.getPageType().equals("SIG")){
				requiredContents[sig] = true;
			}
			else if(h.getPageType().equals("ABS")){
				requiredContents[abs] = true;
			}
			else if(h.getPageType().equals("TOC")){
				requiredContents[toc] = true;
			}
			else if(h.getPageType().equals("REF")){
				requiredContents[ref] = true;
			}
		}
		
		for(int i=0; i<4; ++i){
			if(requiredContents[i] == false){
				if(i == sig){
					System.out.println("Signature page is missing");
					result += "<b>* Signature page is missing</b><br>";
				}
				if(i == abs){
					System.out.println("Abstract page is missing");
					result += "<b>* Abstract page is missing</b><br>";
				}
				if(i == toc){
					System.out.println("Table of Content is missing");
					result += "<b>* Table of Content is missing</b><br>";
				}
				if(i == ref){
					System.out.println("References is missing");
					result += "<b>* References is missing</b><br>";
				}
			}
		}
		
		result += "<br>Check sequence of parts:<br>";
		
		for(int i=1; i<headings.size(); ++i){
//			System.out.println(headings.get(i).getPageType() + " can reach to " + headings.get(i).getHeadingsReachable());
			if(headings.get(i-1).getHeadingsReachable().contains(headings.get(i).getPageType())){
				System.out.println(headings.get(i-1).getPageType() + " ---> " + headings.get(i).getPageType());
				result += headings.get(i-1).getPageType() + " ---> " + headings.get(i).getPageType() + "<br>";
				
			}
			else{
				System.out.println(headings.get(i-1).getPageType() + "," + headings.get(i-1).getPageNum() + 
									" -x-> " + 
									headings.get(i).getPageType() + "," + headings.get(i).getPageNum());
				
				result += headings.get(i-1).getPageType() + "(Real page num: " + headings.get(i-1).getPageNum() + ")" +
							" -x-> " + 
							headings.get(i).getPageType() + "(Real page num: " + headings.get(i).getPageNum() + ")<br>";
			}
//			System.out.println("reachable of i + " + headings.get(i).getHeadingsReachable());
		}
		
		result += "<br>Note:<br>"
				+ "SIG: SIGNATURE PAGE<br>"
				+ "ACK: ACKNOWLEDGEMENTS<br>"
				+ "ABS: ABSTRACT<br>"
				+ "TOC: TABLE OF CONTENTS<br>"
				+ "LOT: LIST OF TABLES<br>"
				+ "LOF: LIST OF FIGURES<br>"
				+ "REF: REFERENCE or WORKS CITED or BIBLIOGRAPHY<br>"
				+ "APP: APPENDIX<br>";
				
				/*
				 * relation: 
				    SIGNATURE PAGE
					ACKNOWLEDGEMENTS (optional)
					ABSTRACT
					TABLE OF CONTENTS
					LIST OF TABLES (optional)
					LIST OF FIGURES (optional)
					REFERENCE = WORKS CITED = BIBLIOGRAPHY
					APPENDIX (optional)
				 * 
				 * */
		return result;
		
		/*
		 * 
		 * 	ABS can reach to TOC
			TOC can reach to LOT,LOF,REF
			LOT can reach to LOF,REF
			LOF can reach to REF
			REF can reach to APP,END
		 * 
		 * 
		 * 
		 */


	}
	
 
	
	
}

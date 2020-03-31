package edu.cpplib.headings;

import java.io.IOException;
import java.util.ArrayList;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.export.HeadingResult;
import edu.cpplib.page.Page;

public class Heading {
	protected int pageNum;
	protected String text, pageType, textWithoutSpaces;
	protected boolean isHeading;
	private boolean isBoldInPage, isCapsInPage;
	private String headingsReachable;
	
	public Heading(int pgNum, String text){
		this.pageNum = pgNum;
		this.text = text;
		this.textWithoutSpaces = StrFunc.getCharsOnly(text);
		this.isHeading = true;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getTextWithoutSpaces() {
		return textWithoutSpaces;
	}

	public void setTextWithoutSpaces(String textWithoutSpaces) {
		this.textWithoutSpaces = textWithoutSpaces;
	}

	public boolean isHeading() {
		return isHeading;
	}

	public void setHeading(boolean isHeading) {
		this.isHeading = isHeading;
	}
	
	public boolean isBoldInPage() {
		return isBoldInPage;
	}

	public void setBoldInPage(boolean isBoldInPage) {
		this.isBoldInPage = isBoldInPage;
	}

	public boolean isCapsInPage() {
		return isCapsInPage;
	}

	public void setCapsInPage(boolean isCapsInPage) {
		this.isCapsInPage = isCapsInPage;
	}

//	public ArrayList<String> getHeadingsReachable() {
//		return headingsReachable;
//	}
//
//	public void setHeadingsReachable(ArrayList<String> headingsReachable) {
//		this.headingsReachable = headingsReachable;
//	}

	public String getHeadingsReachable() {
		return headingsReachable;
	}

	public void setHeadingsReachable(String headingsReachable) {
		this.headingsReachable = headingsReachable;
	}

	public boolean containKeyword(String str){
		String textWithoutSpacesAndUpper = this.textWithoutSpaces.toUpperCase();
		return textWithoutSpacesAndUpper.contains(str);
	}
	
	public int wordCount(String str){
		String trim = str.trim();
	    if (trim.isEmpty())
	        return 0;
	    return trim.split("\\s+").length; 
	}
	
	
	public void defineHeading(){
		if(wordCount(this.text) > 3){
			this.setHeading(false);
		}
		if(this.containKeyword("SIGNATURE")){
			this.setPageType("SIG");
		}
		else if(this.containKeyword("ABSTRACT")){
			if(wordCount(this.text) == 1){
				this.setPageType("ABS");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("ACKNOWLEDGEMENT")){
			if(wordCount(this.text) == 1){
				this.setPageType("ACK");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("TABLEOFCONTENT")){
			if(wordCount(this.text) == 3){
				this.setPageType("TOC");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("CONTENT")){
			if(wordCount(this.text) == 1){
				this.setPageType("TOC");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("LISTOFTABLE")){
			if(wordCount(this.text) == 3){
				this.setPageType("LOT");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("LISTOFFIGURE")){
			if(wordCount(this.text) == 3){
				this.setPageType("LOF");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
			
		}
		else if(this.containKeyword("REFERENCE")){
//			System.out.println(this.getText());
			if(wordCount(this.text) == 1){
				this.setPageType("REF");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("WORKSCITED")){
			if(wordCount(this.text) == 1){
				this.setPageType("REF");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("BIBLIOGRAPHY")){
			if(wordCount(this.text) == 1){
				this.setPageType("REF");
			}
			else{
				this.setPageType("NI");
				this.setHeading(false);
			}
		}
		else if(this.containKeyword("APPENDI")){
			this.setPageType("APP");
		}
		else{
			this.setPageType("NI");
			this.setHeading(false);
		}
	}
	
	public void print(){
		if(this.isHeading){
//			System.out.println("word count: " + wordCount(text));
			System.out.println(pageNum + ": " + text + " " + pageType + ", Headings reachable:" + headingsReachable);
//			ArrayList<String> tmp = this.getHeadingsReachable();
//			System.out.println(this.getHeadingsReachable().size());
		}
	}
	
	
	public void boldAndCapsInPage(ArrayList<Page> allPages, int pageNum) throws IOException{
		int lastPage = allPages.size();
		if(this.pageNum >= lastPage){}
		if(this.isHeading && (!this.getPageType().equals("SIG"))){
			Page pg = new Page();
			pg = allPages.get(pageNum-1);
//			pg.getData(filePath, pages.get(this.pageNum), this.pageNum);
//			for(Glyph g : pg.getGlyphs()){
//				System.out.println(g.getGl() + " " + g.getFontFamily());
//			}
//			System.out.println();
			
			String boldChars = pg.getBoldChars();
			boldChars = StrFunc.getCharsOnly(boldChars);
			//check: bold chars contains this.text -> heading is bold
			this.setBoldInPage(boldChars.contains(this.textWithoutSpaces));
			
			//check: bold chars are all caps -> heading is caps
			this.setCapsInPage(StrFunc.allCaps(this.textWithoutSpaces));
//			System.out.println("Bold: " + boldChars + ", textWithoutSpaces" + this.textWithoutSpaces);
		}
	}

	
	public void boldCapsResult(){
		if(this.isHeading && (!this.getPageType().equals("SIG"))){
			System.out.println("Heading in the page is bold and caps?");
			System.out.println("Heading [ " 
								+ this.getText() 
								+ " ] in page [ " + this.getPageNum() + " ], " 
								+ "Bold? " + this.isBoldInPage()
								+ ", Caps? " + this.isCapsInPage());
		}
	}
	
	public HeadingResult buildHeadingItem(){
		if(this.isHeading && (!this.getPageType().equals("SIG"))){
			return new HeadingResult(this.getText(), this.getPageNum(), this.isBoldInPage(), this.isCapsInPage());
		}
		return new HeadingResult("", -1, true, true);
	}
	/*
	 * finally print:
	 * Heading [ABSTRACT] in page [3], bold [true], caps[true]
	 * Heading [TableofContents] in page [4], bold [true], caps [false]
	 * 
	 * 	3: ABSTRACT
		4: TableofContents
		5: LISTOFFIGURES
		8: LISTOFTABLES
		55: APPENDIX
		60: REFERENCE
	 * 
	 * 
	 * 
	 * */
	
	public void buildHeadingsReachable(){
		if(this.isHeading){
			if(this.getPageType().equals("SIG")){
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("ACK");
//				reachable.add("ABS");
				this.setHeadingsReachable("ACK,ABS");
			}
			else if(this.getPageType().equals("ACK")){
//				reachable = new String[1];
//				reachable[0] = "ABS";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("ABS");
				this.setHeadingsReachable("ABS");
			}
			else if(this.getPageType().equals("ABS")){
//				reachable = new String[1];
//				reachable[0] = "TOC";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("TOC");
				this.setHeadingsReachable("TOC");
			}
			else if(this.getPageType().equals("TOC")){
//				reachable = new String[3];
//				reachable[0] = "LOT";
//				reachable[1] = "LOF";
//				reachable[2] = "REF";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("LOT");
//				reachable.add("LOF");
//				reachable.add("REF");
				this.setHeadingsReachable("LOT,LOF,REF");
			}
			else if(this.getPageType().equals("LOT")){
//				reachable = new String[2];
//				reachable[0] = "LOF";
//				reachable[1] = "REF";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("LOF");
//				reachable.add("REF");
				this.setHeadingsReachable("LOF,REF");
				
			}
			else if(this.getPageType().equals("LOF")){
//				reachable = new String[1];
//				reachable[0] = "REF";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("REF");
				this.setHeadingsReachable("REF");
			}
			else if(this.getPageType().equals("REF")){
//				reachable = new String[2];
//				reachable[0] = "APP";
//				reachable[1] = "END";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("APP");
//				reachable.add("END");
				this.setHeadingsReachable("APP,END");
			}
			else if(this.getPageType().equals("APP")){
//				reachable = new String[1];
//				reachable[0] = "END";
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("END");
				this.setHeadingsReachable("APP,END");
			}
			else{
//				ArrayList<String> reachable = new ArrayList<String>();
//				reachable.add("NI");
				this.setHeadingsReachable("NI");
			}
//			this.setHeadingsReachable(reachable);
		}

		
	}
	
	
	
}

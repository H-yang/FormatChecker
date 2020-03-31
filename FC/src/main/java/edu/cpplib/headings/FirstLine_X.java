package edu.cpplib.headings;

import java.util.ArrayList;

public class FirstLine_X {
	private int pageNum;
	private String text;
	private String pageType; //we can tell page type from first line
	/*
	 * page type:
	 * SIG signature page
	 * ACK acknowledgement
	 * ABS abstract
	 * TOC table of content
	 * LOF list of figure
	 * LOT list of table
	 * GC  general content
	 * REF reference
	 * APP appendix
	 * */
	
	
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
	public void collectInfo(int pgNum, String text){
		setPageNum(pgNum);
		setText(text);
		setPageType("GC");
	}
	
	public void print(){
		System.out.println(pageNum + ": " + text + " " + pageType);
	}
	
	public void definePageType_Pass1(){
		String textToUpper = text.toUpperCase();
		if(textToUpper.contains("SIGNATURE")) setPageType("SIG");
		if(textToUpper.contains("ACKNOWLEDGMENTS")) setPageType("ACK");
		if(textToUpper.contains("ABSTRACT")) setPageType("ABS");
		if(textToUpper.contains("TABLEOFCONTENTS")) setPageType("TOC");
		if(textToUpper.contains("LISTOFTABLES")) setPageType("LOT");
		if(textToUpper.contains("LISTOFFIGURES")) setPageType("LOF");
		if(textToUpper.contains("REFERENCE")) setPageType("REF");
		if(textToUpper.contains("APPENDIX")) setPageType("APP");
	}
	
//	public static void definePageType_Pass2(ArrayList<FirstLine> firstLines){
//		for(int i=1; i<firstLines.size(); ++i){
//			String pageTypeCur = firstLines.get(i).getPageType();
//			String pageTypePrev = firstLines.get(i-1).getPageType();
//			
//			if(pageTypeCur.equals("GC")){
//				if(!pageTypePrev.equals("GC")){
//					firstLines.get(i).setPageType(pageTypePrev);
//				}
//			}
//		}
//	}
	
}

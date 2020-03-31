package edu.cpplib.line;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.glyph.Glyph;

public class Line {
	
	private String text;
	private boolean isEnter;
	private float fontSize;
	private float topPos;
	private float leftPos;
	private float rightPos;
	private boolean isTOCItem;


	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public float getTopPos() {
		return topPos;
	}
	public void setTopPos(float topPos) {
		this.topPos = topPos;
	}
	public float getLeftPos() {
		return leftPos;
	}
	public void setLeftPos(float leftPos) {
		this.leftPos = leftPos;
	}
	public float getRightPos() {
		return rightPos;
	}
	public void setRightPos(float rightPos) {
		this.rightPos = rightPos;
	}
	public boolean isEnter() {
		return isEnter;
	}
	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}

	
	public boolean isTOCItem() {
		return isTOCItem;
	}
	public void setTOCItem(boolean isTOCItem) {
		this.isTOCItem = isTOCItem;
	}
	
	public void print(){
		System.out.println(text);
	}
	
	

	public void elimDot(){
		text = text.replaceAll("\\.", "");
	}
	
	public boolean determineEnter(){
		char[] chars = text.toCharArray();
		for (char c : chars){
			if(c == ' ') continue;
			if(Character.isAlphabetic(c)) return false;
			if(Character.isDigit(c)) return false;
			if(c == '.') return true;
		}
		return false;
	}

	public static String getCharsOnly(String source){
//		
//		String s = text.replace(" ", "");

		char[] chars = source.toCharArray();
		String s = "";
		for(char c : chars){
			if(c == ' ') continue;
//			s += c;
			if(Character.isAlphabetic(c)) s += c;
			if(Character.isDigit(c)) s += c;
//			if(c == ',') s+= c;
		}

		return s;
	}

	
	public static String getCharsAndComma(String source){
//		
//		String s = text.replace(" ", "");

		char[] chars = source.toCharArray();
		String s = "";
		for(char c : chars){
			if(c == ' ') continue;
//			s += c;
			if(Character.isAlphabetic(c)) s += c;
			if(Character.isDigit(c)) s += c;
			if(c == ',') s+= c;
		}

		return s;
	}
	
	
	
	
	public boolean containsChars(){
		char[] chars = text.toCharArray();
//		String s = "";
		boolean flag = false;
		
		for(char c : chars){
			if(c == ' ') continue;
			if(Character.isAlphabetic(c)) {
				flag = true; 
				break;
			}
			if(Character.isDigit(c)) {
				flag = true; 
				break;
			}
		}
		
		return flag;
	}
	
	public void noOpOnNullLine(){
		if(!this.containsChars()){
			this.setLeftPos(0);
			this.setTopPos(0);
		}
	}
	
	
		
	
	public String parseTOCToStrArr(){
		
//		ArrayList<String> TOCitemStrArr = new ArrayList<String>();
		
		if(this.getText() == null || this.getText().length() <= 10){
			//skip
//			System.out.println("ifXXXXX " + this.getText());
//			System.out.print(isEnter());
//			System.out.println(this.getText().length());
			return "";
		}
		else{
//			System.out.println("elseXXXXX " + this.getText());
			String lineText = StrFunc.elimSpaces(this.getText()).trim();
			lineText = StrFunc.elimLastTwoChars(lineText);
			lineText = lineText.replace('\u2026', '.');
			
			String dotPattern = "[\\.]{2}";
			Pattern r = Pattern.compile(dotPattern);
			Matcher m = r.matcher(lineText);
			
			boolean containsDot;
			if(m.find()){
				containsDot = true;
			}else {
				containsDot = false;
			}
			
//			System.out.println(lineText);
//			System.out.println("Contains Dot? " + containsDot);
			
			String itemStr = "";
			if(containsDot){
				String strPattern = "[^.]*";
				r = Pattern.compile(strPattern);
				m = r.matcher(lineText);
				
				int foundValueCounter = 0;
				while(m.find()){
					if(m.group(0).length() >= 1){
						itemStr += m.group(0);
						itemStr += "#";
//						System.out.println("Found value: " + m.group(0));
						foundValueCounter ++;
					}
				}
				
				if(foundValueCounter == 1){
					String sharp = "#";
					itemStr = sharp + itemStr;
				}
//				System.out.println("Found: " + itemStr);
//				TOCitemStrArr.add(itemStr);
				
			}
			else{
				this.setTOCItem(false);
				String lineTextUpper = lineText.toUpperCase();
				if( lineTextUpper.contains("TABLEOFCONTENT") ||
					lineTextUpper.contains("LISTOFTABLE") ||
					lineTextUpper.contains("LISTOFFIGURE")){
					
				}
				else{
					itemStr += lineText;
					itemStr += "#NEXT#";
//					System.out.println("Found: " + itemStr);
//					TOCitemStrArr.add(itemStr);
//					return itemStr;
				}
			}
			return itemStr;
		}
	}
	
	
}

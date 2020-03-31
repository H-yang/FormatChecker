package edu.cpplib.block;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Block {
	
	private String raw;
	private String text;
	private float leftPos;
	private float topPos;


	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float getLeftPos() {
		return leftPos;
	}
	public void setLeftPos(float leftPos) {
		this.leftPos = leftPos;
	}
	public float getTopPos() {
		return topPos;
	}
	public void setTopPos(float topPos) {
		this.topPos = topPos;
	}

	public String getRaw() {
		return raw;
	}
	public void setRaw(String raw) {
		this.raw = raw;
	}
	
	
	public void print(){
		System.out.println("text: " + text);
		System.out.println("l/t: " + leftPos + " " + topPos);
	}
	
	public String findText(){

//		System.out.println(raw);
		String[] aa = raw.split("\\|");
		String textInBlock = "";
		
		for(String a : aa){
//			System.out.print(a);
			char[] tmp = a.toCharArray();
			textInBlock += tmp[0];
		}
		
		return textInBlock;
	}
	
	
	//I$155.18005;279.518| get 155.18005
	public float findTopPos(){
//		System.out.println(raw);
		String pattern = "\\$(.*?)\\;";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(raw);
		
		String topStr = "";
		
		if (m.find()) {topStr = m.group(1);}
		else {topStr = "-1";}
		
		Float top = Float.parseFloat(topStr);
		
		return top;
	}
	
	
	//I$155.18005;279.518| get 279.518
	public float findLeftPos(){
//		System.out.println(raw);
		String pattern = "\\;(.*?)\\|";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(raw);
		
		String leftStr = "";
		
		if (m.find()) {leftStr = m.group(1);}
		else {leftStr = "-1";}
		
		Float left = Float.parseFloat(leftStr);
		
		return left;
	}
	
}

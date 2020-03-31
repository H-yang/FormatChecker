package edu.cpplib.font_style;

import java.util.ArrayList;

public class FontStyle {
	private int pageNum;
	private String fontFamily;
	private float fontSize;
	private boolean fontStyleDifferent;

	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	public boolean isFontStyleDifferent() {
		return fontStyleDifferent;
	}
	public void setFontStyleDifferent(boolean fontStyleDifferent) {
		this.fontStyleDifferent = fontStyleDifferent;
	}
	
	
	
	public FontStyle(int pgNum, String ff, float fs){
		this.pageNum = pgNum;
		this.fontFamily = ff;
		this.fontSize = fs;
	}
	
	public void print(){
		if(isFontStyleDifferent()){
			System.out.println(pageNum + ": " + fontFamily + ", " + fontSize + "*");
		}
		else{
			System.out.println(pageNum + ": " + fontFamily + ", " + fontSize);
		}
	}

	
	public String print_Str(){
		String ret = "";
		if(isFontStyleDifferent()){
			ret += "<font color=\"#ff0066\">" + pageNum + ": " + fontFamily + ", " + fontSize + "*" + "</font>";
//			System.out.println(pageNum + ": " + fontFamily + ", " + fontSize + "*");
		}
		else{
			ret += pageNum + ": " + fontFamily + ", " + fontSize;
//			System.out.println(pageNum + ": " + fontFamily + ", " + fontSize);
		}
		
		return ret;
	}
	
	
	
	public static ArrayList<String> markFontStyleChange(ArrayList<FontStyle> fs){
		for(int i=1; i<fs.size(); ++i){
			
			String ffCur = fs.get(i).getFontFamily();
			String ffPrev = fs.get(i-1).getFontFamily();
			
			float fsCur = fs.get(i).getFontSize();
			float fsPrev = fs.get(i-1).getFontSize();
			
			if(!ffCur.equals(ffPrev) || fsCur != fsPrev){
				fs.get(i).setFontStyleDifferent(true);
			}
		}
		
		
		ArrayList<String> fslist = new ArrayList<String>();
		for(FontStyle f : fs){
			String str = f.print_Str();
			fslist.add(str);
		}
	
		return fslist;
		
	}

}

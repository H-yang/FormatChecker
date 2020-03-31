package edu.cpplib.glyph;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;

import edu.cpplib.line.RawData2;

public class Glyph {
	private char gl;
	private float leftPos;
	private float topPos;
	private String fontFamily;
	private float fontSize;
	private float rotationDir;
	private boolean isBold;
	private boolean isEnter;

	
	public char getGl() {
		return gl;
	}
	public void setGl(char gl) {
		this.gl = gl;
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
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	public boolean isEnter() {
		return isEnter;
	}
	public void setEnter(boolean isEnter) {
		this.isEnter = isEnter;
	}
	public float getRotationDir() {
		return rotationDir;
	}
	public void setRotationDir(float rotationDir) {
		this.rotationDir = rotationDir;
	}
	
	public static float parsePageWidth(ArrayList<String> raw){
		String tmp = raw.get(raw.size()-1);
		float width = Float.parseFloat(tmp);
		return width;
	}
	
	public static float parsePageHeight(ArrayList<String> raw){
		String tmp = raw.get(raw.size()-2);
		float height = Float.parseFloat(tmp);
		return height;
	}
	
	
	public static ArrayList<String> reArrangeRaw(ArrayList<String> raw){
//		System.out.println(raw.get(raw.size()-1));
//		System.out.println(raw.get(raw.size()-2));
//		
		ArrayList<String> rawArranged = new ArrayList<String>();
		for(int i=0; i< raw.size(); ++i){
			String gLines[] = raw.get(i).split("#");
			for(String gline : gLines){
				rawArranged.add(gline);
			}
 		}
		
		return rawArranged;
	}
	
	public static ArrayList<Glyph> build(PDDocument doc, int num, boolean isLast) throws IOException{
		int numOfStandardItems = 6;
		
		ArrayList<String> raw = RawData2.build(doc, num, isLast);
		ArrayList<String> rawArranged = null;
		
		rawArranged = reArrangeRaw(raw);
		
		ArrayList<Glyph> glyphs = new ArrayList<Glyph>();
		
		for(String r : rawArranged){
			Glyph glyph = new Glyph();
			
			String info[] = r.split(";");
			
			if(info.length != numOfStandardItems){
//				System.out.println("!=6: " + info.length); //\n
				glyph.setEnter(true);
			}
			else{
//				System.out.println(info[0]);
				float leftPos = Float.parseFloat(info[0]);
				glyph.setLeftPos(leftPos);
				
				
//				System.out.println(info[1]);
				float topPos = Float.parseFloat(info[1]);
				glyph.setTopPos(topPos);
				
//				System.out.println(info[2]);
				float fontSize = Float.parseFloat(info[2]);
				glyph.setFontSize(fontSize);
				
//				System.out.println(info[3]);
				String fontFamily = info[3];
				glyph.setFontFamily(fontFamily);
				
				if(fontFamily.contains("Bold")){
					glyph.setBold(true);
				}
				
//				System.out.println(info[4]);
				int last = info[4].length()-1;
				char gl = info[4].charAt(last);
				glyph.setGl(gl);
				
//				System.out.println(info[4]);
//				System.out.println(info[5]);
				float dir = Float.parseFloat(info[5]);
				glyph.setRotationDir(dir);
//				glyph.setGl(gl);
			}
			
			glyphs.add(glyph);
		}
		

		return glyphs;
		
	}

	public boolean isSpace(){
		return (gl == ' ' || gl == '\t');
	}
	
	public void specialFontFamilyTrim(){
		if(fontFamily != null){
			if(fontFamily.contains("Wingdings")){
				setFontFamily("default");
			}
		}

	}

	
}

package edu.cpplib.glyph;

public class Glyph1 {
	private String fontFamily, glyph;
	private float fontSizePt, leftPos, topPos;
	
	public Glyph1(String glyph, String fontFamily, float fontSize, float leftPos, float topPos){
		this.fontFamily = fontFamily;
		this.glyph = glyph;
		this.fontSizePt = fontSize;
		this.leftPos = leftPos;
		this.topPos = topPos;
	}
	
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public float getFontSizePt() {
		return fontSizePt;
	}
	public void setFontSizePt(float fontSizePt) {
		this.fontSizePt = fontSizePt;
	}
	public String getGlyph() {
		return glyph;
	}
	public void setGlyph(String glyph) {
		this.glyph = glyph;
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

	public void print(){
		System.out.println(this.glyph + " " + this.fontFamily + " " + this.fontSizePt + " " + this.leftPos + " " + this.topPos);
	}
	


	
	
}

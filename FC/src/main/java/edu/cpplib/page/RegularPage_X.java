package edu.cpplib.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.glyph.Glyph1;
import edu.cpplib.glyph.RawDataRegPage;

public class RegularPage_X {
	ArrayList<Glyph1> glyphsList;	
	String rawText, baseFontFamily, PDFPageNum, firstLine, boldStyle;
	int spacing, baseFontSizePt, realPageNum;
	float 	topMargin, bottomMargin, leftMargin, rightMargin, width, 
			height, pageNumPos, firstLinePos;
	
	public ArrayList<Glyph1> getGlyphsList() {
		return glyphsList;
	}

	public void setGlyphsList(ArrayList<Glyph1> glyphsList) {
		this.glyphsList = glyphsList;
	}

	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public float getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(float topMargin) {
		this.topMargin = topMargin;
	}

	public float getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(float bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public float getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(float leftMargin) {
		this.leftMargin = leftMargin;
	}

	public float getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(float rightMargin) {
		this.rightMargin = rightMargin;
	}

	public String getBaseFontFamily() {
		return baseFontFamily;
	}

	public void setBaseFontFamily(String baseFontFamily) {
		this.baseFontFamily = baseFontFamily;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getBaseFontSizePt() {
		return baseFontSizePt;
	}

	public void setBaseFontSizePt(int baseFontSizePt) {
		this.baseFontSizePt = baseFontSizePt;
	}

	public float getPageNumPos() {
		return pageNumPos;
	}

	public void setPageNumPos(float pageNumPos) {
		this.pageNumPos = pageNumPos;
	}

	public float getFirstLinePos() {
		return firstLinePos;
	}

	public void setFirstLinePos(float firstLinePos) {
		this.firstLinePos = firstLinePos;
	}

	public String getPDFPageNum() {
		return PDFPageNum;
	}

	public void setPDFPageNum(String PDFPageNum) {
		this.PDFPageNum = PDFPageNum;
	}

	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public String getBoldStyle() {
		return boldStyle;
	}

	public void setBoldStyle(String boldStyle) {
		this.boldStyle = boldStyle;
	}

	public static ArrayList<TextPosition> getTextPositions(PDDocument doc, int pageNum, boolean isLast) throws IOException{
		return RawDataRegPage.build(doc, pageNum, isLast);
	}
	
	
	public ArrayList<Glyph1> getGlyphsInfo(ArrayList<TextPosition> textPositions){
		ArrayList<Glyph1> glyphsList = new ArrayList<>();
		for(TextPosition t : textPositions){
			if(t.getDir() == 0){
				Glyph1 glyph = new Glyph1(t.toString(), t.getFont().getName(), t.getFontSizeInPt(), t.getXDirAdj(), t.getYDirAdj());
				glyphsList.add(glyph);
			}
		}
		return glyphsList;
	}

	public String getRaw(){
		String r = "";
		for(Glyph1 g : this.glyphsList){
			r += g.getGlyph();
		}
		r = r.replace(" ", "");
		return r;
	}
	
	public void findTBMargins(){
		ArrayList<Float> topPosListTmp = new ArrayList<Float>();
		
		for(Glyph1 g : this.glyphsList){
			if(StrFunc.containsChars(g.getGlyph())){
				topPosListTmp.add(g.getTopPos());
			}
		}
		
		Set<Float> topPosList = new HashSet<Float>(topPosListTmp);
		List<Float> sortedList = new ArrayList<Float>(topPosList);
		Collections.sort(sortedList);
//		for(float f : sortedList){
//			System.out.println(f);
//		}
		
		this.setTopMargin(sortedList.get(0)/72);
		this.setBottomMargin((this.height - sortedList.get(sortedList.size()-2))/72);
		
		this.setPageNumPos(sortedList.get(sortedList.size()-1));
		this.setFirstLinePos(sortedList.get(0));
	}
	
	public void findLRMargins(){
		ArrayList<Float> leftPosListTmp = new ArrayList<Float>();
		
		for(Glyph1 g : this.glyphsList){
			if(StrFunc.containsChars(g.getGlyph())){
				leftPosListTmp.add(g.getLeftPos());
			}
		}
		
		TreeSet<Float> leftPosList = new TreeSet<Float>(leftPosListTmp);
//		System.out.println(leftPosList.size());
//		for(float f : leftPosList){
//			System.out.println(f);
//		}
		
		this.setLeftMargin(leftPosList.first()/72);
		this.setRightMargin((this.width - leftPosList.last())/72);
	}
	
	public void findPageNum(){
		String pgNum = "";
//		System.out.println("pg num pos: " + getPageNumPos());
		for(Glyph1 g : this.glyphsList){
			if(g.getTopPos() == this.pageNumPos){
				pgNum += g.getGlyph();
			}
		}
		pgNum = pgNum.replace(" ", "");
		this.setPDFPageNum(pgNum);
	}
	
	public void findFirstLine(){
		String firstLine = "";
		for(Glyph1 g : this.glyphsList){
			if(g.getTopPos() == this.firstLinePos){
				firstLine += g.getGlyph();
			}
		}
		
		firstLine = firstLine.replace(" ", "");
		this.setFirstLine(firstLine);
	}
	
	
	
    public int findMode(ArrayList<Integer> list) {
		int mode = 0;		
		Set<Integer> mySet = new HashSet<Integer>(list);

		int most = 0;
		
		for(int s : mySet){	
			if(Collections.frequency(list, s) > most){
				most = Collections.frequency(list, s);
				mode = s;
			}
		}
				
		return mode;
    }
	
	public void findSpacing(){
		ArrayList<Float> topPosListTmp = new ArrayList<Float>();
		
		for(Glyph1 g : this.glyphsList){
			if(StrFunc.containsChars(g.getGlyph())){
				topPosListTmp.add(g.getTopPos());
			}
		}
		
		Set<Float> topPosList = new HashSet<Float>(topPosListTmp);
		List<Float> sortedList = new ArrayList<Float>(topPosList);
		Collections.sort(sortedList);
		
		ArrayList<Integer> spacingList = new ArrayList<Integer>();
		int diff = 0;
		for(int i=1; i<sortedList.size(); ++i){
			diff = (int) (sortedList.get(i) - sortedList.get(i-1));
			spacingList.add((int)Math.round(diff));
		}
		
//		System.out.println("spacing: ");
//		for(int i : spacingList){
//			System.out.println(i);
//		}

		int spacing = 0;
		if(spacingList.size() == 1){ // page contains only 1 line, then it does not have problems about spacing.
			if(this.baseFontSizePt == 10){
				spacing = 23;
			}
			else if(this.baseFontSizePt == 11){
				spacing = 25;
			}
			else if(this.baseFontSizePt == 12){
				spacing = 27;
			}
			else{
				spacing = 27;
			}
		}
		else if(spacingList.size() > 1 && spacingList.size() <= 5){
			spacing = (int) (sortedList.get(1) - sortedList.get(0));
		}
		else{
			spacing = findMode(spacingList);
		}
		
//		System.out.println("spacing used: " + spacing);
		this.setSpacing(spacing);
	}
	
	public void findBaseFontFamily(){
		
		ArrayList<String> fontFamilyUsed = new ArrayList<String>();
		String baseFontFamily = "";
		
		for(Glyph1 g : this.glyphsList){
			if(g.getFontFamily() != null){
				fontFamilyUsed.add(g.getFontFamily());
			}
		}
		
		Set<String> mySet = new HashSet<String>(fontFamilyUsed);

		int most = 0;
		
		for(String s : mySet){	
			if(Collections.frequency(fontFamilyUsed, s) > most){
				most = Collections.frequency(fontFamilyUsed, s);
				baseFontFamily = s;
			}
		}
		
		baseFontFamily = baseFontFamily.replace(" ","");
		baseFontFamily = baseFontFamily.toUpperCase();

		
		if (baseFontFamily.contains("TIMESNEWROMAN")) baseFontFamily = "TimesNewRoman";
		else if (baseFontFamily.contains("ARIAL")) baseFontFamily = "Arial";
		else if (baseFontFamily.contains("CALIBRI")) baseFontFamily = "Calibri";
		else if (baseFontFamily.contains("MATH")) baseFontFamily = "Math";
		else{
//			baseFontFamily = "Illegal";
		}
		
//		System.out.println("font family:" + baseFontFamily);
		this.setBaseFontFamily(baseFontFamily);
	}
	
	public void findBaseFontSize(){
		ArrayList<Float> fontSizeUsed = new ArrayList<Float>();
		float baseFontSize = 0;
		
		for(Glyph1 g : this.glyphsList){
			if(g.getFontFamily() != null){
				fontSizeUsed.add(g.getFontSizePt());
			}
		}
		
		Set<Float> mySet = new HashSet<Float>(fontSizeUsed);

		int most = 0;
		
		for(float s : mySet){	
			if(Collections.frequency(fontSizeUsed, s) > most){
				most = Collections.frequency(fontSizeUsed, s);
				baseFontSize = s;
			}
//			System.out.println(s + " " + Collections.frequency(fontFamilyUsed, s));
		}
		
		this.setBaseFontSizePt((int) baseFontSize);
	}
	
	public void build(PDDocument doc, int pageNum, boolean isLast) throws IOException{
		
		ArrayList<TextPosition> textPositions = getTextPositions(doc, pageNum, isLast);
		
		this.setGlyphsList(getGlyphsInfo(textPositions));
		this.setRawText(getRaw());
		this.setWidth(doc.getPage(pageNum).getMediaBox().getWidth());
		this.setHeight(doc.getPage(pageNum).getMediaBox().getHeight());
		
		this.findTBMargins();
		this.findLRMargins();
		
		this.findPageNum();
		this.findFirstLine();
		
		this.findBaseFontFamily();
		this.findBaseFontSize();
		this.findSpacing();
		
	}
	
	public static RegularPage_X getDataRetPg(PDDocument doc, int pageNum, boolean isLast) throws IOException{
		
		ArrayList<TextPosition> textPositions = getTextPositions(doc, pageNum, isLast);
		
		RegularPage_X regPage = new RegularPage_X();
		
		regPage.setGlyphsList(regPage.getGlyphsInfo(textPositions));
		regPage.setRawText(regPage.getRaw());
		regPage.setWidth(doc.getPage(pageNum).getMediaBox().getWidth());
		regPage.setHeight(doc.getPage(pageNum).getMediaBox().getHeight());
		
		regPage.findTBMargins();
		regPage.findLRMargins();
		
		regPage.findPageNum();
		regPage.findFirstLine();
		
		regPage.findBaseFontFamily();
		regPage.findBaseFontSize();
		regPage.findSpacing();
		
		return regPage;
		
	}
	
	
	
	public void printRaw(){
		System.out.println(getRawText());
	}
	
	public void printPageSize(){
		System.out.println("W x H :" + getWidth() + " x " + getHeight());
	}
	
	public void printPageMargins(){
		System.out.println("LRTB :" + getLeftMargin() + " - " + getRightMargin() + " - " + getTopMargin() + " - " + getBottomMargin());
	}
	
	public void printFirstLineAndPgNum(){
		System.out.println(getFirstLine() + ", " + getPDFPageNum());
	}
	
	public void printSpacingAndFont(){
		System.out.println("Spacing: " + getSpacing() + " , Font info:" + getBaseFontFamily() + "," + getBaseFontSizePt());
	}
}

package edu.cpplib.page;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.export.ImageGenerator;
import edu.cpplib.export.ItemResult;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.line.*;


public class Page {
	
	private float leftMargin, rightMargin, bottomMargin, fontSize;
	protected float topMargin, width, height;
	
	private String fontFamily, pgNum, firstLine;
	
	private int spacing;
	private int wordCount;
	
	protected ArrayList<Glyph> glyphs;
	protected ArrayList<Line> lines;
	
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

	public String getPgNum() {
		return pgNum;
	}

	public void setPgNum(String pgNum) {
		this.pgNum = pgNum;
	}

	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}

	
	
	public ArrayList<Glyph> getGlyphs() {
		return glyphs;
	}

	public void setGlyphs(ArrayList<Glyph> glyphs) {
		this.glyphs = glyphs;
	}
	
	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
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
	
	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}
	
	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}





	public class LineComparator implements Comparator<Line> {
	    public int compare(Line l1, Line l2) {
	        return new Float(l1.getTopPos()).compareTo(l2.getTopPos());
	    }
	}
	
	
	/*print stuff*/
	public void printGlyphs(ArrayList<Glyph> glyphs){
		for (Glyph g : glyphs){

//			if(g.getRotationDir() != 0){
				System.out.print(g.getGl());
//				System.out.print(g.getRotationDir());
//			}
			
			if(g.isEnter()){
				System.out.println();
			}
		}
	}

	public void printText(){
		for (Glyph g : this.glyphs){
				System.out.print(g.getGl());
			if(g.isEnter()){
				System.out.println();
			}
		}
	}
	
	

	
	public void printByLine(){
		for(Line line : this.lines){
//			if(StrFunc.containsChars(line.getText())){
//				line.print();
//				System.out.println();
				System.out.println(line.getText() + " " + wordCountPerLine(line.getText()));
//			}
		}
		
//		for(int i=0; i<this.lines.size(); ++i){
//			if(StrFunc.containsChars(lines.get(i).getText())){
//				System.out.println(i + lines.get(i).getText());
//			}
//		}
	}
	
	public static int wordCountPerLine(String line){
		char[] lineTextArr = line.toCharArray();
		int wordCounter = 0;
		for(char c : lineTextArr){
			if(c == ' '){
				wordCounter ++;
			}
		}
		return wordCounter;
	}
	
	public static int wordCountOfPage(ArrayList<Line> lines){
		int totalWordCount = 0;
		for(Line line : lines){
//			System.out.println(line.getText() + " " + wordCountPerLine(line.getText()));
			totalWordCount += wordCountPerLine(line.getText());
		}
		return totalWordCount;
	}
	
	public ArrayList<Line> deleteNoContentLines(ArrayList<Line> lines){
		ArrayList<Integer> noContentList = new ArrayList<Integer>();
		ArrayList<Line> ret = lines;
		
		for(int i=0; i<ret.size(); ++i){
			if(!StrFunc.containsChars(ret.get(i).getText())){
//				System.out.println(i + lines.get(i).getText());
				noContentList.add(i);
			}
		}
		
//		for(Integer i : noContentList){
//			System.out.println(i);
//		}
		
		for(Integer i : noContentList){
			ret.remove(i);
		}
		
//		for(Line l : this.lines){
//			System.out.println(l.getText());
//		}
		return ret;
	}
	
	public void delete(){
		ArrayList<Integer> noContentList = new ArrayList<Integer>();
		
		for(int i=0; i<this.lines.size(); ++i){
			if(!StrFunc.containsChars(this.lines.get(i).getText())){
//				System.out.println(i + lines.get(i).getText());
				noContentList.add(i);
			}
		}
		
//		for(Integer i : noContentList){
//			System.out.println(i);
//		}
		
		for(Integer i : noContentList){
			this.lines.remove(i);
		}
		
//		for(Line l : this.lines){
//			System.out.println(l.getText());
//		}

	}
	
	
	
	
	public String printMargin_Str(){
		String ret = "Page Margin (top/bottom/left/right): ";
		
		String t = Float.toString(getTopMargin());
		t = t.substring(0, Math.min(t.length(), 4));
		
		String b = Float.toString(getBottomMargin());
		b = b.substring(0, Math.min(b.length(), 4));
		
		String l = Float.toString(getLeftMargin());
		l = l.substring(0, Math.min(l.length(), 4));
		
		String r = Float.toString(getRightMargin());
		r = r.substring(0, Math.min(r.length(), 4));
		
		ret += t + " x " + b + " x " + l + " x " + r + "\r\n";
		
		return ret;
	}
	
	public void printMargin(){
		System.out.print("Page Margin (t/b/l/r): ");
		System.out.printf(	"%.2f x %.2f x %.2f x %.2f\n", 
							this.getTopMargin(), 
							this.getBottomMargin(),
							this.getLeftMargin(), 
							this.getRightMargin());
	}

	
	public String printPageSize_Str(){
		String ret = "Page Size (w x h): ";
		ret += this.getWidth() + " x " + this.getHeight() + "\r\n";
		return ret;
	}
	
	public void printPageSize(){
		System.out.print("Page Size: ");
		System.out.println(this.getWidth() + " x " + this.getHeight());
	}
	

	public void printFontInfo(){
		System.out.print("Font family & size: ");
		System.out.println(this.getFontFamily() + " x " + this.getFontSize());
	}
	
	
	public String printSpacing_Str(){
		String ret = "Line spacing: ";
		ret += this.getSpacing();
		return ret;
	}
	
	public void printSpacing(){
		System.out.print("Line spacing: ");
		System.out.println(this.getSpacing());
	}
	
	public String printPageInfo_Str(){
		String ret = "";
		ret += printPageSize_Str();
		ret += printMargin_Str();
		ret += printSpacing_Str();
		
		return ret;
	}
	

	
	
//	public void buildGlyphs(String filePath, PDPage page, int num) throws IOException{
//		
//		ArrayList<Glyph> glyphs = null;
//		glyphs = Glyph.build(filePath, page, num);
//		for(Glyph g : glyphs){
//			g.specialFontFamilyTrim();
//		}
//		this.setGlyphs(glyphs);
//	}

	public void buildGlyphs1(PDDocument doc, int num, boolean isLast) throws IOException{
		
		ArrayList<Glyph> glyphs = null;
		glyphs = Glyph.build(doc, num, isLast);
		for(Glyph g : glyphs){
			g.specialFontFamilyTrim();
		}
		this.setGlyphs(glyphs);
	}
	
	
	
	
	
//	public void definePageType(){
//		if(pgNum.contains("ii")){
//			setPageType("SIG");
//		}
//
//		if(firstLine != null){
//			String firstLineToUpper = firstLine.toUpperCase();
//			firstLineToUpper = StrFunc.getCharsOnly(firstLineToUpper);
//			if(firstLineToUpper.contains("ACKNOWLEDGMENTS")) setPageType("ACK");
//			else if(firstLineToUpper.contains("ABSTRACT")) setPageType("ABS");
//			else if(firstLineToUpper.contains("TABLEOFCONTENTS")) setPageType("TOC");
//			else if(firstLineToUpper.contains("LISTOFTABLES")) setPageType("LOT");
//			else if(firstLineToUpper.contains("LISTOFFIGURES")) setPageType("LOF");
//			else if(firstLineToUpper.contains("REFERENCE")) setPageType("REF");
//			else if(firstLineToUpper.contains("APPENDIX")) setPageType("APP");
//			else setPageType("GC");
//		}
//	}
	
//	public String getRightHalfText(){
//		String rightHalfText = "";
//		for(Glyph g : glyphs){
//			if(g.getLeftPos() >= 400){
//				rightHalfText += g.getGl();
//			}
//		}
//		return rightHalfText;
//	}
//	
//	public void identifyCatalog(){
//		ArrayList<Integer> numbersOfPage = new ArrayList<Integer>();
//		String str = getRightHalfText();
//		Pattern p = Pattern.compile("[0-9]+");
//		Matcher m = p.matcher(str);
//		while (m.find()) {
//		    int n = Integer.parseInt(m.group());
//		    numbersOfPage.add(n);
//		}
//	    
//		boolean flag = true;
//		for(int i=1; i<numbersOfPage.size(); ++i){
//			System.out.println(numbersOfPage.get(i));
//			int cur = numbersOfPage.get(i);
//			int prev = numbersOfPage.get(i-1);
//			if(cur < prev) flag = false; break; 
//		}
//		
//		System.out.println(flag);
//		
//	}
	
	

	/*Margins*/
	public int indexOfFirstLine(){
		int index = -1;
		for(int i=0; i<this.lines.size(); ++i){
			if(!lines.get(i).isEnter()){
				if(lines.get(i).containsChars())
					index = i;
					break;
			}
		}
		
		return index;
	}

	public float findMin(ArrayList<Float> list){
		float min = 10000;
		for(float i : list){
			if(i < min && i > 0) min = i;
		}
		return min;
	}
	
	public float findMax(ArrayList<Float> list){
		float max = -10000;
		for(float i : list){
			if(i > max && i != 0) max = i;
		}
		return max;
	}

//	public float findMean(ArrayList<Float> list){
//		float sum = 0;
//		for(float f : list){
//			sum += f;
//		}
//		
//		return sum/list.size();
//	}
//	
//	
//	public float findVariance(ArrayList<Float> list){
//		
//		float mean = findMean(list);
//		float temp = 0;
//		for(float a : list)
//		    temp += (a-mean)*(a-mean);
//		return temp/list.size();
//	}
//	
//	public float findSD(ArrayList<Float> list){
//		
//		return (float) Math.sqrt(findVariance(list));
//	}
	
	
	public float findTopMargin(){
		int firstLineIndex = indexOfFirstLine();
		float topMargin = this.lines.get(firstLineIndex).getTopPos();

		return topMargin/72;
		
	}

	public float findBottomMargin(){	
		int lastLineIndex = findLastLineIndex();
		float lastLineTopPos = this.lines.get(lastLineIndex).getTopPos();

		return (this.getHeight() - lastLineTopPos)/72;
		
	}

	public float findLeftMargin(){
		ArrayList<Float> leftPosList = new ArrayList<Float>();
		for (Line line : this.lines){
			if(!line.isEnter() && line.getLeftPos() > 0){
//				System.out.println(line.getText() + line.getLeftPos());
				leftPosList.add(line.getLeftPos());
			}
		}
		float min = findMin(leftPosList);

		return min/72;
		
	}

	public float findRightMargin(){
		ArrayList<Float> rightPosList = new ArrayList<Float>();
		for(Line line : this.lines){
			if(!line.isEnter()){
				rightPosList.add(this.getWidth() - line.getRightPos());
			}
		}

		float min = findMin(rightPosList);

		return min/72;
		
	}
	
	

	
	
	public ArrayList<Line> buildLines(){
//		each line should have the following information:
//		ok. content
//		ok. top position
//		ok. left position for right margin?
//		font bold
		
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Float> leftPos = new ArrayList<Float>();
		ArrayList<Float> topPos = new ArrayList<Float>();
		ArrayList<Float> rotationDir = new ArrayList<Float>();
		
		String text = "";
		for (Glyph g : this.glyphs){
			
			text += g.getGl();
			leftPos.add(g.getLeftPos());
			topPos.add(g.getTopPos());
			rotationDir.add(g.getRotationDir());
			
			if(g.isEnter()){
				text += ".#";
				rotationDir.add((float) -1);
			}
		}
		
		/*add text to each line*/
		String tmpLines[] = text.split("#");
		for (String i : tmpLines){
			Line tmp = new Line();
			tmp.setText(i);
			tmp.setEnter(tmp.determineEnter());
			tmp.elimDot();
			lines.add(tmp);
		}

		ArrayList<Integer> indexOf0 = new ArrayList<Integer>();
		
		/*set right position*/
		for(int i= 0; i< leftPos.size(); ++i){
			if(leftPos.get(i) == 0.0){
				indexOf0.add(i);
			}
		}

		lines.get(0).setLeftPos(leftPos.get(0));
		int lineIndex = 0;
		for (Integer i : indexOf0){

			try{

				lines.get(lineIndex).setRightPos(leftPos.get(i-1));

				lines.get(lineIndex+1).setLeftPos(leftPos.get(i+1));
				lineIndex ++;
			}
			catch(Exception e){
			}
			
		}

		/*set top position*/
		lines.get(0).setTopPos(topPos.get(0));
		lineIndex = 0;
		for (Integer i : indexOf0){
			try{
				lines.get(lineIndex+1).setTopPos(topPos.get(i+1));
				lineIndex ++;
			}
			catch(Exception e){
			}
			
		}
		
		Collections.sort(lines, new LineComparator());
		
		for(Line line : lines){
			line.noOpOnNullLine();
		}
		
		return lines;
	}

	
	
	/*last line doesn't include page number*/
	public int findLastLineIndex(){
		int last = lines.size()-1;
		for(int i=last; i>0; i--){
			if(lines.get(i).getText().length() > 10){
				last = i;
				break;
			}
		}
		return last;
	}
	
	
	public String findFontFamily(){
		ArrayList<String> fontFamilyUsed = new ArrayList<String>();
		String baseFontFamily = "";
		
		for(Glyph g : this.glyphs){
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
		
		return baseFontFamily;
		
	}
	
	public float findFontSize(){
		ArrayList<Float> fontSizeUsed = new ArrayList<Float>();
		float baseFontSize = 0;
		
		for(Glyph g : this.glyphs){
			if(g.getFontFamily() != null){
				fontSizeUsed.add(g.getFontSize());
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
				
		return baseFontSize;
	}
	
	
	
	
    public int findMode(ArrayList<Integer> list) {

		int baseLineSpacing = 0;		
		Set<Integer> mySet = new HashSet<Integer>(list);

		int most = 0;
		
		for(int s : mySet){	
			if(Collections.frequency(list, s) > most){
				most = Collections.frequency(list, s);
				baseLineSpacing = s;
			}
//			System.out.println(s + " " + Collections.frequency(fontFamilyUsed, s));
		}
				
		return baseLineSpacing;
    }
	
	
	/*note that different font size yields different spacing standard*/
	/*12pt: 27-29 is fine*/
    /*9pt: 24*/
    /*11pt: 24 25 28 017: different font size*/ 
	public int findSpacing(){
		ArrayList<Integer> topPosList = new ArrayList<Integer>();
		ArrayList<Integer> spacingList = new ArrayList<Integer>();
		float diff = 0;
		
		
		for(Line line : this.lines){
			if(line.getTopPos() != 0){
				topPosList.add((int)Math.round(line.getTopPos()));
			}
		}
		
		topPosList = (ArrayList<Integer>) topPosList.stream().distinct().collect(Collectors.toList());
		
		
		for(int i=1; i<topPosList.size(); ++i){
//			spacingList.add(lines.get(i).getTopPos());
			
//			if(!lines.get(i).isEnter() || !lines.get(i-1).isEnter()){
//				
////				System.out.println(lines.get(i).getText() + lines.get(i).getTopPos());
////				System.out.println(lines.get(i-1).getText() + lines.get(i-1).getTopPos());
//				
				diff = topPosList.get(i) - topPosList.get(i-1);
				spacingList.add((int)Math.round(diff));
////				System.out.println();
//			}		
		}
		
//		System.out.println("top pos list--------------");
//		for(float f : topPosList){
//			System.out.println(f);
//		}
//		
//		System.out.println("spacing list--------------");
//		
//		for(int i : spacingList){
//			System.out.println(i);
//		}
		
		int spacing = 0;
		if(spacingList.size() == 1){ // page contains only 1 line, then it does not have problems about spacing.
			if(this.fontSize == 10){
				spacing = 23;
			}
			else if(this.fontSize == 11){
				spacing = 25;
			}
			else if(this.fontSize == 12){
				spacing = 27;
			}
			else{
				spacing = 27;
			}
		}
		else if(spacingList.size() > 1 && spacingList.size() <= 5){
			spacing = topPosList.get(2) - topPosList.get(1);
		}
		else{
			spacing = findMode(spacingList);
		}
		
//		System.out.println("spacing used: " + spacing);
		return spacing;
	}
	
	
	private String findFirstLine(){
		String firstLine = "";
		for(Line line : lines){
			if(line.containsChars()){
//				System.out.println(line.getText());
				firstLine = line.getText();

				break;
			}
//			break;
		}
		
//		System.out.println(firstLine);
//		firstLine = Line.getCharsOnly(firstLine);
//		firstLine = firstLine.toUpperCase();
		return firstLine;
	}
	
	

	

	private String findPgNum(){
		int last = lines.size()-1;
		String num = "";
		for(int i=last; i>0; i--){
			if(lines.get(i).containsChars()){
				num = lines.get(i).getText();
				num = Line.getCharsOnly(num);
				break;
			}
		}
//		System.out.println("num len:" + num.length());
		if(num.length() > 5) num = ""; //when page number is missing, last line will be text, if so, discard
		return num;
	}
	
	public String getBoldChars(){
		String bold = "";
//		System.out.println(this.lines.get(0).getTopPos());
		for(Glyph g : this.glyphs){
			if(g.isBold()){ //why did i set get top pos <= 90? && g.getTopPos() <= 90
				bold += g.getGl();
			}
		}
		return bold;
	}
	

	
	public void setMargins(){
		float topMargin = findTopMargin();
		setTopMargin(topMargin);
		
		float bottomMargin = findBottomMargin();
		setBottomMargin(bottomMargin);
		
		float leftMargin = findLeftMargin();
		setLeftMargin(leftMargin);
		
		float rightMargin = findRightMargin();
		setRightMargin(rightMargin);
	}
	
	public boolean checkTopMargin(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.9 <= topMargin);
	}
	
	public boolean checkBottomMargin(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.9 <= bottomMargin);
	}
	
	public boolean checkLeftMargin(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (1.4 <= leftMargin);
	}
	
	public boolean checkRightMargin(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.8 <= rightMargin);
	}
	
	
	public boolean checkRightMarginRotated(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.9 <= rightMargin);
	}
	
	public boolean checkLeftMarginRotated(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.9 <= leftMargin);
	}
	
	public boolean checkTopMarginRotated(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (1.4 <= topMargin);
	}
	
	public boolean checkBottomMarginRotated(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * */
		return (0.8 <= bottomMargin);
	}
	
	
	
	
	
	public void setFontInfo(){
		String ff = findFontFamily();
		setFontFamily(ff);
		
		float fs = findFontSize();
		setFontSize(fs);
	}
	
	public void setSpacing(){
		int spacing = findSpacing();
		setSpacing(spacing);
	}
	
	
	public static Page getDataRetPg(PDDocument doc, int num, boolean isLast) throws IOException{
		
//		buildGlyphs(filePath, page, num);
		Page retPage = new Page();
		retPage.buildGlyphs1(doc, num, isLast);
		retPage.lines = retPage.buildLines();

		retPage.setWidth(doc.getPage(num).getMediaBox().getWidth());
		retPage.setHeight(doc.getPage(num).getMediaBox().getHeight());

		retPage.setMargins();
		retPage.setFontInfo();
		retPage.setSpacing();
		
		String firstLine = retPage.findFirstLine();
		retPage.setFirstLine(firstLine);
		
		String pgNum = retPage.findPgNum();
		retPage.setPgNum(pgNum);
		
		retPage.setWordCount(wordCountOfPage(retPage.lines));
		
		return retPage;
//		definePageType();
	}
	
	
	
	
	public void getData(PDDocument doc, int num, boolean isLast) throws IOException{
		
//		buildGlyphs(filePath, page, num);
		buildGlyphs1(doc, num, isLast);
		lines = buildLines();

		setWidth(doc.getPage(num).getMediaBox().getWidth());
		setHeight(doc.getPage(num).getMediaBox().getHeight());

		setMargins();
		setFontInfo();
		setSpacing();
		
		String firstLine = findFirstLine();
		setFirstLine(firstLine);
		
		String pgNum = findPgNum();
		setPgNum(pgNum);
		
//		definePageType();
	}
	
	public void getDataFirstLine(PDDocument doc, int num, boolean isLast) throws IOException{
		
		buildGlyphs1(doc, num, isLast);
		lines = buildLines();

//		setWidth(page.getMediaBox().getWidth());
//		setHeight(page.getMediaBox().getHeight());

//		setMargins();
		setFontInfo();
//		setSpacing();
		
		String firstLine = findFirstLine();
		setFirstLine(firstLine);
		
		String pgNum = findPgNum();
		setPgNum(pgNum);
		
//		definePageType();
	}
	
	
	

	public String getRawText() throws IOException{
		
//		buildGlyphs(filePath, page, num);
		
		String raw = "";
		for(Glyph g : glyphs){
			if(g.isEnter()) continue;
			if(g.isSpace()) continue;
			raw += g.getGl();
		}
		
		raw = raw.trim();
		
		return raw;
	}
	
	
	

	
	public void printPageInfo(){
		printPageSize();
		printMargin();
		printFontInfo();
		printSpacing();
//		printGlyphs();
//		System.out.println("Page num on the page:" + getPgNum());
	}

	
	public boolean checkSpacing(){
		
		if(this.fontSize == 10){
			return 21 <= this.spacing && this.spacing <= 26;
		}
		else if(this.fontSize == 11){
			return 23 <= this.spacing && this.spacing <= 28;
		}
		else if(this.fontSize == 12){
			return 26 <= this.spacing && this.spacing <= 31;
		}
		else{
			return false; //spacing wrong or font size wrong
		}
		
	}
	
//	
//	public void findDifferentLineSpacing(){
//
//		ArrayList<Integer> topPosList = new ArrayList<Integer>();
//		ArrayList<Integer> spacingList = new ArrayList<Integer>();
//		float diff = 0;
//		
//		
//		for(Line line : this.lines){
//			if(line.getTopPos() != 0){
//				topPosList.add((int)Math.round(line.getTopPos()));
//			}
//		}
//		
//		topPosList = (ArrayList<Integer>) topPosList.stream().distinct().collect(Collectors.toList());
//		
//		
//		for(int i=1; i<topPosList.size(); ++i){
//				diff = topPosList.get(i) - topPosList.get(i-1);
//				spacingList.add((int)Math.round(diff));
//		}
//		
//		for(float f : topPosList){
//			System.out.println(f);
//		}
//		
//		System.out.println("--------------");
//		
//		for(int i=0; i<spacingList.size(); ++i){
//			System.out.println("Line " + i);
//			int i1 = i+1;
//			System.out.println("Line " + i1);
//			System.out.println("Spacing: " + spacingList.get(i));
//			
//		}
//		
//		int spacing = 0;
//		if(spacingList.size() == 1){ // page contains only 1 line, then it does not have problems about spacing.
//			spacing = 27;
//		}
//		else if(spacingList.size() == 2){
//			spacing = topPosList.get(1) - topPosList.get(0);
//			//spacing list is 2 means there are 3 lines, 2 lines in content, 3rd line is page number
//			//page number can't be counted
//		}
//		else{
//			spacing = findMode(spacingList);
//		}
//		
//		System.out.println("spacing used: " + spacing);
//	}
//	
	
	
	public void findDifferentSpacing(){
		ArrayList<Integer> topPosList = new ArrayList<Integer>();		
		for(Line line : this.lines){
			if(line.getTopPos() != 0){
				topPosList.add((int)Math.round(line.getTopPos()));
			}
		}
		
		int diff = 0;
//		System.out.println("Spacing: " + getSpacing());
//		for(int i=1; i<topPosList.size(); ++i){
//			diff = topPosList.get(i) - topPosList.get(i-1);
//			if(diff > getSpacing()+3){
//				System.out.printf("i: %d %d, i-1: %d %d: ", i, topPosList.get(i), i-1, topPosList.get(i-1));
//				System.out.println("[i-1] " + lines.get(i-1).getText() + "[i]" + lines.get(i).getText());
//				System.out.println("spacing too large: " + diff);
//			}
//			else if(diff < getSpacing()-3){
//				System.out.printf("i: %d %d, i-1: %d %d: ", i, topPosList.get(i), i-1, topPosList.get(i-1));
//				System.out.println("[i-1] " + lines.get(i-1).getText() + "[i]" + lines.get(i).getText());
//				System.out.println("spacing too narrow: " + diff);
//			}
//		}
		/*
		 * if line[i].topPos - line[i-1].topPos > baseSpacing+5: these two lines' spacing is too large
		 * if line[i].topPos - line[i-1].topPos > baseSpacing-5: these two lines' spacing is too narrow, might be wrapped lines
		 * return:
		 * line[i]and[i-1].text, topPos and leftPos
		 * then imageGenerator will make image
		 */
		
		
		
	}
	
	
	
	public boolean isPgNumRoman(){
		char[] chars = pgNum.toCharArray();
//		String s = "";
		boolean flag = false;
		
		for(char c : chars){
			if(c == ' ') continue;
			if(c == 'i' || c == 'v' || c == 'x') {
				flag = true; 
				break;
			}
		}
		return flag;
	}
	
	
	public int parseRomanPgNum(String roman){
		
		Hashtable<Character, Integer> table = new Hashtable<Character, Integer>();
	    table.put('i',1);
	    table.put('x',10);
	    table.put('v',5);
	    
	    int intNum = 0;
	    int prev = 0;
	    
	    for(int i = roman.length()-1; i>=0 ; i--){
	    	try{
				int temp = table.get(roman.charAt(i));
				if(temp < prev)
				    intNum -= temp;
				else
				    intNum += temp;
				prev = temp;
	    	}
			catch(Exception e){
			}
	    }
	    
	    return intNum;
	    
	}

	public int convertPDFPgNum(String num, int contentStartPgNum){
		String numTrim = num.trim();
		if(numTrim.length() != 0){
			int intNum = Integer.parseInt(num);
			return intNum -1 + contentStartPgNum;
		}
		else{
			return -1;
		}
	}
	
	
	public boolean checkPgNum(int realPgNum, int contentStartPgNum){
		/*
		 * roman page num ii~xx
		 * regular page num 1~500
		 * 
		 * */
//		System.out.println("real page num: " + realPgNum);
		
		boolean isRoman = isPgNumRoman();
		int convertedIntPgNum = -1;
		if(isRoman){
//			System.out.print("Roman: " + pgNum + "--");
			convertedIntPgNum = parseRomanPgNum(pgNum);
		}
		else{
			if(StrFunc.INTIsParsed(pgNum)){
//				System.out.print("Integer num: " + pgNum + "--");
				convertedIntPgNum = convertPDFPgNum(pgNum, contentStartPgNum);
			}
		}
		
//		System.out.println("After conversion: " + convertedIntPgNum);
		
		return convertedIntPgNum == realPgNum;
	}
	
	
	public int convertPgNumToRealPDFPgNum(int contentStartPgNum){
		boolean isRoman = isPgNumRoman();
		int convertedIntPgNum = -1;
		if(isRoman){
//			System.out.print("Roman: " + pgNum + "--");
			convertedIntPgNum = parseRomanPgNum(pgNum);
		}
		else{
			System.out.print("Integer num: " + pgNum + "--");
			convertedIntPgNum = convertPDFPgNum(pgNum, contentStartPgNum);
		}
		return convertedIntPgNum;
	}
	

	
	public boolean checkPageWidth(){
		return (610 <= width && width <= 613);
	}

	public boolean checkPageHeight(){
		return (789 <= height && height <= 793);
	}
	
	public boolean isRotated(){
		return (789 <= width && width <= 793 && 611 <= height && height <= 613);
	}
	
	public boolean checkFontSize(){
		return (10 <= fontSize && fontSize <= 12);
	}
	
	public boolean checkFontFamily(){
//		String fontFamilyStr = fontFamily.replace(" ","");
//		fontFamilyStr = fontFamilyStr.toUpperCase();
//		
		if (fontFamily.equals("TimesNewRoman")) return true;
		else if (fontFamily.equals("Arial")) return true;
		else if (fontFamily.equals("Calibri")) return true;
		else if (fontFamily.equals("Math")) return true;
		else return false;
	}
	

	public ArrayList<Glyph> glyphsUsingDifferentFontFamily(){
		
		ArrayList<Glyph> differentFontStyleGlyphList = null;
		
		try{
			differentFontStyleGlyphList = new ArrayList<Glyph>();
			for(Glyph g : glyphs){
				if(g.getFontFamily() != null){
					String glyphFontFamily = g.getFontFamily().replace(" ", "");
					glyphFontFamily = glyphFontFamily.toUpperCase();
//					System.out.println(glyphFontFamily);
//					System.out.println(fontFamily.toUpperCase());
					if(!glyphFontFamily.contains(fontFamily.toUpperCase())){
						/*
						 * fontfamily is base fontfamily- that is the one most used in this page
						 * if fontfamily used is eligible
						 * then check if page contains other fontfamily which is different from the base fontfamily, or not eligible ffamily.  
						 * */

						if(!g.isSpace()){
								Glyph differentFontStyleGlyph = new Glyph();
								
								differentFontStyleGlyph.setFontFamily(g.getFontFamily());
								differentFontStyleGlyph.setGl(g.getGl());
								differentFontStyleGlyph.setLeftPos(g.getLeftPos());
								differentFontStyleGlyph.setTopPos(g.getTopPos());
								differentFontStyleGlyph.setFontSize(g.getFontSize());
								
								differentFontStyleGlyphList.add(differentFontStyleGlyph);
						}
					}
					
//					System.out.println("DIFFERENT FONT SIZE::::::::::::::::");
					
//					System.out.println(g.getFontSize() + ", " + fontSize);
					if(g.getFontSize() != fontSize){
						
						if(!g.isSpace()){
							Glyph differentFontStyleGlyph = new Glyph();
//							differentFontStyleGlyph.setFontFamily(g.getFontFamily());
							differentFontStyleGlyph.setGl(g.getGl());
							differentFontStyleGlyph.setLeftPos(g.getLeftPos());
							differentFontStyleGlyph.setTopPos(g.getTopPos());
							differentFontStyleGlyph.setFontSize(g.getFontSize());
							
							differentFontStyleGlyphList.add(differentFontStyleGlyph);
						}
					}
					
					
				}
			}
//			System.out.println(differentFontStyleGlyph);
			
		}
		catch(Exception e){
		}

		
//		ArrayList<Glyph> differentFontStyleGlyphList1 = new ArrayList<Glyph>();
//		try{
//			for(Glyph g : differentFontStyleGlyphList){
//				if(g.getFontFamily().contains("Math") || g.getFontFamily().contains("MATH")) continue;
//				else if(g.getFontFamily().contains("Symbol") || g.getFontFamily().contains("SYMBOL")) continue;
//				else if(g.getFontSize() == 7) continue; //super/sub script
//				else{
//					Glyph g1 = new Glyph();
//					g1.setGl(g.getGl());
//					g1.setLeftPos(g.getLeftPos());
//					g1.setTopPos(g.getTopPos());
//					g1.setFontSize(g.getFontSize());
//					
//					differentFontStyleGlyphList1.add(g1);
//				}
//	    		
//			}
//		}
//		catch(Exception e){
//		}

		
//		for(Glyph g : differentFontStyleGlyphList){
//			System.out.print(g.getGl());
//		}
//		System.out.println("");
//		for(Glyph g : differentFontStyleGlyphList1){
//			System.out.print(g.getGl());
//		}
		
		
		return differentFontStyleGlyphList;

		
	}

	
	public String findRightPart(){
		String rightPart = "";
		
		for(Glyph g : this.glyphs){
			if(g.getLeftPos() >= 500){
				rightPart += g.getGl();
			}
		}
		
		return rightPart;
	}
	
	public ArrayList<Integer> getNumsFromStr(String str){
		String pattern = "[1-9][0-9]{0,2}";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		ArrayList<Integer> numberList = new ArrayList<Integer>();
		
		while (m.find()) {
			int i = Integer.parseInt(m.group());
			numberList.add(i);
		}
		
		return numberList;
	}

	
	public boolean isIncreasing(ArrayList<Integer> numberList){
		
		if(numberList.size() == 0) return false;
		else if(numberList.size() == 1) return true;
		
		else{
			for(int i=1; i<numberList.size(); ++i){
				if(numberList.get(i) < numberList.get(i-1)) {
					return false;
				}
			}
			return true;
		}

	}
	
	public ArrayList<Integer> getNumListOfPage(){
		String rightHalfRaw = "";
		for(Glyph g : this.glyphs){
			if(g.getLeftPos() >= 300){
				rightHalfRaw += g.getGl();
			}
		}
		
		rightHalfRaw = rightHalfRaw.replace("\u2026", "..."); //replace horizontal ellipsis
		rightHalfRaw = rightHalfRaw.replace(" .", ".");
		rightHalfRaw = rightHalfRaw.replace(". ", ".");
		
//		System.out.println("right half raw " + rightHalfRaw);
		String potentialNums = "";
		String pattern = "(\\.{4})(\\d+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(rightHalfRaw);
		while (m.find()) {
			potentialNums += m.group();
//	        System.out.println("Found value: " + m.group());
	    }
		
		ArrayList<Integer> numListOfPage = getNumsFromStr(potentialNums);
		return numListOfPage;
	}
	
	public boolean isContentStart(){

		String rightPart = findRightPart();
//		System.out.println("right part" + rightPart);
		
		float numberInRightPart = 0;
		float charInRightPart = 0;
		
		char[] rightPartChar = rightPart.toCharArray();
		
		for(char c : rightPartChar){
			if(Character.isAlphabetic(c)){
				charInRightPart ++;
			}
			if(Character.isDigit(c)){
				numberInRightPart ++;
			}
		}
		
//		System.out.println("number:" + numberInRightPart);
//		System.out.println("char:" + charInRightPart);
		
		float rightPartLen = rightPart.length();
//		System.out.println("right part len" + rightPartLen);
		
		float numberRatio = numberInRightPart/rightPartLen;
		float charRatio = charInRightPart/rightPartLen;
		
//		System.out.println("number ratio:" + numberRatio);
//		System.out.println("char ratio:" + charRatio);
		
		
		ArrayList<Integer> numListOfPage = getNumListOfPage();
//		System.out.println("num list of page");
		for (Integer n : numListOfPage){
//			System.out.print(n + " ") ;
		}
		
		boolean isNumListIncreasing = isIncreasing(numListOfPage);

		float lastLinePosPx = lines.get(this.findLastLineIndex()).getTopPos();
//		System.out.print("char ratio, hasIncreaingNum, wordcount, last line px: ");
//		System.out.println(charRatio + " " + isNumListIncreasing + " " + this.wordCount + " " + lastLinePosPx);
		
		if(numberRatio < charRatio){
//			System.out.println("MIGHT BE CONTENT START PAGE!!!!!!!!!!!\n\n");
			
			if(charRatio >= 0.4 && !isNumListIncreasing && lastLinePosPx > 550){
				return true; // it is main body start
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		
		
//		String pattern = "[1-9][0-9]{0,2}";
//		Pattern r = Pattern.compile(pattern);
//		Matcher m = r.matcher(rightPart);
//		
//		ArrayList<Integer> pageNumberList = new ArrayList<Integer>();
//		
//		while(m.find()){
//			Integer i = Integer.parseInt(m.group());
//			pageNumberList.add(i);
//		}
//		
//		for(int i : pageNumberList){
//			System.out.println(i);
//		}

	}
	

	public void check(int realPgNum, int contentStartPgNum) throws IOException{
		
//		definePageType();
		
		
		boolean checkPgNum = checkPgNum(realPgNum, contentStartPgNum);
		
		if(isRotated()){
			System.out.println("Page rotated"); //when check rotation, it checked page size already
			/*
			 * when page is rotated, margins standard is different
			 * page number cannot be parsed, need to take a look of those rotated glyphs
			 * 
			 * */
			System.out.println("TopMargin: " + checkTopMarginRotated());
			System.out.println("Bottom margin: " + checkBottomMarginRotated());
			System.out.println("Left margin: " + checkLeftMarginRotated());
			System.out.println("Right margin: " + checkRightMarginRotated());
			
			
		}
		
		else{
			System.out.println("Page width: " + checkPageWidth());
			System.out.println("Page height: " + checkPageHeight());
			System.out.println("TopMargin: " + checkTopMargin());
			System.out.println("Bottom margin: " + checkBottomMargin());
			System.out.println("Left margin: " + checkLeftMargin());
			System.out.println("Right margin: " + checkRightMargin());
		}
		
		System.out.println("Page number check: " + checkPgNum);
		System.out.println("Parsed page number: " + pgNum);
		
		if(!checkPgNum){
			System.out.println("Parsed page number: " + pgNum);
			//if pgNum shows nothing here, which means program cannot parse page number for some reasons
		}


		System.out.println("Font size used: <" + fontSize + "> eligible? " + checkFontSize());
		
		boolean checkFontFamily = checkFontFamily();
		System.out.println("Base Font family used: <" + fontFamily + "> eligible? " + checkFontFamily);
		if(checkFontFamily){
			/*
			 * make sure user did not use different font family
			 * */
			ArrayList<Glyph> differentFontStyle = glyphsUsingDifferentFontFamily();
			
			int numOfDifferentFontStyle = differentFontStyle.size();
			
			if(numOfDifferentFontStyle == 0){
				System.out.println("Font size and font family are consistent");
			}
			else{
				System.out.println("Font size and font family may not consistent:");
				
				for(Glyph g : differentFontStyle){
					System.out.print(g.getGl() + "," + g.getFontFamily() + "," + g.getFontSize());
				}

				
				/*print wrong font family used in page to a png*/
//				ImageGenerator img = new ImageGenerator();
//				int realPDFPgNum = this.convertPgNumToRealPDFPgNum(contentStartPgNum);
//				String pdfPgNumInStr = Integer.toString(realPDFPgNum);
//				img.run(differentFontStyle, pdfPgNumInStr);
			}
			
		}
//		this.printGlyphs(glyphs);
	}


	public String check_PrintToTxt(int realPgNum, int contentStartPgNum) throws IOException{
			
	//		definePageType();
			String regularCheckStr = "";
			
			boolean checkPgNum = checkPgNum(realPgNum, contentStartPgNum);
			
			if(isRotated()){
				regularCheckStr += "Page is rotated\r\n";
				/*
				 * when page is rotated, margins standard is different
				 * page number cannot be parsed, need to take a look of those rotated glyphs
				 * 
				 * */
				
				regularCheckStr += "Page width: " + checkPageWidth() + "\r\n";
				regularCheckStr += "Page height: " + checkPageHeight() + "\r\n";
				regularCheckStr += "Top margin: " + checkTopMarginRotated() + "\r\n";
				regularCheckStr += "Bottom margin: " + checkBottomMarginRotated() + "\r\n";
				regularCheckStr += "Left margin: " + checkLeftMarginRotated() + "\r\n";
				regularCheckStr += "Right margin: " + checkRightMarginRotated() + "\r\n";
				
				
			}
			
			else{
				
				regularCheckStr += "Page width: " + checkPageWidth() + "\r\n";
				regularCheckStr += "Page height: " + checkPageHeight() + "\r\n";
				regularCheckStr += "TopMargin: " + checkTopMargin() + "\r\n";
				regularCheckStr += "Left margin: " + checkLeftMargin() + "\r\n";
				regularCheckStr += "Right margin: " + checkRightMargin() +"\r\n";
				
				
			}
			
			
			regularCheckStr += "Page number check: " + checkPgNum + "\r\n";
			regularCheckStr += "Parsed page number: " + pgNum + "\r\n";
			
			
			if(!checkPgNum){
				regularCheckStr += "Parsed page number: " + pgNum + "\r\n";
				regularCheckStr += "(if pgNum shows nothing here, which means program cannot parse page number for some reasons)\r\n";
				//if pgNum shows nothing here, which means program cannot parse page number for some reasons
			}
	
	
			regularCheckStr += "Font size used <" + fontSize + ">: " + checkFontSize() + "\r\n";
			
			
			boolean checkFontFamily = checkFontFamily();
			regularCheckStr += "Base Font family used <" + fontFamily + ">: " + checkFontFamily + "\r\n";
			
			if(checkFontFamily){
				/*
				 * make sure user did not use different font family
				 * */
				ArrayList<Glyph> differentFontStyle = glyphsUsingDifferentFontFamily();
				
				int numOfDifferentFontStyle = differentFontStyle.size();
				
				if(numOfDifferentFontStyle == 0){
					regularCheckStr += "Font size and font family are consistent in this page: true\r\n";
				}
				else{
					regularCheckStr += "Font size and font family are consistent in this page: false\r\n";
					
					for(Glyph g : differentFontStyle){
						regularCheckStr += g.getGl() + "," + g.getFontFamily() + "," + g.getFontSize() + "\r\n";
					}
	
					
					/*print wrong font family used in page to a png*/
	//				ImageGenerator img = new ImageGenerator();
	//				int realPDFPgNum = this.convertPgNumToRealPDFPgNum(contentStartPgNum);
	//				String pdfPgNumInStr = Integer.toString(realPDFPgNum);
	//				img.run(differentFontStyle, pdfPgNumInStr);
				}
				
			}
	//		this.printGlyphs(glyphs);
			
			regularCheckStr += "-------------------------------------------\r\n";
			return regularCheckStr;
		}
	

	
	public ArrayList<ItemResult> rotatedMarginsResult(){
		ArrayList<ItemResult> margins = new ArrayList<ItemResult>();
		ItemResult item = null;
		
		item = new ItemResult("Page is rotated", this.getWidth()/72 + " x " + this.getHeight()/72,
				this.isRotated(), "Letter size: 11 x 8.5", "-");
		margins.add(item);
		
		item = new ItemResult("Top margin", Float.toString(this.getTopMargin()) + " (inches)", 
						checkTopMarginRotated(), ">=1.5 inches", "");
		margins.add(item);
		
		
		item = new ItemResult("Bottom margin", Float.toString(this.getBottomMargin()) + " (inches)", 
						checkBottomMarginRotated(), ">=1 inch", "");
		margins.add(item);
		
		
		item = new ItemResult("Left margin", Float.toString(this.getLeftMargin()) + " (inches)", 
						checkLeftMarginRotated(), ">=1 inches", "");
		margins.add(item);
		
		
		item = new ItemResult("Right margin", Float.toString(this.getRightMargin()) + " (inches)", 
						checkRightMarginRotated(), ">=1 inches", "");
		margins.add(item);
		
		return margins;
	}
	
	public ArrayList<ItemResult> regularMarginsResult(){
		ArrayList<ItemResult> margins = new ArrayList<ItemResult>();
		ItemResult item = null;
		
		item = new ItemResult("Page size", Float.toString(this.getWidth()/72) + " x " + Float.toString(this.getHeight()/72),
				this.checkPageWidth() && this.checkPageHeight(), "Letter size: 8.5 x 11", "");
		margins.add(item);
		
		item = new ItemResult("Top margin", Float.toString(this.getTopMargin()) + " (inches)", 
						checkTopMargin(), ">=1 inches", "");
		margins.add(item);
		
		
		item = new ItemResult("Bottom margin", Float.toString(this.getBottomMargin()) + " (inches)", 
						checkBottomMargin(), ">=1 inch", "");
		margins.add(item);
		
		
		item = new ItemResult("Left margin", Float.toString(this.getLeftMargin()) + " (inches)", 
						checkLeftMargin(), ">=1.5 inches", "");
		margins.add(item);
		
		
		item = new ItemResult("Right margin", Float.toString(this.getRightMargin()) + " (inches)", 
							checkRightMargin(), ">=1 inches", "");
		margins.add(item);
		
		return margins;
	}
	
	
	
	public ArrayList<ItemResult> check_PrintToList(int realPgNum, int contentStartPgNum, String dir, PDFRenderer pdfRenderer) throws IOException{
			
			ArrayList<ItemResult> pgrList = new ArrayList<ItemResult>();
			ItemResult singleItem = null;
			
			if(isRotated()){
				ArrayList<ItemResult> rotatedMarginsResult = rotatedMarginsResult();
				pgrList.addAll(rotatedMarginsResult);
				/*
				 * when page is rotated, margins standard is different
				 * page number cannot be parsed, need to take a look of those rotated glyphs
				 * 
				 * */
				
			}
			
			else{
				ArrayList<ItemResult> regularMarginsResult = regularMarginsResult();
				pgrList.addAll(regularMarginsResult);
//				System.out.println("not rotated");
			}
			
//			System.out.println("Spacing");
			singleItem = new ItemResult("Spacing", "" + this.getSpacing(), this.checkSpacing(), 
										"Double spacing standard: 10pt: 21-26; 11pt: 23-28; 12pt: 26-31", "-");
			pgrList.add(singleItem);

			
//			System.out.println("Pg num");
			if(!this.getPgNum().equals("")){
//				System.out.println("Pg num not null, real and start:");
				boolean checkPgNum = checkPgNum(realPgNum, contentStartPgNum);
//				System.out.println(realPgNum);
//				System.out.println(contentStartPgNum);
				singleItem = new ItemResult("Page number", this.getPgNum(), checkPgNum, 
											"value is parsed page num. (if pgNum shows nothing here, "
											+ "which means program cannot parse page number for some reasons)", "-");
				pgrList.add(singleItem);
			}
			else{
//				System.out.println("Pg num yes");
				singleItem = new ItemResult("Page number", this.getPgNum(), false, 
						"value is parsed page num. (if pgNum shows nothing here, "
						+ "which means program cannot parse page number for some reasons)", "-");
				pgrList.add(singleItem);
			}

//			System.out.println("Font");
			
			// font size and font family check is based on base font styles
			boolean checkFontSize = checkFontSize();
			singleItem = new ItemResult("Font size", "" + this.getFontSize(), checkFontSize(), "10pt~12pt", "-");
			pgrList.add(singleItem);
			
			boolean checkFontFamily = checkFontFamily();
			singleItem = new ItemResult("Font family", this.getFontFamily(), checkFontFamily, "Times New Roman, Arial, Calibri", "-");
			pgrList.add(singleItem);
			
			ArrayList<ItemResult> fontStylesResult = check_PrintFontStylesImg
					(realPgNum, contentStartPgNum, dir, checkFontFamily, checkFontSize, pdfRenderer);
			pgrList.addAll(fontStylesResult);
			
			/*
			 * if check font style is true
			 * continue to check that, if there are chars with wrong font style
			 * add result to pgrList and print out image (origin pdf page and wrong font stlyes, then combine them)
			 */

			
			return pgrList;
		}
	
		public ArrayList<ItemResult> check_PrintFontStylesImg( 
				int realPgNum, int contentStartPgNum, String dir, boolean checkFontFamily, boolean checkFontSize, PDFRenderer pdfRenderer) 
						throws IOException{
			
			ArrayList<ItemResult> fontStylesResult = new ArrayList<ItemResult>();
			ItemResult singleItem;
			
			if(checkFontFamily){
			/*
			 * make sure user did not use different font family
			 * */
//			System.out.println("make sure user did not use different font family");
			
			ArrayList<Glyph> differentFontStyle = glyphsUsingDifferentFontFamily();
			int numOfDifferentFontStyle = differentFontStyle.size();
			if(numOfDifferentFontStyle == 0){
				singleItem = new ItemResult("Font style consistency", "-", true, "Student does not use different font style", "-");
				fontStylesResult.add(singleItem);
			}
			else{
				singleItem = new ItemResult("Font style consistency", "-", false, "Student MIGHT use different font style", "-");
				fontStylesResult.add(singleItem);
				for(Glyph g : differentFontStyle){
					if(g.getFontFamily()!= null) {
						if(g.getFontFamily().contains("Math") || g.getFontFamily().contains("MATH")) continue;
						if(g.getFontFamily().contains("Symbol") || g.getFontFamily().contains("SYMBOL")) continue;
						singleItem = new ItemResult("Different style used", 
													g.getGl() + "--" + g.getFontFamily() + "--" + g.getFontSize(), false, "-", "-");
						fontStylesResult.add(singleItem);
					}
				}

				
				/*print wrong font family used in page to a png*/
//				System.out.println("img output");
				ImageGenerator img = new ImageGenerator();
//				System.out.println(contentStartPgNum + "-img outputed");
//				int realPDFPgNum = this.convertPgNumToRealPDFPgNum(contentStartPgNum);
//				System.out.println(realPgNum + "-img outputed");
				String pdfPgNumInStr = Integer.toString(realPgNum);
				img.run(differentFontStyle, pdfPgNumInStr, dir);

//				PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
				
//				for (int page = 0; page < pdfDoc.getNumberOfPages(); ++page)
//				{ 
//				    BufferedImage bim = pdfRenderer.renderImageWithDPI(realPgNum, 10);
//
//				    // suffix in filename will be used as the file format
//				    ImageIOUtil.writeImage(bim, "aaaaa" + "-" + (realPgNum) + ".png", 10);
//				}
				
//				ImageGenerator imgSpacing = new ImageGenerator();
//				imgSpacing.generateSpacing(lines, pdfPgNumInStr, dir);
				
			}
		}
			
			return fontStylesResult;
		}
	
	
	
	
}

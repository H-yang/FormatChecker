package edu.cpplib.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.dept_list.DeptListTitlePage;
import edu.cpplib.export.ImageGenerator;
import edu.cpplib.export.ItemResult;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.line.Line;

public class TitlePage extends Page{
	
	private String correctTitle;
	private String sec1;
	private String sec2;
	private String sec3;
	private String sec4;
	private String author;
	private int paperYear;
	
	ArrayList<String> departments = null;


	public String getCorrectTitle() {
		return correctTitle;
	}

	public void setCorrectTitle(String correctTitle) {
		this.correctTitle = correctTitle;
	}



	public String getSec1() {
		return sec1;
	}

	public void setSec1(String sec1) {
		this.sec1 = sec1;
	}

	public String getSec2() {
		return sec2;
	}

	public void setSec2(String sec2) {
		this.sec2 = sec2;
	}

	public String getSec3() {
		return sec3;
	}

	public void setSec3(String sec3) {
		this.sec3 = sec3;
	}

	public String getSec4() {
		return sec4;
	}

	public void setSec4(String sec4) {
		this.sec4 = sec4;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPaperYear() {
		return paperYear;
	}

	public void setPaperYear(int year) {
		this.paperYear = year;
	}

	
	
	public void printSections(){
		System.out.println("sec1: " + sec1);
		System.out.println("sec2: " + sec2);
		System.out.println("sec3: " + sec3);
		System.out.println("sec4: " + sec4);
		System.out.println("Year: " + paperYear);
	}
	
	
	//in title page, strings that using bold means the strings are the title lines
	public String findTitle(){
		String title = "";
		
		for (Glyph g : this.glyphs){
			if(g.isBold()){
				title += g.getGl();
			}
		}
		
		return title;
	}
	
	public String getRawText(){
		String raw = "";
		for(Line line : lines){
			raw += Line.getCharsAndComma(line.getText());
		}
		
		return raw;
	}
	
	
	public String findS1(String raw, char paperType){
		String s1Pattern = "";

		if(paperType == 't'){
			s1Pattern = "(.*)(?:AThesisPresented)";
		}
		else if(paperType == 'p'){
			s1Pattern = "(.*?)(?:AProjectPresented)";
		}
		Pattern r = Pattern.compile(s1Pattern);
		Matcher m = r.matcher(raw);
		String s1 = "";

		if (m.find()){
			s1 = m.group(1);
		}
		else{
			System.out.println("ERROR: Please check paper type");
		}
		
		s1 = StrFunc.getCharsOnly(s1);
		
		return s1;
	}
	
	
	public String findS2(String raw, char paperType){
		String s2Pattern = "";
		String s2 = "";
		
		if(paperType == 't'){
			s2Pattern = "(?:AThesisPresented)(.*)(?:InPartial)";
			s2 = "AThesisPresented";
		}
		else if(paperType == 'p'){
			s2Pattern = "(?:AProjectPresented)(.*)(?:InPartial)";
			s2 = "AProjectPresented";
		}
		
		Pattern r = Pattern.compile(s2Pattern);
		Matcher m = r.matcher(raw);
		

		if (m.find()){
			s2 += m.group(1);
		}
		else{
			System.out.println("ERROR: Please check paper type");
		}
		
//		System.out.println(s2);

		return s2;
	}
	
	
	public String findS3(String raw){
		String s3Pattern = "(?:InPartial)(.*)(?:By)";
		String s3 = "InPartial";
		
		Pattern r = Pattern.compile(s3Pattern);
		Matcher m = r.matcher(raw);
		

		if (m.find()){
			s3 += m.group(1);
		}
		else{
			System.out.println("ERROR: Please check paper type");
		}
		
		if (StrFunc.hasUTF8(s3)){
			System.out.println("This line has UTF8");
		}
		return s3;
	}
	
	
	public String findS4(String raw){
		String s4Pattern = "(?:By)(.*)";
		String s4 = "By";
		
		Pattern r = Pattern.compile(s4Pattern);
		Matcher m = r.matcher(raw);
		

		if (m.find()){
			s4 += m.group(1);
		}
		else{
			System.out.println("ERROR: Please check paper type");
		}
		
		return s4;
	}
	
	
	public String findYear(String s4){
		String yPattern = "\\d{4}";
		String year = "";
		
		Pattern r = Pattern.compile(yPattern);
		Matcher m = r.matcher(s4);
		

		if (m.find()){
			year += m.group(0);
		}
		else{
			System.out.println("ERROR:");
		}
		
		return year;
	}
	
	
	public String findAuthor(){
		String pattern = "(By)(.*?)(\\d{4})";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(sec4);
		String author = "";
		if (m.find()){
			author += m.group(2);
		}
		else{
			System.out.println("ERROR:");
		}
		
		return author;
	}
	
	public void collectSections(char paperType) throws IOException{
		String title = findTitle();
		setCorrectTitle(title);
		
		String raw = getRawText();
		
		String s1 = findS1(raw, paperType);
		setSec1(s1);
		
		String s2 = findS2(raw, paperType);
		setSec2(s2);
		
		String s3 = findS3(raw);
		setSec3(s3);
		
		String s4 = findS4(raw);
		setSec4(s4);
		
		String yearStr = findYear(s4);
		int year = Integer.parseInt(yearStr);
		setPaperYear(year);
		
		String author = findAuthor();
		setAuthor(author);
		
		departments = DeptListTitlePage.readDeptListTXT();
		
//		for(String d : departments){
//			System.out.println(d);
//		}
		
//		print();

	}

	public boolean isTopMarginCorrect(){
		return (getTopMargin() >= 1.8);
	}
	
	public boolean isBottomMarginCorrect(){
		return (getBottomMargin() >= 0.9);
	}
	
	public boolean isLeftMarginCorrect(){
		return (getLeftMargin() >= 1.4);
	}
	
	public boolean isRightMarginCorrect(){
		return (getRightMargin() >= 0.9);
	}
	
	
	public boolean allUppercase(String str){
		for (int i=0; i<str.length(); i++){
			if(Character.isAlphabetic(str.charAt(i))){
				if(Character.getType(str.charAt(i)) == 2) continue;
				if (!Character.isUpperCase(str.charAt(i))){
					return false;
				}
			}
		}
		return true;
	}
	
	
	public boolean isSorted(float arr[]){
		boolean flag = true;
		for(int i=0; i<arr.length; ++i){
			if(arr[i] >= arr[i+1]) flag = false;
			break;
		}
		
		return flag;
	}
	
	public boolean isSec1Pass(){
		//correct title has been checked all bold
		boolean isTitleAllUppercase = allUppercase(correctTitle);
		
		String correctTitleCharsOnly = StrFunc.getCharsOnly(correctTitle);

		boolean isTitleEqualsSec1 = sec1.equals(correctTitleCharsOnly);

//		System.out.println("CORRECT: " + correctTitleCharsOnly);
//		System.out.println("SEC1:    " + sec1);
//		System.out.println("all upp" + isTitleAllUppercase);
//		System.out.println("equal" + isTitleEqualsSec1);
		return isTitleAllUppercase && isTitleEqualsSec1;
	}
	
	

	public boolean Sec2_isSpellingAndUppercaseFine(char paperType){
		
		/*paper type consistency has been checked during data collecting,
		 * if the type is not consistency, program cannot use pattern to split the section
		 * if there's something wrong in the findSec2, which implies the paper type is not consistent. 
		 * 
		 * avoid the situation like:
		 * Presented To The (wrong, should be "to the")
		 * Cali~ Pomona (wrong, should be "Cali~, Pomona")
		 * */
		
		String A__ = "";
		if(paperType == 'p'){
			A__ = "AProject";
		}
		else if(paperType == 't'){
			A__ = "AThesis";
		}
		
		String sec2Correct = A__ +  "PresentedtotheFacultyofCaliforniaStatePolytechnicUniversity,Pomona";
		
//		System.out.println(sec2.length());
//		System.out.println(sec2Correct.length());
//
//		System.out.println("   " + sec2Correct);
		
		return sec2.equals(sec2Correct);
	}
	
	
//	public boolean Sec2_isLineContainsCorrectChars(char paperType){
//		/*
//		 * avoid the following situation:
//		 * (correct)
//		 * A Thesis
//		 * Presented to the
//		 * Faculty of
//		 * Cali~, Pomona
//		 * 
//		 * (wrong)
//		 * A Thesis
//		 * Presented to the Faculty of
//		 * Cali~, Pomona
//		 * 
//		 * (wrong) (don't think there would be someone doing like this though)
//		 * Presented to the Faculty of
//		 * A Thesis
//		 * */
//		
//		boolean rec[] = new boolean[4];
//		
//		String line1 = "";
//		if(paperType == 'p'){
//			line1 = "AProject";
//		}
//		else if(paperType == 't'){
//			line1 = "AThesis";
//		}
//		
//		String line2 = "Presentedtothe";
//		String line3 = "Facultyof";
//		String line4 = "CaliforniaStatePolytechnicUniversity,Pomona";
//	
//		
//		float linesTopPos[] = new float[4];
//		
//		
//		for(Line line : lines){
//			String currentLine = Line.containsOnlyChars(line.getText());
//			float currentTopPos = line.getTopPos();
//			if(currentLine.equals(line1)){
//				rec[0] = true; 
//				linesTopPos[0] = currentTopPos;
//			}
//
//			if(currentLine.equals(line2)) {
//				rec[1] = true; 
//				linesTopPos[1] = currentTopPos;
//			}
//			
//			if(currentLine.equals(line3)) {
//				rec[2] = true; 
//				linesTopPos[2] = currentTopPos;
//			}
//			
//			if(currentLine.equals(line4)) {
//				rec[3] = true; 
//				linesTopPos[3] = currentTopPos;
//			}
//		}
//		
//		
//		boolean isCharFine = true;
//		for(int i=0; i<4; ++i){
//			if(rec[i] == false) isCharFine = false;
//			break;
//		}
//				
//		boolean isSeqFine = isSorted(linesTopPos);
//
//		return isCharFine && isSeqFine;
//	}
	
	
	
	
	public boolean Sec3_isSpellingUppercaseDeptFine(){
		/*
		 * 2 parts:
		 * (1) In Partial Fulfillment Of the ~
		 * (2) Master of [College] in [Dept]
		 * */
		
		/*(1)*/
		String s3Part1Correct = "InPartialFulfillmentOftheRequirementsfortheDegree";
		boolean isPart1Pass = sec3.contains(s3Part1Correct);
		boolean isPart2Pass = false;
		
		for(String d : departments){
//			System.out.println(d);
			if(sec3.contains(d)){
				isPart2Pass = true;
				break;
//				System.out.println("\n" + d);
			}
		}
		
//		System.out.println(isPart1Pass);
//		System.out.println(isPart2Pass);
		
		return isPart1Pass && isPart2Pass;
	}
	
	
//	public boolean Sec3_isLineContainsCorrectChars(){
//		/*
//		 * avoid the situation like:
//		 * Of the Requirements
//		 * for the Degree
//		 * 
//		 * Master of Science In
//		 * DEPT
//		 * */
//		
//		String line1 = "InPartialFulfillment";
//		String line2 = "OftheRequirementsfortheDegree";
//		String line3 = "Masterof"; //contains
//		String line4 = "In";
//		
//		return false;
//	}
	
	
	
	
	public boolean isSec4Pass(){
		/* By
		 * AUTHOR NAME
		 * 2016
		 * (nothing should be after year)
		 * */
		
		char[] chars = sec4.toCharArray();

		int last = chars.length-1;
		boolean flag = true;
		for(int i=last; i>last-4; i--){
//			System.out.println(chars[i]);
			if(!Character.isDigit(chars[i])){
				flag = false;
			}
			
		}
		
		String[] quarter = {"SPRING", "FALL", "WINTER", "SUMMER"};
		String s4 = sec4.toUpperCase();
		
		for(String q : quarter){
			if(s4.contains(q)) flag = false;
		}
		
		
		

//		System.out.println(flag);
		return flag;
		
	}
	
	
	
	
	public boolean isYearMatch(int currentYear){
		return currentYear == paperYear;
	}

	
	
	
	
	public boolean isLineCenter(Line line){
		
		
		float distanceFromLeft = line.getLeftPos() -35; //left margin: 1.5 inch, right margin: 1 inch
		float distanceFromRight = width - line.getRightPos();
	
//		System.out.print(line.getText() + " ");
//		System.out.println(distanceFromLeft + " " + distanceFromRight);
//		System.out.println(Math.abs(distanceFromLeft - distanceFromRight));
	
		
		if( Math.abs(distanceFromLeft - distanceFromRight) <= 10){
			return true;
		}
		else return false;
		
		
	}
	
	public boolean allLinesCenter(){
		
		boolean rec[] = new boolean[lines.size()];
		
		for(int i=0; i<lines.size(); ++i){
//			System.out.println(line.getText());
			if(!lines.get(i).isEnter() && lines.get(i).containsChars()){
				rec[i] = isLineCenter(lines.get(i));
			}
			else rec[i] = true; //skip the enter line, default is true
//			System.out.println(lines.get(i).containsChars());
		}
		
		boolean allCenter = true;
		int notCenterCounter = 0;
		
		for(int i=0; i<rec.length; ++i){
//			System.out.println(rec[i]);
			if(rec[i] == false) {
				notCenterCounter ++;
			}
			
		}
		
		
//		System.out.println("not center :" + notCenterCounter);
		if(notCenterCounter > 1) allCenter = false;
		
		return allCenter;
	}
	
	

	
	
	
	@Override
	public boolean checkTopMargin(){
		/*
		 * regular page: top/bottom/left/right: 1/1/1.5/1 
		 * tolerance : 0.1
		 * */
		return (1.9 <= topMargin);
	}
	
	
	
	
	
	public ArrayList<String> itemsNeedToBeConsistent(){
		/*
		 * name, title, project or thesis, year
		 * */
		ArrayList<String> ret = new ArrayList<String>();

		ret.add(sec1);
		
		if(sec2.contains("Project")){
			ret.add("Project");
		}
		if(sec2.contains("Thesis")){
			ret.add("Thesis");
		}
		
		ret.add(author);
		String year = Integer.toString(paperYear);
		ret.add(year);
		
		return ret;
	}
	
	
	
	public void check(char paperType, int currentYear){
		System.out.println("----------------------Title page check: ");
		System.out.print("* Margin: (t/b/l/r) ");
		System.out.print(isTopMarginCorrect() + " ");
		System.out.print(isBottomMarginCorrect() + " ");
		System.out.print(isLeftMarginCorrect() + " ");
		System.out.println(isRightMarginCorrect() + "\n");
		
		System.out.println("* Title format: (bold and all uppercase) ");
		System.out.println("   " + isSec1Pass() + "\n"); 
		// if false: not all uppercase, not bold, there're words in bold too(shouldn't be)
		
		System.out.println("* SEC2: A Project/Thesis Presented to the... (Check spelling and uppercase)");
		System.out.println("   " + sec2 + " " + Sec2_isSpellingAndUppercaseFine(paperType) + "\n");

		
		System.out.println("* SEC3: In Partial Fulfillment Of the... (Check spelling and uppercase)");
		System.out.println("   " + sec3);
		System.out.println("   " + sec3 + " " + Sec3_isSpellingUppercaseDeptFine() + "\n");
		/* if false: uppercase wrong (example: if the Requirements For the Degree)
		 * Master of BLAH in BLAH wrong: should be In but in or IN; dept not found or spelling wrong
		*/
		
		System.out.println("* SEC4: Check page number doesn't show on title page");
		System.out.println("   " + sec4 + " " + isSec4Pass() + "\n");
		
		System.out.println("* Check year is matched now");
		System.out.println("   " + paperYear + " " + currentYear + isYearMatch(currentYear) + "\n");
		
		System.out.println("* All lines are center " + allLinesCenter());
		
		System.out.println("Title page top margin: >2: " + checkTopMargin());
		System.out.println("Bottom margin: " + checkBottomMargin());
		System.out.println("Left margin: " + checkLeftMargin());
		System.out.println("Right margin: " + checkRightMargin());
		
		
		
		System.out.println("author: " + findAuthor());
		
		
		
	}
	
	
	public String check_PrintToTxt(char paperType, int currentYear){
		
		String ret = "";

		ret += "Margin: \r\n";
		ret += "Title page top margin >2: " + checkTopMargin() + "\r\n";
		ret += "Bottom margin: " + checkBottomMargin() + "\r\n";
		ret += "Left margin: " + checkLeftMargin() + "\r\n";
		ret += "Right margin: " + checkRightMargin() + "\r\n";
				
//		ret += "Top margin (2): " + isTopMarginCorrect() + "\r\n";
//		ret += "Bottom margin (1): " + isBottomMarginCorrect() + "\r\n";
//		ret += "Left margin (1.5): " + isLeftMarginCorrect() + "\r\n";
//		ret += "Right margin (1): " + isRightMarginCorrect() + "\r\n";

		ret += "Title format (bold and all caps): " + isSec1Pass() + "\r\n";
		ret += "If it is a false, which means there might be words are bold too, while it should not exist.\r\n";
		
		ret += "(Section 2) A Project/Thesis Presented to the... (Check spelling caps): ";
		ret += Sec2_isSpellingAndUppercaseFine(paperType) + "\r\n";
		
		ret += "(Section 3) In Partial Fulfillment Of the... (Check spelling and caps): ";
		ret += Sec3_isSpellingUppercaseDeptFine() + "\r\n";

		ret += "(Section 4) Check page number doesn't show on title page: " + isSec4Pass() + "\r\n";

		/* if false: uppercase wrong (example: if the Requirements For the Degree)
		 * Master of BLAH in BLAH wrong: should be In but in or IN; dept not found or spelling wrong
		*/

		ret += "Check year is matched >> " + "Year in paper [" + paperYear + "], Current year [" + currentYear + "]";
		ret += ": " + isYearMatch(currentYear) + "\r\n";
		
		ret += "All lines are center: " + allLinesCenter() + "\r\n";
		ret += "Author: " + findAuthor() + "\r\n";
		
		return ret;
		
	}
	
	
	public ArrayList<ItemResult> check_PrintToList(char paperType, int currentYear, String dir) throws IOException{
			
			ArrayList<ItemResult> pgrList = new ArrayList<ItemResult>();
			ItemResult singleItem = null;
			
			singleItem = new ItemResult("Page size", Float.toString(this.getWidth()) + " x " + Float.toString(this.getHeight()),
										this.checkPageWidth() && this.checkPageHeight(), "Letter size: 612.0 x 792.0", "");
			pgrList.add(singleItem);
			
			singleItem = new ItemResult("Top margin", Float.toString(this.getTopMargin()) + " (inches)", 
										checkTopMargin(), ">=2 inches", "");
			pgrList.add(singleItem);
			
			
			singleItem = new ItemResult("Bottom margin", Float.toString(this.getBottomMargin()) + " (inches)", 
										checkBottomMargin(), ">=1 inch", "");
			pgrList.add(singleItem);
			
			
			singleItem = new ItemResult("Left margin", Float.toString(this.getLeftMargin()) + " (inches)", 
										checkLeftMargin(), ">=1.5 inches", "");
			pgrList.add(singleItem);
			

			singleItem = new ItemResult("Right margin", Float.toString(this.getRightMargin()) + " (inches)", 
					      				checkRightMargin(), ">=1 inches", "");
			pgrList.add(singleItem);
			

			singleItem = new ItemResult("Title format", this.getCorrectTitle(), isSec1Pass(), "check: all bold and caps", "");
			pgrList.add(singleItem);
			
						
			singleItem = new ItemResult("Section 2", this.getSec2(), 
										Sec2_isSpellingAndUppercaseFine(paperType), "check: spelling and caps", "");
			pgrList.add(singleItem);
			
			
			singleItem = new ItemResult("Section 3", this.getSec3(), 
										Sec3_isSpellingUppercaseDeptFine(), "check: spelling and caps; department name is matched", "");
			pgrList.add(singleItem);
			
			
//			System.out.println("* SEC3: In Partial Fulfillment Of the... (Check spelling and uppercase)");
//			System.out.println("   " + sec3);
//			System.out.println("   " + sec3 + " " + Sec3_isSpellingUppercaseDeptFine() + "\n");
			/*
			 * section3 includes master XXXX in OOOO
			 * */
			
			
	
			singleItem = new ItemResult("Section 4", this.getSec4(),
										isSec4Pass(), "check: existence of page number, quarter", "");
			pgrList.add(singleItem);
			
			
			/* if false: uppercase wrong (example: if the Requirements For the Degree)
			 * Master of BLAH in BLAH wrong: should be In but in or IN; dept not found or spelling wrong
			*/
	
//			singleItem = new ItemResult("Spacing", "" + this.getSpacing(), true, "value between 26-28 is double", "");
//			pgrList.add(singleItem);
			
			singleItem = new ItemResult("Year", Integer.toString(this.getPaperYear()), isYearMatch(currentYear), 
										"check: value is matched " + currentYear, "");
			pgrList.add(singleItem);
					
//			singleItem = new ItemResult("Lines are center", "-", allLinesCenter(), "-", "");
//			pgrList.add(singleItem);
			
			
			boolean checkFontFamily = checkFontFamily();
			singleItem = new ItemResult("Font family", this.getFontFamily(), checkFontFamily, "Times New Roman, Arial, Calibri", "-");
			pgrList.add(singleItem);
			
			
			if(checkFontFamily){
				/*
				 * make sure user did not use different font family
				 * */
				ArrayList<Glyph> differentFontStyle = glyphsUsingDifferentFontFamily();
				int numOfDifferentFontStyle = differentFontStyle.size();
//				System.out.println("llllll" + numOfDifferentFontStyle);
				if(numOfDifferentFontStyle == 0){
					singleItem = new ItemResult("Font style consistency", "-", true, "Student does not use different font style", "-");
					pgrList.add(singleItem);
				}
				else{
					singleItem = new ItemResult("Font style consistency", "-", false, "Student MIGHT use different font style", "-");
					pgrList.add(singleItem);
					for(Glyph g : differentFontStyle){
						if(g.getFontFamily()!= null) {
							if(g.getFontFamily().contains("Math") || g.getFontFamily().contains("MATH")) continue;
							if(g.getFontFamily().contains("Symbol") || g.getFontFamily().contains("SYMBOL")) continue;
							singleItem = new ItemResult("Different style used", 
														g.getGl() + "--" + g.getFontFamily() + "--" + g.getFontSize(), false, "-", "-");
							pgrList.add(singleItem);
						}
					}
	
					
					/*print wrong font family used in page to a png*/
					ImageGenerator img = new ImageGenerator();
					int realPDFPgNum = 1;
					String pdfPgNumInStr = Integer.toString(realPDFPgNum);
					img.run(differentFontStyle, pdfPgNumInStr, dir);
				}
				
			}
			
			
			
			
			
			return pgrList; 
			
		}
	
}

package edu.cpplib.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;

import edu.cpplib.block.Block;
import edu.cpplib.dept_list.DeptListSignaturePage;
import edu.cpplib.export.ImageGenerator;
import edu.cpplib.export.ItemResult;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.line.Line;


public class SignaturePage extends Page{
	
	private String firstLine;
	private String lastLine;
	private String rawText;
	private String paperTitle;
	private String dept;
	
	private ArrayList<Block> blocks;

	ArrayList<String> departments = null;
	
	public String getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}

	public String getLastLine() {
		return lastLine;
	}
	
	public void setLastLine(String lastLine) {
		this.lastLine = lastLine;
	}
	
	public String getRawText() {
		return rawText;
	}

	public void setRawText(String rawText) {
		this.rawText = rawText;
	}

	public String getPaperTitle() {
		return paperTitle;
	}

	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}


	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	
	
	public String findFirstLine(){
		String firstLine = "";
		for(Line line : lines){
			if(line.containsChars()){
				firstLine = line.getText();
				break;
			}
		}
		
		firstLine = Line.getCharsOnly(firstLine);
		return firstLine;
	}
	
	public String findLastLine(){
		String lastLine = "";
		for(int i=lines.size()-1; i>0; i--){
			if(lines.get(i).containsChars()){
				lastLine = lines.get(i).getText();
				break;
			}
		}
		
		lastLine = Line.getCharsOnly(lastLine);
		return lastLine;
	}
	
	
	public boolean isSignaturePage(){

		if(firstLine.equals("SIGNATUREPAGE")) return true;
		else return false;
	}
	

	public boolean isPageNumberCorrect(){

//		System.out.println(lastLine.length());
//		System.out.println("ii".length());

		if(lastLine.equals("ii")) return true;
		else return false;
	}
	
	
	public String findBold(){
		String bold = "";
		
		for (Glyph g : this.glyphs){
			if(g.isBold()){
				bold += g.getGl();
			}
		}
		
		return bold;
	}
	
	
	public String findRawText(){
		String raw = "";
		for(Glyph g : glyphs){
//			System.out.print(g.getGl());
//			System.out.println(g.getLeftPos());
			raw += g.getGl();
		}
		
		raw = Line.getCharsOnly(raw);
		return raw;
	}
	
	
	public String findDept(){
		int dsIndex = -1;
		for(int i=0; i<this.blocks.size(); ++i){
			if(blocks.get(i).getText().contains("DATESUBMITTED")) {
				dsIndex = i;
				break;
			}
		}
		
		if(dsIndex == -1){
			return "ERROR";
		}
		else{
			String dept = blocks.get(dsIndex+1).getText() + blocks.get(dsIndex+2).getText();
			return dept;
		}
	}
	
	
	
	public boolean containsChars(String source){
		char[] chars = source.toCharArray();
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
	

	public String findPaperTitle(char paperType){
		/*
		 * paper title is the string between THESIS/PROJECT and AUTHOR  
		 * */
//		System.out.println(rawText);
		
		String pattern = "";

		if(paperType == 't'){
			pattern = "(?:THESIS)(.*?)(?:AUTHOR)";
		}
		else if(paperType == 'p'){
			pattern = "(?:PROJECT)(.*?)(?:AUTHOR)";
		}

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(rawText);
		String title = "";

		if (m.find()){
			title = m.group(1);
		}
		else{
			System.out.println("ERROR: Please check paper type");
		}
		

		
		return title;
	}
	
	
	public boolean checkQuarter(){
		
		int dsIndex = -1;
		boolean quarterExists = false;
		
		String[] quarters = new String[]{"Spring", "Summer", "Fall", "Winter"};
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains("DATESUBMITTED")){
				dsIndex = i;
				break;
			}
		}
		
		if(dsIndex == -1){
			quarterExists = false; //or unknown error
		}
		else{
			String dsStr = blocks.get(dsIndex).getText() + blocks.get(dsIndex+1).getText();
//			System.out.println("check quarter in " + dsStr);
			for(String q : quarters){
				if(dsStr.contains(q)){
					quarterExists = true;
					break;
				}
			}			
		}
		
		return quarterExists;
		
	}
	
	
	
	public ArrayList<Block> buildBlocks(){

		String all = "";
		
		for(int i=0; i<glyphs.size(); ++i){
			String tmp = "";
			if(!glyphs.get(i).isEnter() && (glyphs.get(i).getGl() != ' ')){
				tmp += glyphs.get(i).getGl();
				tmp += "$";
				tmp += glyphs.get(i).getTopPos();
				tmp += ";";
				tmp += glyphs.get(i).getLeftPos();
				tmp += "|";
			}
			
			try{
				if(Math.abs(glyphs.get(i).getLeftPos() - glyphs.get(i+1).getLeftPos()) > 16){
					tmp += "#";
				}
			}
			catch (Exception ex){
			}

			all += tmp;
		}

		
		String[] strSplit = all.split("#");
		
		
		ArrayList<Block> retBlocks = new ArrayList<Block>();
		
		for(String ss : strSplit){
			if(containsChars(ss)){
				Block block = new Block();
				block.setRaw(ss);
				retBlocks.add(block);
			}
		}


		for(Block b : retBlocks){
			String tib = b.findText();
			b.setText(tib);
			
			float top = b.findTopPos();
			b.setTopPos(top);
			
			float left = b.findLeftPos();
			b.setLeftPos(left);
			
//			b.print();
		}

		for(Block b : retBlocks){
			String str = Line.getCharsOnly(b.getText());
			b.setText(str);
		}
		
		return retBlocks;
		
	}
	
	
	public boolean is2ColumnsAligned(){
		/*
		 * check:
		 * THESIS:         BLAHBLAHBLAH
		 *                 BLAHBLAH
		 * AUTHOR:         NAME
		 * DATE SUBMITTED: QUARTER YYYY
		 * ...
		 * 
		 * false example:
		 * THESIS:
		 * BLAHBLAHBLAHBLAH
		 * 
		 * AUTHOR:
		 * NAME
		 * 
		 * DATE SUBMITTED:
		 * QUARTER YYYY
		 * 
		 * */
		
		/* get left positions of each block
		 * right standard should have 2 different positions
		 * do not count first line(SIGNATURE PAGE) and last line(page number ii) 
		 * */
		
		ArrayList<Integer> leftPos = new ArrayList<Integer>();
		for(Block b : blocks){
//			System.out.println(b.getText() + b.getLeftPos());
			if(b.getText().equals("SIGNATUREPAGE") || 
			   b.getText().equals("ii") || 
			   b.getText().contains("__") ||
			   b.getText().length() <5 ) continue;
			
			leftPos.add(Math.round(b.getLeftPos()));

		}
		
		
		Set<Integer> setOfLeftPos = new HashSet<Integer>(leftPos);
//		for(int i : setOfLeftPos){
//			System.out.println(i);
//		}
		
		if(setOfLeftPos.size() == 1){
			return false;
		}
		
		else{
			ArrayList<Integer> col1 = new ArrayList<Integer>();
			ArrayList<Integer> col2 = new ArrayList<Integer>();
			
			for(int i: setOfLeftPos){
				if(108 <= i && i <= 200){
					col1.add(i);
				}
				else if (i < 108){
				}
				else{
					col2.add(i);
				}
			}
			
			Collections.sort(col1);
			Collections.sort(col2);
			
			
			boolean isCol1Aligned = true;
			boolean isCol2Aligned = true;
			
//			System.out.println("col1");
			if(col1.size() == 1){
				isCol1Aligned = true;
			}
			else{
				for(int i = 0; i< col1.size(); ++i){
					try{
						if(Math.abs(col1.get(i) - col1.get(i+1)) > 4) isCol1Aligned = false;
						break;
					}
					catch (Exception ex){}
				}
			}

			
//			System.out.println("col2");
			if(col2.size() == 1){
				isCol2Aligned = true;
			}
			else{
				for(int i = 0; i< col2.size(); ++i){
					try{
						if(Math.abs(col2.get(i) - col2.get(i+1)) > 4) isCol2Aligned = false;
						break;
					}
					catch (Exception ex){}
				}
			}

//			System.out.println(isCol1Aligned + ", " + isCol2Aligned);			
			return isCol1Aligned && isCol2Aligned;
		}
		
	}
	
	

	public boolean isBoldLinesMatch(char paperType){
		/*
		 * Bold lines in signature page should contains:
		 * 1. SIGNATURE PAGE (sp)
		 * 2. PROJECT/THESIS (pt) (if false, might be project/thesis consistency problem)
		 * 3. AUTHOR (author)
		 * 4. DATE SUBMITTED (dateSubmitted)
		 * Other lines contain bold fonts are not allowed
		 * also do the spelling check here, avoid the situation like:
		 * Data Submitted
		 * */
		
		int standardBoldCharsNum = 39;
		String bold = findBold();
		bold = bold.replace(" ", "");
		bold = bold.replace(":", "");
		
//		System.out.println(bold);
		
		
		if(bold.length() > standardBoldCharsNum){
			//there are lines contain bold chars, for example: 066.pdf
			return false;
		}
		
		else{
			String sp = "SIGNATUREPAGE";
			
			String pt = "THESIS";
			if(paperType == 'p'){
				pt = "PROJECT";
			}
			
			String author = "AUTHOR";
			String dateSubmitted = "DATESUBMITTED";
			
			boolean rec[] = new boolean[4];
			
			if(bold.contains(sp)) rec[0] = true;
			if(bold.contains(pt)) rec[1] = true;
			if(bold.contains(author)) rec[2] = true;
			if(bold.contains(dateSubmitted)) rec[3] = true;
			
			for(int i=0; i<4; ++i){
				if(rec[i] == false) return false;
			}
			
			return true;
		}

		
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
	

	public boolean isCommitteeChairAtFirstPlace(char paperType){
		/*
		 * determine by the distance (how many blocks between) between "date submitted" and "XXXX committee chair"
		 * text: DATESUBMITTED:
		 * text: Winter2016
		 * text: XXXXXXXXXXXXXXXDepartment
		 * text: Dr.XXXXXXXXX
		 * text: Project/Thesis CommitteeChair
		 * 
		 * if false, means the content doesn't have Committee Chair 
		 * or 
		 * the spelling/bold/upper case is wrong in "DATE SUBMITTED"
		 * */
		
		int standardBlocksBetween = 6;
		int indexOfDateSubmitted = -1;
		int indexOfCommitteeChair = -1;
		
		String CommitteeChair = "ThesisCommitteeChair";
		if(paperType == 'p'){
			CommitteeChair = "ProjectCommitteeChair";
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains("DATESUBMITTED")) indexOfDateSubmitted = i;
			if(blocks.get(i).getText().contains(CommitteeChair)) indexOfCommitteeChair = i;
		}
		
		
//		System.out.println(indexOfDateSubmitted + ", " + indexOfCommitteeChair);
		
		if(indexOfDateSubmitted == -1 || indexOfCommitteeChair == -1){
			return false;
		}
		
		else{
			if(indexOfCommitteeChair - indexOfDateSubmitted <= standardBlocksBetween){
				return true;
			}
			
			else return false;
		}

		
	}
	
	
	public boolean checkDept(){
		
		boolean flag = false;
		for(String d : this.departments){
			
//			System.out.println(d);
			
			if(this.dept.contains(d)){
				flag = true;
//				System.out.println(this.dept + " " + d);
				break;
			}
		}
		
		return flag;
	}
	

	public void collect(char paperType) throws IOException{
		String fLine = findFirstLine();
		setFirstLine(fLine);
		
		String lLine = findLastLine();
		setLastLine(lLine);
		
		String raw = findRawText();
		setRawText(raw);
		
		ArrayList<Block> bs = buildBlocks();
		setBlocks(bs);
		
		String paperTitle = findPaperTitle(paperType);
		setPaperTitle(paperTitle);
		
		String dept = findDept();
		setDept(dept);
		
		departments = DeptListSignaturePage.readDeptListTXT();
		
//		System.out.println(raw);
	}
	
	@Override
	public boolean checkTopMargin(){
		/*
		 * signature page: top/bottom/left/right: 2/1/1.5/1 
		 * tolerance : 0.1
		 * */
		return (1.9 <= topMargin);
	}
	
	
	
	public void checkItemsConsistent_cmd(ArrayList<String> itemsFromTitlePage){
		/*
		 * 0 title
		 * 1 tp
		 * 2 author
		 * 3 year
		 * */
		
		int correctMatchedNumber = 4;
		int matchedCounter = 0;
		
		String title = itemsFromTitlePage.get(0);
		String tp = itemsFromTitlePage.get(1).toUpperCase();
		String author = itemsFromTitlePage.get(2);
		String year = itemsFromTitlePage.get(3);
		
		
		for(int i=0; i<blocks.size(); ++i){
			if(title.contains(blocks.get(i).getText())) {
				matchedCounter ++;
//				System.out.println(title + blocks.get(i).getText());
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains(tp)) {
				matchedCounter ++;
//				System.out.println(blocks.get(i).getText() + (tp));
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains(author)) {
				matchedCounter ++;
//				System.out.println(blocks.get(i).getText() + (author));
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains(year)) {
				matchedCounter ++;
//				System.out.println(blocks.get(i).getText() + (year));
				break;
			}

		}
		
		
		
		
//		int matchedCounter = 0;
//		
//		for(int i=0; i<itemsFromTitlePage.size(); ++i){
//			for(int j=0; j<blocks.size(); ++j){
//				if(blocks.get(j).getText().contains(itemsFromTitlePage.get(i))) {
//					System.out.println(itemsFromTitlePage.get(i));
//					System.out.println(blocks.get(j).getText());
//					matchedCounter ++;
//				}
//			}
//		}
//		
//		System.out.println(matchedCounter);
		
		System.out.print("Items are consistent between title page and signature page: ");
		if(matchedCounter == correctMatchedNumber){
			System.out.println("true");
		}
		else System.out.println("false");
	}
	
	
	
	public void check(char paperType){

		/*
		 * should return an record array, which stores like:
		 * enum?  
		 *  arr[boldLines] = true
		 *  arr[pgnumii] = true
		 * ...
		 * */
//		for(Block b : blocks){
//			b.print();
//		}
		
//		for(int i=0; i<blocks.size(); ++i){
//			System.out.println(i);
//			blocks.get(i).print();
//		}

		
		System.out.println("----------------------Signature page check:");

		boolean isSignaturePage = isSignaturePage();
		System.out.println("Is this page signature page? " + isSignaturePage);
		
		if(isSignaturePage){
			
			System.out.println("Title page top margin: >2: " + checkTopMargin());
			System.out.println("Bottom margin: " + checkBottomMargin());
			System.out.println("Left margin: " + checkLeftMargin());
			System.out.println("Right margin: " + checkRightMargin());
			
			
			System.out.println("Is bold lines correct? " + isBoldLinesMatch(paperType));
			//if false, tell user to check if page number or other lines using bold
			
			System.out.println("Page number is ii? " + isPageNumberCorrect());
			
			boolean isPageContains2Columns = is2ColumnsAligned();
			System.out.println("2 columns? " + isPageContains2Columns);
			
			if(!isPageContains2Columns){
				System.out.println("((might contains white chars))");
			}

			System.out.println("Paper title should all uppercase " + allUppercase(paperTitle));
			System.out.println("Committee Chair at First Place " + isCommitteeChairAtFirstPlace(paperType));
			System.out.println("Student uses quarter (not month) " + checkQuarter());
		}
	}
	
	
	
	
	public String check_PrintToTxt(char paperType){

		String ret = "";

		boolean isSignaturePage = isSignaturePage();
		ret += "Is this page signature page? " + isSignaturePage + "\r\n";
		
		
		if(isSignaturePage){
			
			
			ret += "Margin:\r\n";
			ret += "Title page top margin >2: " + checkTopMargin() + "\r\n";
			ret += "Bottom margin: " + checkBottomMargin() + "\r\n";
			ret += "Left margin: " + checkLeftMargin() + "\r\n";
			ret += "Right margin: " + checkRightMargin() + "\r\n";

			ret += "Are bold lines correct: " + isBoldLinesMatch(paperType) + "\r\n";
			ret += "(if it is a false, which means some lines are using bold too.)\r\n";
			
			ret += "Page number is ii: " + isPageNumberCorrect() + "\r\n";

			
			boolean isPageContains2Columns = is2ColumnsAligned();
			ret += "2 columns: " + isPageContains2Columns + "\r\n";
			
			
			if(!isPageContains2Columns){
				ret += "((!!might contains white chars!!))\r\n";
			}
			
			ret += "Paper title should all uppercase: " + allUppercase(paperTitle) + "\r\n";
			ret += "Committee Chair at First Place: " + isCommitteeChairAtFirstPlace(paperType) + "\r\n";
			ret += "Student uses quarter (not month): " + checkQuarter() + "\r\n";
		}

		return ret;
	}
	
	
	public ArrayList<ItemResult> check_PrintToList(char paperType, ArrayList<String> itemsFromTitlePage, String dir) throws IOException{

		
//		for(Block b : this.blocks){
//			b.print();
//		}
		
		
		
		ArrayList<ItemResult> pgrList = new ArrayList<ItemResult>();
		ItemResult singleItem = null;
		
		singleItem = new ItemResult("Page size", Float.toString(this.getWidth()) + " x " + Float.toString(this.getHeight()),
									this.checkPageWidth() && this.checkPageHeight(), "Letter size: 612.0 x 792.0", "");
		pgrList.add(singleItem);
		
		boolean isSignaturePage = isSignaturePage();
		singleItem = new ItemResult("Is signature page", "-", isSignaturePage, "if this page is signature page", "-");

		
		if(isSignaturePage){
			
			singleItem = new ItemResult("Page number is ii", "-", isPageNumberCorrect(), "-", "-");
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


			singleItem = new ItemResult("Right margin", Float.toString(this.getRightMargin()) + " inches", 
			      						checkRightMargin(), ">=1 inches", "");
			pgrList.add(singleItem);

			
			singleItem = new ItemResult("Bold lines", "-", isBoldLinesMatch(paperType), 
										"if it is a false, which means some lines are using bold.", "");
			pgrList.add(singleItem);


			boolean isPageContains2Columns = is2ColumnsAligned();
			singleItem = new ItemResult("Alignment", "-", isPageContains2Columns, "if false, might contains white chars", "-");
			pgrList.add(singleItem);
			

			singleItem = new ItemResult("Paper title", this.getPaperTitle(), allUppercase(paperTitle), "all caps", "-");
			pgrList.add(singleItem);
			
			singleItem = new ItemResult("Committee chair", "-", isCommitteeChairAtFirstPlace(paperType), 
										"committee chair at first Place", "-");
			pgrList.add(singleItem);
			
			singleItem = new ItemResult("Quarter (not month)", "-", checkQuarter(), "-", "-");
			pgrList.add(singleItem);
			
			singleItem = new ItemResult("Items consistency", "-", checkItemsConsistent(itemsFromTitlePage), 
										"check Title, Thesis/Project, Author, Year are consistent as shown in title page", "-");
			pgrList.add(singleItem);

			singleItem = new ItemResult("Department", this.getDept(), checkDept(), "-", "-");
			pgrList.add(singleItem);
			

			
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
					int realPDFPgNum = 2;
					String pdfPgNumInStr = Integer.toString(realPDFPgNum);
					img.run(differentFontStyle, pdfPgNumInStr, dir);
				}
				
			}
			
			
			
			
		}
		else{
			singleItem = new ItemResult("ERROR!!", "Signature Page is not existed!", false, "-", "-");
			pgrList.add(singleItem);
		}

		return pgrList;
	}
	
	
	
	
	
	
	
	public boolean checkItemsConsistent(ArrayList<String> itemsFromTitlePage){
		
		int correctMatchedNumber = 4;
		int matchedCounter = 0;
		
		String title = itemsFromTitlePage.get(0);
		String tp = itemsFromTitlePage.get(1).toUpperCase();
		String author = itemsFromTitlePage.get(2);
		String year = itemsFromTitlePage.get(3);
		
		
		for(int i=0; i<blocks.size(); ++i){
			if(title.contains(blocks.get(i).getText())) {
//				System.out.println(blocks.get(i).getText());
//				System.out.println(title);
				matchedCounter ++;
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains(tp)) {
//				System.out.println(blocks.get(i).getText());
//				System.out.println(tp);
				matchedCounter ++;
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
//			System.out.println(blocks.get(i).getText());
//			System.out.println(author);
			if(blocks.get(i).getText().contains(author)) {
//				System.out.println(blocks.get(i).getText());
//				System.out.println(author);
				matchedCounter ++;
				break;
			}
		}
		
		for(int i=0; i<blocks.size(); ++i){
			if(blocks.get(i).getText().contains(year)) {
//				System.out.println(blocks.get(i).getText());
//				System.out.println(year);
				matchedCounter ++;
				break;
			}

		}

		if(matchedCounter == correctMatchedNumber){
			return true;
		}
		else {
			return false;
		}

	}
	
	
	
	
	
	
	
	
}

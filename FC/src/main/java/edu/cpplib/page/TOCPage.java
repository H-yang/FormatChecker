package edu.cpplib.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.line.Line;
import edu.cpplib.table.TItem;

/* Table of content
 * List of table
 * List of figure
 * */

public class TOCPage{
	
	private String TOCType; //LoF, LoT, ToC
	private ArrayList<TItem> tableList;
	private boolean isExistInRealPg;
	private ArrayList<Glyph> glyphs;
	private ArrayList<Line> lines;
	
//	private HashMap<String, Integer> itemsOfTable;
//
//	public HashMap<String, Integer> getItemsOfTable() {
//		return itemsOfTable;
//	}
//
//	public void setItemsOfTable(String item, int target) {
//		this.itemsOfTable.put(item, target);
//	}
	
	public ArrayList<TItem> getItems() {
		return tableList;
	}
	public void setTItems(ArrayList<TItem> items) {
		this.tableList = items;
	}
	
	public String getTOCType() {
		return TOCType;
	}
	public void setTOCType(String tOCType) {
		TOCType = tOCType;
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
	public boolean isExistInRealPg() {
		return isExistInRealPg;
	}
	public void setExistInRealPg(boolean isExistInRealPg) {
		this.isExistInRealPg = isExistInRealPg;
	}
	
	public class LineComparator implements Comparator<Line> {
	    public int compare(Line l1, Line l2) {
	        return new Float(l1.getTopPos()).compareTo(l2.getTopPos());
	    }
	}
	
	public void buildGlyphs1(PDDocument doc, int num, boolean isLast) throws IOException{
		
		ArrayList<Glyph> glyphs = null;
		glyphs = Glyph.build(doc, num, isLast);
		for(Glyph g : glyphs){
			g.specialFontFamilyTrim();
		}
		this.setGlyphs(glyphs);
	}
	
	
	/*Same way to build lines for a page, but do not eliminate dot*/
	public ArrayList<Line> buildLines(){
		
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
//			tmp.elimDot();
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
		return lines;
	}
	
	
	public static TOCPage getDataRetPg(PDDocument doc, int num, boolean isLast) throws IOException{
		
//		buildGlyphs(filePath, page, num);
		TOCPage retPage = new TOCPage();
		retPage.buildGlyphs1(doc, num, isLast);
		retPage.lines = retPage.buildLines();
//		System.out.println("Print glyphs");
//		for(Glyph g : retPage.glyphs){
//			System.out.println("Glyph: " + g.getGl() + "Left pos: " + g.getLeftPos() + ", Top pos: " + g.getTopPos());
//		}
//		for(Line l : retPage.lines){
//			l.print();
//		}
		return retPage;
//		definePageType();
	}
	
	
	
	public String leftSection(){
//		ArrayList<Line> leftSecs = new ArrayList<Line>();
		String leftSec = "";
		for(Glyph g : glyphs){
			if(g.getLeftPos() <= 200){
				leftSec += g.getGl();
			}
		}
		
		return leftSec;
	}
	
	
	public String findTOCType(String leftSec){
		String type = "TOC";
		leftSec = leftSec.toUpperCase(); //some may use caps, some may not
		int occuranceTable = 0, occuranceFigure = 0;
		occuranceTable = StringUtils.countMatches(leftSec, "TABLE");
		occuranceFigure = StringUtils.countMatches(leftSec, "FIGURE");

		if(occuranceTable == 0) type = "LoF";
		else if(occuranceFigure == 0) type = "LoT";
		else type = "ToC";
		
		return type;
	}
	
	public void defineTOCType(){
		String leftSec = leftSection();
//		System.out.println(leftSec);
		String type = findTOCType(leftSec);
		this.setTOCType(type);
		
		/*left section will contain:
		 * Table 1
		 * Table 2
		 * ...
		 * Figure 1
		 * Figure 2
		 * 
		 * so we search Table/Figure in all the chars in left section
		 * if it can get lots of "Table" then the TOC type is table
		*/
	}
	
	
	




	public String lastNChars(String str, int n){
		String lastN = "";
		if (str != null && str.length() >= n) {  
		    lastN = str.substring(str.length() - n);
		}
		return lastN;
	}
	
	
	public String firstNChars(String str, int s){
		String firstN = "";
		if(str != null && str.length() >= s){
			int n = str.length() -s;
			firstN = str.substring(0, Math.min(str.length(), n));
		}

		return firstN;
	}
	
	
	public String combineAllChars(){
		String all = "";
		for(Glyph g : glyphs){
			if(g.isSpace()) continue;
			if(g.isEnter()) continue;
			all += g.getGl();
			
		}
		return all;
	}
	

	public ArrayList<TItem> getTableInformation(int startPage){
//		System.out.println("this is a " + this.getTOCType());
		ArrayList<TItem> itemsList = new ArrayList<TItem>();
		
		for(Line line : lines){
			String lineText = StrFunc.elimSpaces(line.getText()).trim();
//			System.out.println(lineText);
			
			lineText = firstNChars(lineText, 2); //eliminate last 2 chars which are space and dot
			
			String tableName = firstNChars(lineText, 4);
			tableName = StrFunc.elimDot(tableName);
			tableName = StrFunc.elimSpaces(tableName);
			
			String targetPgNum = lastNChars(lineText, 4);
			targetPgNum = StrFunc.elimDot(targetPgNum);
			targetPgNum = StrFunc.elimSpaces(targetPgNum);
			
			if(StrFunc.containsChars(tableName)){
				TItem i = new TItem(tableName, targetPgNum, -1);
				itemsList.add(i);
			}

			
		}
		
		this.setTItems(itemsList);
		
		/* first line and last line should be ignored
		 * first line is the heading, last line is page num
		 * */
		if(tableList.size() > 2){
			tableList.remove(0);
			tableList.remove(tableList.size()-1);
		}

		
		for(TItem i : tableList){
			i.calculateRealPgNum(startPage);
		}
	
		
		return this.tableList;
		
	}
	
	
	public void printTable(){
		for(TItem i : tableList){
			i.print();
		}
	}
	
	

	public TItem parseItemStr(String itemStr){
		
		char[] chars = itemStr.toCharArray();
		
		int secondSharpIndex = -1; //count from the end
		int sharpCounter = 0;
		
		
		for(int i=chars.length-1; i>=0; i--){
//			System.out.println(chars[i]);
			if(chars[i] == '#') sharpCounter += 1;
			if(sharpCounter == 2){
				secondSharpIndex = i;
				break;
			}
		}
//		System.out.println(secondSharpIndex);

		String targetPgNum = "";
		for(int i=secondSharpIndex; i<chars.length; ++i){
//			System.out.print(chars[i]);
			targetPgNum += chars[i];
		}
		targetPgNum = StrFunc.getCharsOnly(targetPgNum);
		
		
		String itemName = "";
//		System.out.println("item name different way:");
		for(int i=0; i<secondSharpIndex; ++i){
			itemName += chars[i];
//			System.out.print(chars[i]);
		}
		itemName = itemName.replace('#', '.'); //restore dot

//		System.out.println("Item Name: " + itemName + ", on " + targetPgNum);
		
		TItem item = new TItem(itemName, targetPgNum, -1);
		return item;
		
		
//		System.out.println(pageNum);
		
	}
	
	
	
	public ArrayList<TItem> getTableInformation1(int startPage){
//		System.out.println("this is a " + this.getTOCType());
		ArrayList<TItem> itemsList = new ArrayList<TItem>();
		TItem currentItem = null;
		
		for(Line line : lines){
			//parse item line by line
			String itemStr = line.parseTOCToStrArr();
			if(itemStr.length() >= 1){
//				System.out.println("ITEM STR: " + itemStr);
				currentItem = parseItemStr(itemStr);
				currentItem.calculateRealPgNum(startPage);
				itemsList.add(currentItem);
			}
			
//			if(line.isValidTableItem())
//				get title from line
//				get page number from line
//				add to itemsList
//			String lineText = StrFunc.elimSpaces(line.getText()).trim();
//			System.out.println(lineText);


			
		}
		this.setTItems(itemsList);
		return this.tableList;
		
	}
	
	
	
	
	

	
}












/*
 * HashMap<String, Integer> headingsHashMap = new HashMap<String, Integer>();
		for(FirstLine h : headings){
			headingsHashMap.put(h.getPageType(), h.getPageNum());
		}
		
		System.out.println(headingsHashMap.get("SIG")); and you get sig's num
 * 
 * 
 * 
 * 
 * */
 
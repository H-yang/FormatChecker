package edu.cpplib.fc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.export.Export2PDF;
import edu.cpplib.export.HeadingResult;
import edu.cpplib.export.HtmlHelper;
import edu.cpplib.export.ItemResult;
import edu.cpplib.font_style.FontStyle;
import edu.cpplib.headings.*;
import edu.cpplib.page.*;
import edu.cpplib.table.GroupFunc;
import edu.cpplib.table.TItem;


public class FCDocument{
	private String fPath, TP, inputYear, inputStart, fName;
	private int yearInt, pgNum3, startInt, totlaPagesCount;
	private char paperType;
	private boolean isTex;
	
	public String getfPath() {
		return fPath;
	}

	public void setfPath(String fPath) {
		this.fPath = fPath;
	}

	public String getTP() {
		return TP;
	}

	public void setTP(String tP) {
		this.TP = tP;
	}
	public String getInputYear() {
		return inputYear;
	}

	public void setInputYear(String inputYear) {
		this.inputYear = inputYear;
	}

	public String getInputStart() {
		return inputStart;
	}

	public void setInputStart(String inputStart) {
		this.inputStart = inputStart;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public int getYearInt() {
		return yearInt;
	}

	public void setYearInt(int yearInt) {
		this.yearInt = yearInt;
	}

	public int getPgNum3() {
		return pgNum3;
	}

	public void setPgNum3(int pgNum3) {
		this.pgNum3 = pgNum3;
	}

	public char getPaperType() {
		return paperType;
	}

	public void setPaperType(char paperType) {
		this.paperType = paperType;
	}

	public int getStartInt() {
		return startInt;
	}

	public void setStartInt(int startInt) {
		this.startInt = startInt;
	}

	public int getTotlaPagesCount() {
		return totlaPagesCount;
	}

	public void setTotlaPagesCount(int totlaPagesCount) {
		this.totlaPagesCount = totlaPagesCount;
	}

	public boolean isTex() {
		return isTex;
	}

	public void setTex(boolean isTex) {
		this.isTex = isTex;
	}

	public FCDocument(String fPath, String TP, String year, String start, String fName){
		this.setfPath(fPath);
		this.setTP(TP);
		this.setInputYear(year);
		this.setInputStart(start);
		this.setfName(fName);
		this.setPgNum3(3);
		
		this.setPaperType('t');
		if(this.TP.equals("Project")) paperType = 'p';
		
		if(this.getInputYear().equals("0")){
			this.setYearInt(Calendar.getInstance().get(Calendar.YEAR));
		}
		else{
			this.setYearInt(Integer.parseInt(this.getInputYear()));
		}
		
		this.setStartInt(Integer.parseInt(this.inputStart));
		this.setTex(false);
	}
	
	public boolean needToFindStartPage(){
		return this.getStartInt() == 0;
	}
	
	public void printDocInfo(){
		System.out.println(	"File path: " + this.getfPath() + "\n" + 
							"Paper type: " + this.getPaperType() + "\n" +
							"Year: " + this.getYearInt() + "\n" +
							"Start: " + this.getStartInt() + "\n" +
							"File dir: " + this.getfName() + "\n" +
							"Total Pages: " + this.getTotlaPagesCount());
	}

	public String printDocInfoStr(){
		String str = "";
		
		str += 	"File path: " + this.getfPath() + "; " + 
				"Paper type: " + this.getPaperType() + "; " +
				"Year: " + this.getYearInt() + "; " +
				"Start: " + this.getStartInt() + "; " +
				"File dir: " + this.getfName() + "; " +
				"Total Pages: " + this.getTotlaPagesCount();
		
		return str;
	}
	
	public boolean isCreatedByTex(String creater, String producer){
		if(creater.contains("TEX") || producer.contains("TEX")) return true;
		else return false;
	}
	
	public static String getFileNameFromPath(String path){
//		String fName1 = "";
		Path p = Paths.get(path);
		String fName1 = p.getFileName().toString();
		
//		if (path.indexOf("\\") > 0){
//			fName1 = path.substring(path.lastIndexOf("\\"), path.length());
//		}
//		if (path.indexOf("/") > 0){
//			fName1 = path.substring(path.lastIndexOf("/"), path.length());
//		}
		if (fName1.indexOf(".") > 0){
			fName1 = fName1.substring(0, fName1.lastIndexOf("."));
		}
		System.out.println(fName1);
		return fName1;
//		return fName1.substring(1);
	}
	
	public static boolean createDir(String dir){
		File theDir = new File(dir);
		// if the directory does not exist, create it
		boolean result = false;
		if (!theDir.exists()) {
		    try{
		        theDir.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		    }
		}
		else{
			result = true;
		}
		return result;
	}
	
	
	
	public static boolean run(String fPath, String tp, String curYear, String startPage) throws IOException{
//		C:\Users\USER\Desktop\formatcheckerrrrrr\016-1.pdf project 2015 0
		//C:\Users\USER\Desktop\formatcheckerrrrrr\016-1.pdf project 0 0
		//C:\Users\USER\Desktop\1208\Project_MasterTemplate_seqX.pdf project 0 0
		//C:\\Users\\USER\\Desktop\\1208\\workspace\\huh\\pdf\\project\\016.pdf project 2015 0
		
/*
 * 		fPath args[0], TP args[1], 
 * 		curYear args[2] (0 as default), 
 * 		contentPg args[3] (0 as default), 
 * 		fName from fpath;
 */		
//		String parsedFileName = getFileNameFromPath(args[0]);
//		Document doc = new Document(args[0], args[1], args[2], args[3], parsedFileName);
		String parsedFileName = getFileNameFromPath(fPath);
		FCDocument doc = new FCDocument(fPath, tp, curYear, startPage, parsedFileName);

		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		try{//whole main part
			boolean dirCreated = createDir(doc.getfName());
			if(dirCreated){
//				System.out.println(timeFormat.format(Calendar.getInstance().getTime()));
//				System.out.println("Directory is created");
				String outputLogPath = "./" + doc.getfName() + "/log.txt";
				PrintStream out = new PrintStream(new FileOutputStream(outputLogPath));
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Directory is created");
								
				/*Add results in an arraylist*/
				ArrayList<ItemResult> titlePageResult = new ArrayList<ItemResult>();
				ArrayList<ItemResult> signaturePageResult = new ArrayList<ItemResult>();
				ArrayList<ItemResult> regularPageResult = null;
				ArrayList<FontStyle> fontStyleUsed = new ArrayList<FontStyle>();
				ArrayList<Heading> headings = new ArrayList<Heading>();	
				
				/*read file*/
//				System.out.println("Reading file and gathering required information...");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Reading file and gathering required information");
				
				File file = new File(doc.getfPath());
				PDDocument pdfDoc = PDDocument.load(file);
				PDDocumentInformation pdfDocInfo = pdfDoc.getDocumentInformation();
				if(doc.isCreatedByTex(pdfDocInfo.getCreator().toUpperCase(), 
									  pdfDocInfo.getProducer().toUpperCase())){
					doc.setTex(true);
				}
				doc.setTotlaPagesCount(pdfDoc.getNumberOfPages());
				out.println(doc.printDocInfoStr());
				
				
				
//				mark start
				/*for output pdf*/
				//Document outputDoc = new Document();
				//outputDoc.setPageSize(PageSize.LETTER);
				//String outputFileName = "./" + doc.getfName() + "/result.pdf";
				//PdfWriter writer = PdfWriter.getInstance(outputDoc, new FileOutputStream(outputFileName));
//				Rectangle letterSize = new Rectangle(612,792);
//				writer.setPageSize(PageSize.LETTER);
				PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
				
				/*title page*/
//				System.out.println("Checking Title page...");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking Title page");
				
				TitlePage titlePage = new TitlePage();
				titlePage.getData(pdfDoc, 1, false);
				titlePage.collectSections(doc.getPaperType());
				titlePageResult = titlePage.check_PrintToList(doc.getPaperType(), doc.getYearInt(), doc.getfName());
//				HtmlHelper.print(doc.getfName(), titlePageResult, 1, doc.getTotlaPagesCount(), fPath);
				//print to a html file
				
				
				/* new method here */
				// return html string
				// use this string to print out to pdf file
				// pass String img to this function
				// if img is not null then means this page needs to output a img link
				// document, writer, XMLWorkerHelper
				// outputPDF(document, writer, XML_WH, htmlStr, String img) 
				// documnet close and save at last
				String imgPath = "";
				String html = HtmlHelper.htmlStr(doc.getfName(), titlePageResult, 1, imgPath);
				//Export2PDF.add(outputDoc, writer, html, imgPath);//print to a PDF page and add this page to outputDoc
				/*new method*/
				
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Title page check results is printed");
				
				ArrayList<String> itemsNeedToBeConsistent_TitlePage = titlePage.itemsNeedToBeConsistent();
				
				/*collect base font family*/
				FontStyle fs = null;
				fs = new FontStyle(1, titlePage.getFontFamily(), titlePage.getFontSize());
				fontStyleUsed.add(fs);
				
				/*collect first line (heading) info*/
				Heading hd = null;
//				hd = new Heading(1, titlePage.getFirstLine());
//				headings.add(hd);

				/*signature page*/
//				System.out.println("Checking Signature page...");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking Signature page");
				SignaturePage signaturePage = new SignaturePage();
				signaturePage.getData(pdfDoc, 2, false);
				signaturePage.collect(doc.getPaperType());		
				signaturePageResult = signaturePage.check_PrintToList(doc.getPaperType(), itemsNeedToBeConsistent_TitlePage, doc.getfName());
//				HtmlHelper.print(doc.getfName(), signaturePageResult, 2, doc.getTotlaPagesCount(), fPath);
				
				imgPath = "";
				html = HtmlHelper.htmlStr(doc.getfName(), signaturePageResult, 2, imgPath);
				//Export2PDF.add(outputDoc, writer, html, imgPath);
								
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Signature page check results is printed");

				fs = new FontStyle(2, signaturePage.getFontFamily(), signaturePage.getFontSize());
				fontStyleUsed.add(fs);

//				hd = new Heading(2, signaturePage.getFirstLine());
//				headings.add(hd);
				
				
				ArrayList<Page> allPages = new ArrayList<Page>();
				allPages.add(titlePage);
				allPages.add(signaturePage);
				
				

				Page tmpPage = null;
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Collecting all pages");
				for(int i=3; i< doc.getTotlaPagesCount(); ++i){
//					System.out.println(i);
					tmpPage = Page.getDataRetPg(pdfDoc, i, false);
					allPages.add(tmpPage);
				}
				
				/*last page*/
				tmpPage = Page.getDataRetPg(pdfDoc, 0, true); //0: doesn't matter
				allPages.add(tmpPage);
				
//				for(Glyph g : allPages.get(8).getGlyphs()){
//					System.out.println(g.getGl() + "," + g.getFontSize());
//				}
//				allPages.get(9).printFontInfo();
//				allPages.get(10).printFontInfo();
				
//				System.out.println("Collecting first lines/headings of each page...");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Collecting first lines/headings of each page");
//				Page tmpPage = new Page();
				for(int i=0; i<allPages.size(); ++i){ //i<pages.getCount()
//					System.out.println("Page " + i);
//					tmpPage.getDataFirstLine(pdfDoc, i);			
					hd = new Heading(i+1, allPages.get(i).getFirstLine());
					headings.add(hd);
				}
				
//				System.out.println("define headings");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Defining headings");
				int TOCIndex = -1;
				for(Heading hd1 : headings){
					hd1.defineHeading();
//					hd1.print();
					if(hd1.getPageType().equals("TOC")){
						TOCIndex = hd1.getPageNum();
					}
				}
				
				if(doc.getStartInt() == 0 && TOCIndex != -1){
					//then need to find content start page:
//					System.out.println("TOC page num: " + TOCIndex);
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Table of Content page num: " + TOCIndex);
//					System.out.println("FIND content start");
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Looking for main body start page number");
						int contentStartPgNum1 = -1;
						tmpPage = new Page();
						
						int searchRange = 10;
						if(doc.getTotlaPagesCount() > 50){
							searchRange = 25;
						}
						for(int i=TOCIndex; i<TOCIndex+searchRange; ++i){ //+5 if you're using template pdf
//							System.out.print("Page ");
//							System.out.println(i+1);
							
//							tmpPage.getDataFirstLine(filePath, pages.get(i), i);
							tmpPage = allPages.get(i);
							boolean isContentStart = tmpPage.isContentStart();
//							
							/*restore from here*/
							if(isContentStart) {
								contentStartPgNum1 = i;
								break;
							}
							/*to here*/
						}
//						System.out.println("content start: " + contentStartPgNum1+1);
						doc.setStartInt(contentStartPgNum1+1);
				}
				else{
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- failed to find content start or failed to define toc page num");
				}
				
//				System.out.println("Start checking regular page (from p3), content start page num:" + doc.getStartInt());
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + 
						"- Start checking regular page (from p3), main body start page num:" + doc.getStartInt());
//				System.out.println("Checking regular pages...");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking regular pages");
				
				Page regularPage = new Page();
				String dir = doc.getfName();
				for(int i=3; i<allPages.size(); ++i){ //i<pages.getCount()
//					System.out.println("Page " + i);
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking Page" + i);
					regularPage = allPages.get(i-1);
					
					regularPageResult = new ArrayList<ItemResult>();
					regularPageResult = regularPage.check_PrintToList(i, doc.getStartInt(), dir, pdfRenderer);
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Page" + i + " is checked");
					HtmlHelper.print(dir, regularPageResult, i, allPages.size(), fPath);
					
					imgPath = "";
					html = HtmlHelper.htmlStr(dir, regularPageResult, i, imgPath);
					imgPath = HtmlHelper.getImgPath(dir, i);
//					System.out.println(imgPath);
					//Export2PDF.add(outputDoc, writer, html, imgPath);
					
					
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Page" + i + " result is printed");

					fs = new FontStyle(i, regularPage.getFontFamily(), regularPage.getFontSize());
					fontStyleUsed.add(fs);

					
				}
				
				/*last page*/
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking last page");
				regularPage = allPages.get(allPages.size()-1);
				regularPageResult = new ArrayList<ItemResult>();
				regularPageResult = regularPage.check_PrintToList(allPages.size(), doc.getStartInt(), dir, pdfRenderer);
				HtmlHelper.print(dir, regularPageResult, allPages.size(), allPages.size(), fPath);
				
				imgPath = "";
				html = HtmlHelper.htmlStr(dir, regularPageResult, allPages.size(), imgPath);
				imgPath = HtmlHelper.getImgPath(dir, allPages.size());
				//Export2PDF.add(outputDoc, writer, html, imgPath);

				
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Last page checking result is printed");
							
				
				/*check font style consistency*/
				fs = new FontStyle(allPages.size(), regularPage.getFontFamily(), regularPage.getFontSize());
				fontStyleUsed.add(fs);

				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking font style through all pages");
				ArrayList<String> fontStyleSummary = FontStyle.markFontStyleChange(fontStyleUsed);
				HtmlHelper.fontStyleSummary(dir, fontStyleSummary);
				String fontStyleHtml = HtmlHelper.fontStyleHtmlStr(dir, fontStyleSummary);
				//Export2PDF.add(outputDoc, writer, fontStyleHtml, "");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Font style checking result is printed");
				
				
				/*Skip TOC process if the doc is a Tex*/
				ArrayList<TItem> allTableItems1 = new ArrayList<TItem>();
				if(doc.isTex()){
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- This file is created by Tex, checking Table of Contents is redundant");
					HtmlHelper.TOCSummary(dir, allTableItems1);
					String tocHtml = HtmlHelper.TOCSummary_HtmlStr(dir, allTableItems1);
					//Export2PDF.add(outputDoc, writer, tocHtml, "");
				}
				else{
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking Table of Content, List of Tables/Figures");
//					System.out.println("Checking Table of Content, List of Tables/Figures...");
					ArrayList<TItem> allTableItems = new ArrayList<TItem>();
					TOCPage TOC = new TOCPage();
					for(int i=TOCIndex; i<=doc.getStartInt()-1; ++i){ //i<pages.getCount()
						TOC = TOCPage.getDataRetPg(pdfDoc, i, false);
						TOC.defineTOCType();
						ArrayList<TItem> listOfCurrentPg = TOC.getTableInformation1(doc.getStartInt());
						
						for(TItem ti : listOfCurrentPg){
//							ti.print();
							allTableItems.add(ti);
						}
					}
					
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Collecting Table of Content, List of Tables/Figures info");
					allTableItems1 = GroupFunc.assignRealPgNum(allTableItems);
					
		//			for(TItem ti : allTableItems){
		//				ti.print();
		//			}
		//////			
		//			System.out.println("----------------------------------");
		//			
		//			for(TItem ti : allTableItems1){
		//				ti.print();
		//			}
		//			
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking existency of items from Table of Content, List of Tables/Figures");
					Page targetPage = new Page();
					for(TItem ti : allTableItems1){
						int targetIndex = ti.getRealPgNum()-1;
		//				System.out.println(targetIndex + " " + ti.getTargetPgNum());
						if(targetIndex < 0) continue;
						String raw = "";
						if(targetIndex >= allPages.size()){
		//					targetPage = allPages.get(allPages.size()-1);
		//					raw = targetPage.getRawText();
		//					System.out.println("raw:" + raw);
						}
						else{
							targetPage = allPages.get(targetIndex);
							raw = targetPage.getRawText();
						}
						
						raw = raw.toUpperCase();
						raw = StrFunc.getCharsOnly(raw);
						
						String cmp = ti.getName().toUpperCase();
						cmp = StrFunc.getCharsOnly(cmp);
						
						if(raw.contains(cmp)) ti.setItemExists(true);
						else ti.setItemExists(false);
					}
					
		//			for(TItem ti : allTableItems){
		//				if(ti.getRealPgNum() == -1) continue;
		//				System.out.println(	ti.getName() + " in " + ti.getRealPgNum() + 
		//									"->" + ti.isItemExists() + " (" + ti.getTargetPgNum() + ")");
		//				
		//			}
					
					HtmlHelper.TOCSummary(dir, allTableItems1);
					String tocHtml = HtmlHelper.TOCSummary_HtmlStr(dir, allTableItems1);
					//Export2PDF.add(outputDoc, writer, tocHtml, "");
					out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Table of Content, List of Tables/Figures summary is printed");
				}
				
				
				
				/*check firstline (heading) is bold and caps in the page*/
//				System.out.println("check heading is bold and caps in the page");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking heading style");
				for(Heading h : headings){
					h.boldAndCapsInPage(allPages, h.getPageNum());
				}
				
				/* export this to html
				 * haven't done this yet!
				 * */
				ArrayList<HeadingResult> headingResults = new ArrayList<HeadingResult>();
				
				for(Heading h : headings){
					h.boldCapsResult();
					HeadingResult hr = h.buildHeadingItem();
					headingResults.add(hr);
				}
				
				
//				System.out.println("Check sequence:");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Checking sequence of parts");
				String seq = ContentOrder.check(headings);
				
				HtmlHelper.HeadingSummary(dir, headingResults, seq);
				String headingSumHtml = HtmlHelper.HeadingSummary_HtmlStr(dir, headingResults, seq);
				out.println(headingSumHtml);
				//Export2PDF.add(outputDoc, writer, headingSumHtml, "");
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- Sequence of parts and heading styles result is printed");
				
//				pdfDoc.save(new File("C:\\Users\\USER\\Desktop\\1208\\workspace\\huh\\pdf\\project\\016-new.pdf"));
				
				
				out.println(timeFormat.format(Calendar.getInstance().getTime()) + "- End of process");
								
				out.close();
				pdfDoc.close();
				
				
				return true;
				
			}
			else{
//				System.out.println("Directory is not created");
				return false;
			}
		}
		
		catch(Exception e){
			return false;
		}
//		mark end
	}

	
	
	
}

package edu.cpplib.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import edu.cpplib.table.TItem;

public class HtmlHelper {
	/*
	 * print result to html or return html string in order to out put to a PDF file
	 * Page check result (table and page)
	 * TOC check result (table and summary)
	 * Font style result (summary)
	 * 
	 */

	/* check if image of wrong font styles existed*/
	public static boolean isImgExisted(String dir, int pgNum){
		String imgName = "./" + dir + "/" + pgNum + ".png";
		File f = new File(imgName);
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		else{
			return false;
		}
	}
	
	/* if existed get the path */
	public static String getImgPath(String dir, int pgNum){
		String imgPath = "";
		if(isImgExisted(dir,pgNum)){
			imgPath = "./" + pgNum + ".png";
		}
		return imgPath;
	}

	
	/* generate page result (table part) to html string */
	public static String buildPageTable_HtmlStr (ArrayList<ItemResult> ir){
		String ret = "";
		ret += "<tr>"
			+ "<td width=\"15%\"><b>Checked Item</b></td> "
			+ "<td width=\"30%\"><b>Value</b></td> "
			+ "<td width=\"40%\"><b>Note</b></td>"
			+ "<td width=\"5%\"><b></b></td> "
			+ "</tr>";
		
		for(int i=0; i<ir.size(); ++i){
			if(i >= 20) break;
			ret += "<tr>"
				+ "<td>" + ir.get(i).getCheckItem() + "</td>" 
				+ "<td style=\"word-break: break-all;width: 300px\">" + ir.get(i).getValue() + "</td>"
				+ "<td>" + ir.get(i).getNote() + "</td>";
//				+ "<td>" + ir.get(i).getResult() + "</td>"
				if(!ir.get(i).getResult()){
					ret += "<td> <font color=\"#ff0066\">X</font></td>"
							+ "</tr>";
				}
				else{
					ret += "<td>OK</td>"
					+ "</tr>";
				}
		}
		return ret;
	}
	
	/* generate page result to html string (a page)*/
	public static String htmlStr(String fname, ArrayList<ItemResult> ir, int p, String imgPath) 
			throws IOException{

		String str = "";

		str += "<!DOCTYPE html>";
		str += "<html><head>";
		
		str += "<style> "
				+ "table, th, td, tr { border: 1px solid black; "
				+ "border-collapse: collapse;}"
				+ "th, td { padding: 5px;}"
				+ "</style>";

			
		str += "<title>" + p + "</title>";
		str += "</head><body>";
//		String pageAnchor = "page" + p;
//		<a name="myanchor">This is the destination of a link</a>
//		str += "<a name =\"";
//		str += pageAnchor + "\"" +  ">" + "Page " + p + "</a>";
		str += "<p> Page " + p + "</p>";
		str += "<table style=\"width:100%\">";
		str += buildPageTable_HtmlStr(ir);

		str += "</table>";
		
		boolean imgExisted = isImgExisted(fname, p);
		
		if(imgExisted){
			String img = "./" + p + ".png";
//			String fPath_Pg = fPath + "#page=" + p;
			
			str +=  "<img src = \" " + img + " \" />";
		}
		
		
		str += "</body></html>";
		
//		System.out.print(str);
		
		return str;
	}
	
	
	/* generate font styles consistency summary */
	
	public static String fontStyleHtmlStr(String fname, ArrayList<String> fs) 
			throws FileNotFoundException, UnsupportedEncodingException{
		
		String str = "";
//		String exportFileName = "./" + fname + "/" + fname + "-" + "font_style_sum";
//		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		str += "<!DOCTYPE html>";
		str += "<html><head><title>Font style summary</title></head><body>";
		str += "Font style in each page<br/>";

		for(String s : fs){
			str += s;
			str += "<br/>";
		}

		str += "</body></html>";
		
		return str;
	}
	
	/* generate toc table */
	public static String buildTOCTable_HtmlStr(ArrayList<TItem> allTableItems){
		String str = "";
		str += "<tr>"
				+ "<td width=\"30%\"><b>Item Name</b></td> "
				+ "<td width=\"10%\"><b>Real Page Number</b></td> "
				+ "<td width=\"10%\"><b>PDF Page Number</b></td> "
				+ "<td width=\"10%\"><b>Existence</b></td>"
				+ "</tr>";
		
		for(TItem ti : allTableItems){
			if(ti.getRealPgNum() == -1){
				str += "<tr>"
						+ "<td>" + ti.getName() + "</td>" 
						+ "<td>" + ti.getRealPgNum() + "</td>"
						+ "<td>" + ti.getTargetPgNum() + "</td>";
						if(!ti.isItemExists()){
							str += "<td> <font color=\"#ff0066\">" + ti.isItemExists() + "</font></td>"
									+ "</tr>";
						}
						else{
							str += "<td>" + ti.isItemExists() + "</td>"
									+ "</tr>";
						}
			}
			if(ti.getName().length() < 1) continue;
				str += "<tr>"
						+ "<td>" + ti.getName() + "</td>"
						+ "<td>" + ti.getRealPgNum() + "</td>" //<a href="#myanchor">Click here to go to my anchor</a>
						+ "<td>" + ti.getTargetPgNum() + "</td>";
						if(!ti.isItemExists()){
							str += "<td> <font color=\"#ff0066\">" + ti.isItemExists() + "</font></td>"
									+ "</tr>";
						}
						else{
							str += "<td>" + ti.isItemExists() + "</td>"
									+ "</tr>";
						}
		}
		
		return str;
	}
	
	public static String TOCSummary_HtmlStr(String fname, ArrayList<TItem> allTableItems) 
			throws FileNotFoundException, UnsupportedEncodingException{
		String str = "";
//		String exportFileName = "./" + fname + "/" + fname + "-" + "toc_sum"; //001.pdf-1, 001.pdf-2, 001.pdf-3...
//		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		str += "<!DOCTYPE html>";
		str += "<html><head>"
				+ "<style> table, th, td, tr { border: 1px solid black; border-collapse: collapse;}th, td { padding: 5px;}</style>"
				+ "<title>Table Of Content, List Of Figure, List Of Table summary</title>"
				+ "</head><body>";
		
		str += "Table of Content, List of Figures, List of Tables check:<br/>";
		if(allTableItems.size()>1){
			str += "<table style=\"width:100%\">";
			str += buildTOCTable_HtmlStr(allTableItems);
			str += "</table>";
		}
		else{
			str += "This file was created by Tex, Table of Contents, List of Figures/Tables were generated automatically.";
		}

		str += "</body></html>";
		return str;
		
	}
	
	public static String buildHeadingFormatTable_HtmlStr(ArrayList<HeadingResult> headingResults){
		String str = "";
		str += "<tr>"
				+ "<td width=\"10%\"><b>Heading</b></td> "
				+ "<td width=\"6%\"><b>Bold</b></td> "
				+ "<td width=\"6%\"><b>Caps</b></td> "
				+ "<td width=\"6%\"><b>Real Page Number (Merged) </b></td> "
				+ "</tr>";
		
		for(HeadingResult hr : headingResults){
			if(hr.getPage() != -1){
				int mergedPgNum = 2*hr.getPage()-1;
				str += "<tr>"
						+ "<td>" + hr.getHeading() + "</td>" 
						+ "<td>" + hr.isBold() + "</td>"
						+ "<td>" + hr.isCaps() + "</td>"
						+ "<td>" + hr.getPage() + " ( " + mergedPgNum + " )</td>"
						+ "</tr>";
			}
		}
		
		return str;
	}
	
	public static String HeadingSummary_HtmlStr(String fname, ArrayList<HeadingResult> headingResults, String seq)
			throws FileNotFoundException, UnsupportedEncodingException{
		String str = "";
//		String exportFileName = "./" + fname + "/" + fname + "-" + "heading_sum"; 
//		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		str += "<!DOCTYPE html>";
		str += "<html><head><title>Heading Summary</title>"
				+ "<style> "
				+ "table, th, td, tr { border: 1px solid black; "
				+ "border-collapse: collapse;}"
				+ "th, td { padding: 5px;}"
				+ "</style>"
				+ "</head><body>";
		str += "Sequence of parts and heading format check:<br/>";
		str += seq;
		
		str += "<table style=\"width:60%\">";
		str += buildHeadingFormatTable_HtmlStr(headingResults);
		str += "</table>";
		str += "</body></html>";
		
		return str;
	}
	
	
	
	
	
	/*
	 * print to html, currently they are not used
	 */
	public static void buildPageTable(PrintWriter writer, ArrayList<ItemResult> ir){
		writer.println("<tr>"
						+ "<td width=\"15%\"><b>Checked Item</b></td> "
						+ "<td width=\"30%\"><b>Value</b></td> "
						+ "<td width=\"5%\"><b>Result</b></td> "
						+ "<td width=\"40%\"><b>Note</b></td>"
						+ "</tr>");
		
		for(int i=0; i<ir.size(); ++i){
			writer.println(	"<tr>"
							+ "<td>" + ir.get(i).getCheckItem() + "</td>" 
							+ "<td style=\"word-break: break-all;width: 300px\">" + ir.get(i).getValue() + "</td>"
							+ "<td>" + ir.get(i).getResult() + "</td>"
							+ "<td>" + ir.get(i).getNote() + "</td>"
							+ "</tr>");
		}
	}
	
	
	public static void buildTOCTable(PrintWriter writer, ArrayList<TItem> allTableItems){
		writer.println("<tr>"
						+ "<td width=\"30%\"><b>Item Name</b></td> "
						+ "<td width=\"10%\"><b>Real Page Number</b></td> "
						+ "<td width=\"10%\"><b>PDF Page Number</b></td> "
						+ "<td width=\"10%\"><b>Existence</b></td>"
						+ "</tr>");
		
		for(TItem ti : allTableItems){
			if(ti.getRealPgNum() == -1){
				writer.println(	"<tr>"
						+ "<td>" + ti.getName() + "</td>" 
						+ "<td>" + ti.getRealPgNum() + "</td>"
						+ "<td>" + ti.getTargetPgNum() + "</td>"
						+ "<td>" + ti.isItemExists() + "</td>"
						+ "</tr>");
			}
			if(ti.getName().length() < 1) continue;
			writer.println(	"<tr>"
							+ "<td>" + ti.getName() + "</td>" 
							+ "<td>" + ti.getRealPgNum() + "</td>"
							+ "<td>" + ti.getTargetPgNum() + "</td>"
							+ "<td>" + ti.isItemExists() + "</td>"
							+ "</tr>");
		}
	}
	
	
	public static void buildHeadingFormatTable(PrintWriter writer, ArrayList<HeadingResult> headingResults){
		writer.println("<br>");
		writer.println("<tr>"
				+ "<td width=\"10%\"><b>Heading</b></td> "
				+ "<td width=\"6%\"><b>Bold</b></td> "
				+ "<td width=\"6%\"><b>Caps</b></td> "
				+ "<td width=\"6%\"><b>Real Page Number</b></td> "
				+ "</tr>");
		
		for(HeadingResult hr : headingResults){
			if(hr.getPage() != -1){
				writer.println(	"<tr>"
						+ "<td>" + hr.getHeading() + "</td>" 
						+ "<td>" + hr.isBold() + "</td>"
						+ "<td>" + hr.isCaps() + "</td>"
						+ "<td>" + hr.getPage() + "</td>"
						+ "</tr>");
			}

		}
	}
	
	

	public static void print(String fname, ArrayList<ItemResult> ir, int p, int total, String fPath) 
			throws IOException{
		
		String exportFileName = "./" + fname + "/" + fname + "-" + p; //001.pdf-1, 001.pdf-2, 001.pdf-3...
		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		writer.println("<!DOCTYPE html>");
		writer.println("<html><head>");
		
		writer.println("<style> "
						+ "table, th, td, tr { border: 1px solid black; "
						+ "border-collapse: collapse;}"
						+ "th, td { padding: 5px;}"
						+ "</style>");

			
		writer.println("<title>" + p + "</title>");
		writer.println("</head><body>");
		
		writer.println("<table style=\"width:100%\">");
		buildPageTable(writer, ir);
		writer.print("</table>");
		
//		System.out.println("is img existed? " + isImgExisted(fname, p));
		
		boolean imgExisted = isImgExisted(fname, p);
		
		if(imgExisted){
			String imgPath = "./" + p + ".png";
//			String fPath_Pg = fPath + "#page=" + p;
			
			writer.print("Image of wrong font style used: "
						+ "<table style=\"width:612\">"
						+ "<td><img src = \" " + imgPath + " \" /></td>"
						+ "</table>");
		}
		

		String next = "";
		if(p < total){
			int pPlus1 = p+1;
			next = fname + "-" + pPlus1 + ".html";
			writer.println("<a href = \"" + next + "\">" + "Next Page >>" + "</a>");
		}
		else if(p == total){
			//next page will be font style consistency through pages
			writer.println("<a href = \"" + fname + "-font_style_sum.html" + "\">" + "Next Page >>" + "</a>");
		}

		writer.println("</body></html>");
		writer.close();
	}
	
	
	public static void fontStyleSummary(String fname, ArrayList<String> fs) 
			throws FileNotFoundException, UnsupportedEncodingException{
		
		String exportFileName = "./" + fname + "/" + fname + "-" + "font_style_sum";
		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		writer.println("<!DOCTYPE html>");
		writer.println("<html><head></head><body>");
		
		writer.println("<title>Font style summary</title>");
		writer.print("<h1>Font style in each page: (Page Number : Font style used)</h1></br>");

		
		for(String s : fs){
			writer.println(s + "</br>");
		}

		/*next: content order*/
		writer.println("</br></br><a href = \"" + fname + "-heading_sum.html" + "\">" + "Next Page >>" + "</a>");
		writer.println("</body></html>");
		
		writer.close();
	}
	
	
	public static void HeadingSummary(String fname, ArrayList<HeadingResult> headingResults, String seq)
			throws FileNotFoundException, UnsupportedEncodingException{
		
		String exportFileName = "./" + fname + "/" + fname + "-" + "heading_sum"; 
		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		writer.println("<!DOCTYPE html>");
		writer.println("<html><head></head><body>");
		writer.println("<h1>Sequence of parts and heading format check:</h1></br>");
		writer.print(seq);
		
		writer.println("<style> "
				+ "table, th, td, tr { border: 1px solid black; "
				+ "border-collapse: collapse;}"
				+ "th, td { padding: 5px;}"
				+ "</style>");
		writer.println("<table style=\"width:60%\">");
		buildHeadingFormatTable(writer, headingResults);
		writer.println("</table>");
		writer.println("</br></br><a href = \"" + fname + "-toc_sum.html" + "\">" + "Next Page >>" + "</a>");
		writer.println("</body></html>");
		
		writer.close();
	}
	
	
	
	public static void TOCSummary(String fname, ArrayList<TItem> allTableItems) 
			throws FileNotFoundException, UnsupportedEncodingException{

		String exportFileName = "./" + fname + "/" + fname + "-" + "toc_sum"; //001.pdf-1, 001.pdf-2, 001.pdf-3...
		PrintWriter writer = new PrintWriter(exportFileName + ".html", "UTF-8");
		writer.println("<!DOCTYPE html>");
		writer.println("<html><head></head><body>");
		writer.println("<h1>Table of Content, List of Figures, List of Tables check:</h1></br>");
		if(allTableItems.size()>1){
			writer.println("<style> "
					+ "table, th, td, tr { border: 1px solid black; "
					+ "border-collapse: collapse;}"
					+ "th, td { padding: 5px;}"
					+ "</style>");
			writer.println("<title>" + "Result" + "</title>");
			writer.println("<table style=\"width:100%\">");
			buildTOCTable(writer, allTableItems);
			writer.println("</table>");
		}
		else{
			writer.println("This file was created by Tex, Table of Contents, List of Figures/Tables were generated automatically.<br>");
			writer.println("Hence, checking the correctness for them is redundant.<br>");
		}
		writer.println("</br></br>End");
		writer.println("</body></html>");
		
		writer.close();

	}
	

}

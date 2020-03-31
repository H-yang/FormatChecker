package edu.cpplib.export;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Export2PDF {
	
	public static void add(Document document, PdfWriter writer,String htmlStr, String img) throws IOException, DocumentException{
		document.open();
		InputStream html = new ByteArrayInputStream(htmlStr.getBytes());
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, html);
//		System.out.println("img" + img.length() + "," + img);
		if(img.length() >0){
	        Font blue = new Font(FontFamily.COURIER, 12, Font.NORMAL, BaseColor.BLUE);
	        Chunk blueText = new Chunk("View img comparison here >> ", blue);
	        
	        Paragraph paragraph = new Paragraph(blueText);
	
	        Anchor anchor = new Anchor("img");
	        anchor.setReference(img);
	
	        paragraph.add(anchor);
	        document.add(paragraph);
		}
		document.newPage();
	}
	
	
	
	
//	public static void printMargins(Page regularPage, PDPage page, PDDocument pdfDoc) throws IOException{
//		PDPageContentStream contentStream 
//			= new PDPageContentStream(pdfDoc, page, PDPageContentStream.AppendMode.APPEND, true, true);
//		contentStream.beginText(); 
//		contentStream.setFont(PDType1Font.COURIER, 12);
//		
//		double t = Math.round(regularPage.getTopMargin() * 100.0) / 100.0;
//		double b = Math.round(regularPage.getBottomMargin() * 100.0) / 100.0;
//		double l = Math.round(regularPage.getLeftMargin() * 100.0) / 100.0;
//		double r = Math.round(regularPage.getRightMargin() * 100.0) / 100.0;
//		
//		System.out.println(regularPage.getHeight() - regularPage.getTopMargin()*72 +48);
//		
//		contentStream.newLineAtOffset(regularPage.getLeftMargin()*72 + 5, 757);
//		contentStream.showText("Top Margin: " + t + ", Bottom Margin: " + b);
//		
//		contentStream.newLineAtOffset(0, -20);
//		contentStream.showText("Left Margin: " + l + ", Right Margin: " + r);
//		contentStream.endText();
//		contentStream.close();		
//	}
//	
//	public static PDPage printTitlePage(ArrayList<ItemResult> titlePageResult, PDDocument pdfDoc) 
//			throws IOException{
//		PDPage page = new PDPage();
//		@SuppressWarnings("deprecation")
//		PDPageContentStream contentStream 
//			= new PDPageContentStream(pdfDoc, page, PDPageContentStream.AppendMode.APPEND, true, true);
//		contentStream.beginText();
//		contentStream.setFont(PDType1Font.COURIER, 12);
//		contentStream.newLineAtOffset(72, 700);
//
//		for (ItemResult ir : titlePageResult){
////			String curLine = ir.getCheckItem() + " : " + ir.getValue() + " : " + ir.getNote() + " : " + ir.getResult();
////			System.out.println(ir.getCheckItem() + " : " + ir.getValue() + " : " + ir.getNote() + " : " + ir.getResult());
//			contentStream.showText(ir.getCheckItem() + " : " + ir.getValue() + " : " + ir.getNote() + " : " + ir.getResult());
//			contentStream.newLineAtOffset(0, -15);
//		}
//		contentStream.endText();
//		contentStream.close();
//		return page;
//
//	}

	
}

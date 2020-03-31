package edu.cpplib.fc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class New {
	/*html->pdf converter test*/
	public static void main(String args[]) throws IOException, DocumentException{
		
		// step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("pdf.pdf"));
        // step 3
        document.open();
        // step 4
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream("/home/hsuan/Documents/workspace/016-n.html"),Charset.forName("cp1252"));
        document.newPage();
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream("/home/hsuan/Documents/workspace/toc_sum.html"),Charset.forName("cp1252")); 
//        Font red = new Font(FontFamily.COURIER, 12, Font.NORMAL, BaseColor.RED);
//        Chunk redText = new Chunk("View img comparison here >> ", red);
//        
//        Paragraph paragraph = new Paragraph(redText);
////        paragraph.add(new Phrase("You can find the IText tutorial at "));
//
//        Anchor anchor = new Anchor(
//            "img");
//        anchor.setReference(
//            "http://tutorials.jenkov.com/java-itext/index.html");
//
//        paragraph.add(anchor);
//
//        document.add(paragraph);
        
        //step 5
        document.close();
 
        System.out.println( "PDF Created!" );
	}
}

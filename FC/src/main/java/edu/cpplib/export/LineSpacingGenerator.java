package edu.cpplib.export;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import edu.cpplib.line.Line;

public class LineSpacingGenerator {
	
	public void output2PDF(ArrayList<Line> lines) throws IOException{
		 PDDocument document = new PDDocument();

	      for (int i=0; i<10; i++) {
	        //Creating a blank page 
	        PDPage blankPage = new PDPage();

	        //Adding the blank page to the document
	        document.addPage(blankPage);
	         
	        PDFont font = PDType1Font.TIMES_ROMAN;

			// Start a new content stream which will "hold" the to be created content
			PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);
			// Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
			contentStream.beginText();
			contentStream.setFont( font, 12 );
			contentStream.moveTextPositionByAmount( 100, 700 );
			contentStream.drawString( "Hello World" );
			contentStream.endText();
			
			// Make sure that the content stream is closed:
			contentStream.close();
	         
	         
	      } 
	     
	      //Saving the document
	      document.save("C:/PdfBox_Examples/my_doc.pdf");
	      System.out.println("PDF created");
	      
	      //Closing the document
	      document.close();
	}
}

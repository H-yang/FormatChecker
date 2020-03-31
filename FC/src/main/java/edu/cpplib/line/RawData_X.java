package edu.cpplib.line;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;


public class RawData_X extends PDFTextStripper {

	public RawData_X() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}


	
	public static List<String> raw = new ArrayList<String>();


	public static float parsePageSize(File file, int pageNum, char wh) throws IOException{
		PDDocument doc = null;
		try{
			doc = PDDocument.load(file);
			
			PDPage page = doc.getPage(pageNum-1);
			if(wh == 'w'){
				return page.getMediaBox().getWidth();
			}
			else if(wh == 'h'){
				return page.getMediaBox().getHeight();
			}
			else{
				return -1;
			}
		}
		finally{
			if (doc != null){
				doc.close();
			}
		}
	}
	
	public static ArrayList<String> build(File file) throws IOException{

		PDDocument doc = null;
//		File file = new File(filePath);
		
		
		
		
		try{
			doc = PDDocument.load(file);
//			PDPage page = doc.getPage(pageNum);
			System.out.println(doc.getNumberOfPages());
			PDPageTree pages = doc.getPages();
			int c = 1;
			for(PDPage i : pages){
				System.out.println("page " + c);
				System.out.println(i.getMediaBox().getHeight());
				System.out.println(i.getMediaBox().getWidth());
				PDFTextStripper stripper = new RawData_X();
				stripper.setSortByPosition(true);
				Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
	            stripper.writeText(doc, dummy);
				c++;
			}
//			System.out.println("PAGE SIZE HEIGHT" + page.getMediaBox().getHeight());
//			System.out.println("PAGE SIZE WIDTH" + page.getMediaBox().getWidth());
//			
//			PDFTextStripper stripper = new RawData();
			
//			stripper.setSortByPosition(true);
//			stripper.setStartPage(pageNum);
//			stripper.setEndPage(pageNum);
//			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
//            stripper.writeText(doc, dummy);
            

            return (ArrayList<String>) raw;
      
		}
		finally{
            if( doc != null ){
                doc.close();
            }
		}
		
	}



	
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException{
		String tmp = "";
        for (TextPosition position : textPositions)
        {
        	tmp += position.getXDirAdj();
        	tmp += ";";
        	tmp += position.getYDirAdj();
        	tmp += ";";
        	tmp += position.getFontSizeInPt();
        	tmp += ";";
        	tmp += position.getFont();
        	tmp += ";";
        	tmp += position.toString();

        	tmp += "#\n";
        }
//        tmp += "#";
//        System.out.println(tmp);
        raw.add(tmp);
	}

}
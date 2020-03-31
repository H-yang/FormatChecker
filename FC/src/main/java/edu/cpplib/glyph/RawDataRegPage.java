package edu.cpplib.glyph;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class RawDataRegPage extends PDFTextStripper {

	public RawDataRegPage() throws IOException {
		super();
	}
	
//	public static List<FontStyleGlyph> raw = new ArrayList<>();

	static ArrayList<TextPosition> pos = new ArrayList<TextPosition>();
	
	public static ArrayList<TextPosition> build(PDDocument doc, int num, boolean isLast) throws IOException{

		try{
//			raw.clear();
			int lastPage = doc.getNumberOfPages();
			
			PDFTextStripper stripper = new RawDataRegPage();
			stripper.setSortByPosition(true);
			
			if(isLast){
				stripper.setStartPage(lastPage); //num
				stripper.setEndPage(lastPage); //num
			}
			else{
				stripper.setStartPage(num); //num
				stripper.setEndPage(num); //num
			}

			
			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(doc, dummy);

//    		for(FontStyleGlyph t : raw){
//    			System.out.println(t.getFontFamily() + t.getFontSizePt());
//    		}
            return (ArrayList<TextPosition>) pos;
      
		}
		finally{
//            if( doc != null ){
//                doc.close();
//            }
		}
		
	}



	
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException{

//		String tmp = "";
//		FontStyleGlyph fsg = new FontStyleGlyph();
        for (TextPosition position : textPositions)
        {
        	pos.add(position);
//        	if(position.getDir() == 0){
//            	tmp += position.getXDirAdj();
//            	tmp += ";";
//            	tmp += position.getYDirAdj();
//            	tmp += ";";
//            	tmp += position.getFontSizeInPt();
//            	tmp += ";";
//            	tmp += position.getFont();
//            	tmp += ";";
//            	tmp += position.toString();
//            	tmp += ";";
//            	tmp += position.getDir();
//            	tmp += "#\n";
//            	System.out.println(position.getFont().toString() + position.getFontSizeInPt() + position.toString());
//            	fsg.setFontFamily(position.getFont().toString());
//            	fsg.setFontSizePt(position.getFontSizeInPt());
//            	fsg.setGlyph(position.toString());
//            	raw.add(fsg);
//        	}
     
        }
//        tmp += "#";
        
        
	}

}

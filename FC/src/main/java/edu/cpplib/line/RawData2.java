package edu.cpplib.line;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorN;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingColorSpace;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceCMYKColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceGrayColor;
import org.apache.pdfbox.contentstream.operator.color.SetNonStrokingDeviceRGBColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColorN;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingColorSpace;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceCMYKColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceGrayColor;
import org.apache.pdfbox.contentstream.operator.color.SetStrokingDeviceRGBColor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;


public class RawData2 extends PDFTextStripper {

	public RawData2() throws IOException {
		super();
		addOperator(new SetStrokingColorSpace());
		addOperator(new SetNonStrokingColorSpace());
		addOperator(new SetStrokingDeviceCMYKColor());
		addOperator(new SetNonStrokingDeviceCMYKColor());
		addOperator(new SetNonStrokingDeviceRGBColor());
		addOperator(new SetStrokingDeviceRGBColor());
		addOperator(new SetNonStrokingDeviceGrayColor());
		addOperator(new SetStrokingDeviceGrayColor());
		addOperator(new SetStrokingColor());
		addOperator(new SetStrokingColorN());
		addOperator(new SetNonStrokingColor());
		addOperator(new SetNonStrokingColorN());
	}
	
	public static List<String> raw = new ArrayList<String>();


	
	public static ArrayList<String> build(PDDocument doc, int num, boolean isLast) throws IOException{

//		PDDocument doc = null;
//		File file = new File(filePath);

		try{
			raw.clear();
//			doc = PDDocument.load(file);
			
			
			
//			PDPage page = doc.getPage(pageNum);
//			System.out.println(doc.getNumberOfPages());
//			PDPageTree pages = doc.getPages();
//			int c = 1;
//			for(PDPage i : pages){
//				System.out.println("page " + c);
			
//			System.out.println(page.getMediaBox().getHeight());
//			System.out.println(page.getMediaBox().getWidth());

			
			int lastPage = doc.getNumberOfPages();
//			int last = lastPage -1;
//			tmpPage = Page.getDataRetPg(pdfDoc, last);
//			allPages.add(tmpPage);
//			
//			tmpPage.getData(doc.getfPath(), pages.get(last), lastPage);
//			allPages.add(tmpPage);
			
//			if(num == lastPage){
//				num = lastPage;
//			}
			
			PDFTextStripper stripper = new RawData2();
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
            

//				c++;
//			}
//			System.out.println("PAGE SIZE HEIGHT" + page.getMediaBox().getHeight());
//			System.out.println("PAGE SIZE WIDTH" + page.getMediaBox().getWidth());
//			
//			PDFTextStripper stripper = new RawData();
			
//			stripper.setSortByPosition(true);
//			stripper.setStartPage(pageNum);
//			stripper.setEndPage(pageNum);
//			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
//            stripper.writeText(doc, dummy);
            

            
//            PDPage page1 = doc.getPage(1);
//            PDFStreamEngine engine = new PDFStreamEngine();
//            engine.processPage(page1);
//            PDGraphicsState pdg = engine.getGraphicsState();
//            System.out.println(pdg.getNonStrokingColor());
            
            
            
            
            
            
            return (ArrayList<String>) raw;
      
		}
		finally{
//            if( doc != null ){
//                doc.close();
//            }
		}
		
	}



	
	@Override
	protected void writeString(String string, List<TextPosition> textPositions) throws IOException{

		String tmp = "";
        for (TextPosition position : textPositions)
        {
//        	if(position.getDir() == 0){
            	tmp += position.getXDirAdj();
            	tmp += ";";
            	tmp += position.getYDirAdj();
            	tmp += ";";
            	tmp += position.getFontSizeInPt();
            	tmp += ";";
            	tmp += position.getFont();
            	tmp += ";";
            	tmp += position.toString();
            	tmp += ";";
            	tmp += position.getDir();
            	tmp += "#\n";
//        	}

        }
//        tmp += "#";
//        System.out.println(tmp);
        raw.add(tmp);
	}

}
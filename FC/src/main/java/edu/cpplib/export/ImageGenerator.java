package edu.cpplib.export;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import edu.cpplib.StrFunction.StrFunc;
import edu.cpplib.glyph.Glyph;
import edu.cpplib.line.Line;

public class ImageGenerator {
	
	
	public boolean containsSomething(BufferedImage bfImg){
        int w = 612;
        int h = 792;
        int[][] pixels = new int[w][h];

        boolean flag = false;
        for( int i = 0; i < w; i++ ){
            for( int j = 0; j < h; j++ ){
            	pixels[i][j] = bfImg.getRGB( i, j );
            	if(pixels[i][j] != -1){
            		flag = true;
            	}
            }
//            System.out.println();
        }
        
        return flag;
	}
	
	public void run(ArrayList<Glyph> differentFontFamily, String pgNum, String dir) throws IOException{
		
//		String key = "Sample";
        BufferedImage bufferedImage = new BufferedImage(612, 792, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 612, 792);//612,792
        graphics.setColor(Color.RED);
        
        for(Glyph g : differentFontFamily){
        	if(g.getFontFamily() != null){
//        		String glyphInStr = "" + g.getGl();
        		if(g.getFontFamily().contains("Math") || g.getFontFamily().contains("MATH")){
        			graphics.setColor(Color.BLUE);
        		}
        		if(g.getFontFamily().contains("Symbol") || g.getFontFamily().contains("SYMBOL")){
        			graphics.setColor(Color.BLUE);
        		}
        		if(g.getFontSize() == 7){
        			graphics.setColor(Color.BLUE);
        		}
                graphics.setFont(new Font("Arial", Font.PLAIN, 11)); //arial doesn't mean anything
                int left = (int) g.getLeftPos();
                int top = (int) g.getTopPos();
                String glyphInStr = "" + g.getGl();

            	graphics.drawString(glyphInStr, left, top);
        	}

        }
        
        
        if(this.containsSomething(bufferedImage)){
            String export = "./" + dir + "/" + pgNum + ".png";
            ImageIO.write(bufferedImage, "png", new File(export));
        }

	}
	
	
	
	
	public void generateSpacing(ArrayList<Line> lines, String pgNum, String dir) throws IOException{
		
//		String key = "Sample";
        BufferedImage bufferedImage = new BufferedImage(612, 792, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 612, 792);
        graphics.setColor(Color.BLACK);
        
        for(Line line : lines){
        	if(StrFunc.containsChars(line.getText())){
                graphics.setFont(new Font("Arial", Font.PLAIN, 10)); //arial doesn't mean anything
                int left = (int) line.getLeftPos();
                int top = (int) line.getTopPos();
//                String glyphInStr = "" + g.getGl();
                
            	graphics.drawString(line.getText(), left, top);
        	}
        }
        
        float diff = 0;
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("Arial", Font.PLAIN, 9));
		for(int i=1; i<lines.size(); ++i){
			diff = lines.get(i).getTopPos() - lines.get(i-1).getTopPos();
			if(diff >0){
				String d = diff + "";
				int left = (int) lines.get(i).getLeftPos();
	            int top0 = (int) lines.get(i).getTopPos();
	            int top1 = (int) lines.get(i-1).getTopPos();
	            int top = (top0+top1)/2;
				graphics.drawString(d, left, top);
			}
		}
        
        
        if(this.containsSomething(bufferedImage)){
        	
            String export = "./" + dir + "/" + pgNum + "-spacing.png";
            ImageIO.write(bufferedImage, "png", new File(export));
        }

	}
	
	
	
	
	
	
	
}




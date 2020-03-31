package edu.cpplib.StrFunction;

import java.nio.charset.Charset;

public class StrFunc {

	public static String elimSpaces(String src){
		char[] chars = src.toCharArray();
		String s = "";
		for(char c : chars){
			if(Character.isWhitespace(c)) continue;
			s += c;
		}
		s = s.replace("\u00A0", "");
		s = s.replace("\t", "");
		return s;
	}
	
	public static void printASCII(String src){
		for(int i=0; i<src.length()-2; i++){
//			char c = src.charAt(i);
//			int j = (int) c;
//			System.out.print(j + " ");
			System.out.print(src.charAt(i));
		}
	}
	
	public static String elimDot(String src){
		String ret = src.replaceAll("\\.", "");
		return ret;
	}
	
	
	public static boolean containsChars(String src){
		char[] chars = src.toCharArray();
//		String s = "";
		boolean flag = false;
		
		for(char c : chars){
			if(c == ' ') continue;
			if(Character.isAlphabetic(c)) {
				flag = true; 
				break;
			}
			if(Character.isDigit(c)) {
				flag = true; 
				break;
			}
		}
		
		return flag;
	}
	
	
	public static String getCharsOnly(String src){
//		
//		String s = text.replace(" ", "");

		char[] chars = src.toCharArray();
		String s = "";
		for(char c : chars){
			if(c == ' ') continue;
//			s += c;
			if(Character.isAlphabetic(c)) s += c;
			if(Character.isDigit(c)) s += c;
//			if(c == ',') s+= c;
		}

		return s;
	}
	
	
	public static String elimLastTwoChars(String str){
		int s = 2;
		String firstN = "";
		if(str != null && str.length() >= s){
			int n = str.length() -s;
			firstN = str.substring(0, Math.min(str.length(), n));
		}

		return firstN;
	}

	
	public static boolean romanIsParsed(String src){
		char[] pgNum = src.toCharArray();
		boolean flag = true;
		String validRomanStr = "ivx";
		
		for(char letter : pgNum){
			if(!validRomanStr.contains("" + letter)){
				flag = false;
			}
		}
		
		return flag;
	}
	
	public static boolean INTIsParsed(String src){
		try{
			int n = Integer.parseInt(src);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	
	
	public static int wordCount(String src){
		String[] charArr = src.split("\\s+");
		int count = charArr.length;
		System.out.println("Word count is = " + count);
		return count;
		
	}
	
	public static boolean allCaps(String src){
		src = getCharsOnly(src);
		boolean flag = true;
		char[] charArr = src.toCharArray();
//		System.out.print("original ");
//		for(char c : charArr){
//			System.out.print(c);
//		}
		for(char c : charArr){
			if(!Character.isUpperCase(c)){
//				System.out.println("not caps: " + c);
				flag = false;
				break;
			}
		}
		
		return flag;
	}
	
	public static boolean isAlpha(String name) {
	    return name.matches("[a-zA-Z]+");
	}
	
	public static boolean hasUTF8(String text){
	    Charset charset = Charset.forName("US-ASCII");
	    String checked=new String(text.getBytes(charset),charset);
	    return !checked.equals(text);

	}
	
	
}

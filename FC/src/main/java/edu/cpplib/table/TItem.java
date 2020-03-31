package edu.cpplib.table;

import java.util.Hashtable;

import edu.cpplib.StrFunction.StrFunc;

public class TItem {
	private String name;
	private String targetPgNum;
	private int realPgNum;
	private boolean itemExists;
	
//	private String type;
	/*
	 * Type:
	 * ToC, Table of Content 
	 * LoT, List of Table 
	 * LoF, List of Figure*/
	
	public TItem (String name, String targetPgNum, int realPgNum){
		this.name = name;
		this.targetPgNum = targetPgNum;
		this.realPgNum = realPgNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetPgNum() {
		return targetPgNum;
	}

	public void setTargetPgNum(String targetPgNum) {
		this.targetPgNum = targetPgNum;
	}

	public int getRealPgNum() {
		return realPgNum;
	}

	public void setRealPgNum(int realPgNum) {
		this.realPgNum = realPgNum;
	}

	public boolean isItemExists() {
		return itemExists;
	}

	public void setItemExists(boolean itemExists) {
		this.itemExists = itemExists;
	}

	public void print(){
		System.out.println(	"Item: " + this.getName() + 
							", target page num: " + this.getTargetPgNum() +
							"( " + isTargetPgNumNull() + " )" +
							", real page num: " + this.getRealPgNum());

	}
	
	public boolean isTargetPgNumNull(){
		String tmp = this.targetPgNum.trim();
		
		if(tmp.length() == 0) return true;
		else return false;
	}
	
	
	public boolean isPgNumRoman(){

		char[] pgNum = this.targetPgNum.toCharArray();
		boolean flag = true;
		String validRomanStr = "ivx";
		
		for(char letter : pgNum){
			if(!validRomanStr.contains("" + letter)){
				flag = false;
			}
		}
		
		return flag;
	}
	
	
	public int parseRomanPgNum(String roman){
		
		Hashtable<Character, Integer> table = new Hashtable<Character, Integer>();
	    table.put('i',1);
	    table.put('x',10);
	    table.put('v',5);
	    
	    int intNum = 0;
	    int prev = 0;
	    
	    for(int i = roman.length()-1; i>=0 ; i--){
	    	try{
				int temp = table.get(roman.charAt(i));
				if(temp < prev)
				    intNum -= temp;
				else
				    intNum += temp;
				prev = temp;
	    	}
			catch(Exception e){
			}
	    }
	    
	    return intNum;
	    
	}
	
	
	
	public void calculateRealPgNum(int startPgNum){
		/*
		 * target page num here could be:
		 * 1) Roman page num
		 * 2) int page num
		 * 3) NEXT
		 * */
		int targetPgNum_int = -1;

		if(StrFunc.containsChars(targetPgNum)){

			if(isPgNumRoman()){
				targetPgNum_int = this.parseRomanPgNum(this.targetPgNum);
			}
			else{
				String tmp = targetPgNum.trim();
//				System.out.println(tmp + tmp.length());
				if(tmp != null){
					try{
						targetPgNum_int = Integer.parseInt(tmp);
					}
					catch(Exception e){
						targetPgNum_int = -1;
					}
				}
			}
		}
		else{
			System.out.println("Page number cannot be parsed");
			this.setRealPgNum(-1);
		}
		

		if(targetPgNum_int != -1){
			/*
			 * means page num converted to integer successfully
			 * */
			if(isPgNumRoman()){
				this.setRealPgNum(targetPgNum_int);
			}
			else{
				int realPgNum = targetPgNum_int - 1 + startPgNum;
				this.setRealPgNum(realPgNum);
			}
		}
		else{
			this.setRealPgNum(-1);
		}
	}
	
	
	
}

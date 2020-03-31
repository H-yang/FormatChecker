package edu.cpplib.export;

public class TOCResult {
	
	private String itemName;
	private int actualPageNum;
	private String pdfPageNum;
	private boolean exist;
	
	public TOCResult(String in, int apn, String ppn, boolean e){
		this.itemName = in;
		this.actualPageNum = apn;
		this.pdfPageNum = ppn;
		this.exist = e;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getActualPageNum() {
		return actualPageNum;
	}
	public void setActualPageNum(int actualPageNum) {
		this.actualPageNum = actualPageNum;
	}
	public String getPdfPageNum() {
		return pdfPageNum;
	}
	public void setPdfPageNum(String pdfPageNum) {
		this.pdfPageNum = pdfPageNum;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	
	
}

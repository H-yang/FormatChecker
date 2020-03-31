package edu.cpplib.export;

public class ItemResult {
	private String checkItem;
	private String img;
	private String value;
	private boolean result;
	private String note;
	
	public ItemResult(String i, String v, boolean r, String n, String im){
		this.checkItem = i;
		this.value = v;
		this.result = r;
		this.note = n;
		this.img = im;
	}
	
	public String getCheckItem() {
		return checkItem;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

	

}

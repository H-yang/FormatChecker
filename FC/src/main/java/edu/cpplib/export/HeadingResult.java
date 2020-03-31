package edu.cpplib.export;

public class HeadingResult {
	private String heading;
	private int page;
	private boolean isBold, isCaps;
	
	public HeadingResult(String h, int p, boolean b, boolean c){
		this.heading = h;
		this.page = p;
		this.isBold = b;
		this.isCaps = c;
	}
	public String getHeading() {
		return heading;
	}
	public void setHeading(String heading) {
		this.heading = heading;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	public boolean isCaps() {
		return isCaps;
	}
	public void setCaps(boolean isCaps) {
		this.isCaps = isCaps;
	}
	
}

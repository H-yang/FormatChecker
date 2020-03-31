package edu.cpplib.headings;

import java.util.ArrayList;

public class HeadingNode extends Heading{

	private int nodeNum;
	private int outEdges[];
//	private ArrayList<Integer> inEdges;
	
	
	public HeadingNode(int pgNum, String text, int nodeNum) {
		super(pgNum, text);
		this.nodeNum = nodeNum;
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}
	
	public int[] getOutEdges() {
		return outEdges;
	}

	public void setOutEdges(int[] outEdges) {
		this.outEdges = outEdges;
	}


//	public ArrayList<Integer> getInEdges() {
//		return inEdges;
//	}
//
//	public void setInEdges(ArrayList<Integer> inEdges) {
//		this.inEdges = inEdges;
//	}



	@Override
	public void print(){
		System.out.println(this.pageType + ", " + this.nodeNum);
		System.out.println("Out edges:");
		try{
			for(int i : this.outEdges){
				System.out.print(i + " ");
			}
			System.out.println("");
		}
		catch (Exception e){
		}
	}
	

}

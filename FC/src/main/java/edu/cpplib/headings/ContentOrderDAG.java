package edu.cpplib.headings;

import java.util.ArrayList;

public class ContentOrderDAG {

	public ArrayList<HeadingNode> standardDAG(){
		
		ArrayList<HeadingNode> DAG = new ArrayList<HeadingNode>();
		
		int [] sigOutEdges = {2,3};
		int [] ackOutEdges = {3};
		int [] absOutEdges = {4};
		int [] tocOutEdges = {5,6,7};
		int [] lotOutEdges = {6};
		int [] lofOutEdges = {7};
		int [] refOutEdges = {8,9};
		int [] appOutEdges = {9};
		int [] endOutEdges = {0};

		
		HeadingNode sig = new HeadingNode(-1, "SIG", 1);
		sig.setOutEdges(sigOutEdges);
		DAG.add(sig);
		
		HeadingNode ack = new HeadingNode(-1, "ACK", 2);
		ack.setOutEdges(ackOutEdges);
		DAG.add(ack);
		
		HeadingNode abs = new HeadingNode(-1, "ABS", 3);
		abs.setOutEdges(absOutEdges);
		DAG.add(abs);
		
		HeadingNode toc = new HeadingNode(-1, "TOC", 4);
		toc.setOutEdges(tocOutEdges);
		DAG.add(toc);
		
		HeadingNode lot = new HeadingNode(-1, "LOT", 5);
		lot.setOutEdges(lotOutEdges);
		DAG.add(lot);
		
		HeadingNode lof = new HeadingNode(-1, "LOF", 6);
		lof.setOutEdges(lofOutEdges);
		DAG.add(lof);

		HeadingNode ref = new HeadingNode(-1, "REF", 7);
		ref.setOutEdges(refOutEdges);
		DAG.add(ref);
		
		HeadingNode app = new HeadingNode(-1, "APP", 8);
		app.setOutEdges(appOutEdges);
		DAG.add(app);
		
		HeadingNode end = new HeadingNode(-1, "END", 9);
		end.setOutEdges(endOutEdges);
		DAG.add(end);
		
		return DAG;
	}
	
	
	public ArrayList<HeadingNode> buildDAG(ArrayList<HeadingNode> nodes){
		
		ArrayList<HeadingNode> DAG = new ArrayList<HeadingNode>();
		
		int [] sigOutEdges = {2,3};
		int [] ackOutEdges = {3};
		int [] absOutEdges = {4};
		int [] tocOutEdges = {5,6,7};
		int [] lotOutEdges = {6};
		int [] lofOutEdges = {7};
		int [] refOutEdges = {8,9};
		int [] appOutEdges = {9};
		int [] endOutEdges = {0};

		for(HeadingNode node : nodes){
			if(node.getPageType().equals("SIG")){
				node.setOutEdges(sigOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("ACK")){
				node.setOutEdges(ackOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("ABS")){
				node.setOutEdges(absOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("TOC")){
				node.setOutEdges(tocOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("LOT")){
				node.setOutEdges(lotOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("LOF")){
				node.setOutEdges(lofOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("REF")){
				node.setOutEdges(refOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("APP")){
				node.setOutEdges(appOutEdges);
				DAG.add(node);
			}
			else if(node.getPageType().equals("END")){
				node.setOutEdges(endOutEdges);
				DAG.add(node);
			}
			else{
				
			}
			
		}
		
		
		return DAG;
	}
	
	
	
}

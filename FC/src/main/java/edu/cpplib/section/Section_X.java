package edu.cpplib.section;

import java.util.ArrayList;

import edu.cpplib.line.Line;

public class Section_X {
	private ArrayList<Line> lines;

	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
	
	public void print(){
		for(Line line : lines){
			System.out.println(line.getText());
		}
	}
}

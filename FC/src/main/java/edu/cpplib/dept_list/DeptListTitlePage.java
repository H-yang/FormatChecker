package edu.cpplib.dept_list;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DeptListTitlePage {
	
	public static ArrayList<String> readDeptListTXT() throws IOException{
		String fileName = "deptListTP.txt";
		ArrayList<String> departments = new ArrayList<String>();
		
		try {
			FileReader deptList = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(deptList);
			String line;
			while((line = bufferedReader.readLine()) != null){
//				System.out.println(line);
				String lineWithoutSpaces = line.replace(" ", "");
				departments.add(lineWithoutSpaces);
			}
			
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		for(String d : departments){
//			System.out.println(d);
//		}
		
		return departments;
	}
	
	
}

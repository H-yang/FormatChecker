package edu.cpplib.models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;

public class FileInfo {
	private String fileName, paperType, absPath, curYear, mainBodyPg, zipPath;

	public FileInfo(){}
	
	public FileInfo(String fName){
		this.fileName = fName;
		int year = Year.now().getValue();
		this.curYear = "" + year;
		this.mainBodyPg = "0";
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getPaperType() {
		return paperType;
	}

	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}

	public String getAbsPath() {
		return absPath;
	}
	
	public void setAbsPath() {
		this.absPath = "./upload-dir/" + this.fileName;
	}

	public String getCurYear() {
		return curYear;
	}

	public void setCurYear(String curYear) {
		this.curYear = curYear;
	}

	public String getMainBodyPg() {
		return mainBodyPg;
	}

	public void setMainBodyPg(String mainBodyPg) {
		this.mainBodyPg = mainBodyPg;
	}

	public String getZipPath() {
		return zipPath;
	}

	public void setZipPath(String zipPath) {
		this.zipPath = zipPath;
	}

	public void print(){
		System.out.println(	absPath + ", " + 
							fileName + ", " + 
							paperType + ", " + 
							curYear + ", " + 
							mainBodyPg);
	}

	public String getFileNameFromPath(){
		Path p = Paths.get(absPath);
		String fName1 = p.getFileName().toString();
		if (fName1.indexOf(".") > 0){
			fName1 = fName1.substring(0, fName1.lastIndexOf("."));
		}
		System.out.println(fName1);
		return fName1;
	}
	
}

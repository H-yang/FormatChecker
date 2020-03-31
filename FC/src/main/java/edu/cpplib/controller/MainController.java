package edu.cpplib.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.cpplib.export.FolderZiper;
import edu.cpplib.fc.FCDocument;
import edu.cpplib.models.FileInfo;

/* controller has info about:
 * what url access trigger it
 * what method to run when accessed
 * annotation indicating this class is controller
 * REST: REpresentational State Transfer
 */
@Controller
public class MainController {
	@RequestMapping(value = "/fc", method=RequestMethod.GET)
	@ResponseBody
    public String greeting() {
        return "Simply string"; //simple return a string and show
    }

	@RequestMapping("/test")
	public String test(){
		return "about";
	}
	
	
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	@RequestMapping("/manual")
	public String manual(){
		return "manual";
	}
	//@RequstMapping("/check") in file uploader
	@RequestMapping("/contact")
	public String contact(){
		return "contact";
	}
	@RequestMapping("/about")
	public String about(){
		return "about";
	}
	
	
	@RequestMapping(value = "/list", method=RequestMethod.GET)
	@ResponseBody
    public ArrayList<String> list() {
        ArrayList<String> ret = new ArrayList<String>();
        ret.add("aaa"); ret.add("bbb");
        return ret;
    }
	
    @GetMapping("/get_info")
    public String greetingForm(Model model) {
        model.addAttribute("fileInfo", new FileInfo(""));
        return "get_info";
    }

    @PostMapping("/show_info")
    public String greetingSubmit(@ModelAttribute FileInfo fileInfo) {
        return "show_info";
    }
    
	@RequestMapping(value = "/go_java")
	public String processForm(@ModelAttribute(value="fileInfo") FileInfo fileInfo) throws IOException {
		fileInfo.setAbsPath();
		fileInfo.print();

		try{
			FCDocument.run(	fileInfo.getAbsPath(), 
					fileInfo.getPaperType(), 
					fileInfo.getCurYear(), 
					fileInfo.getMainBodyPg());
			
			String zip = "./" + fileInfo.getFileNameFromPath() + "/" + fileInfo.getFileNameFromPath() + ".zip";
			fileInfo.setZipPath(zip);
			
			/*zip folder*/
			String srcFolder = "./" + fileInfo.getFileNameFromPath();
			String destZipFile = "./" + fileInfo.getFileNameFromPath() + ".zip";
			FolderZiper.zipFolder(srcFolder, destZipFile);
			
			//generate a zip
			//add absolute file path of zip to fileInfo obj
			//show the abs path as a href in go_java
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		//boolean status = fc.run(args);
		//if(status): return zip file path
		//else: return "something went wrong";
		return "go_java";
		
//		return "/download/" + fileInfo.getFileNameFromPath();
	}
	
	
    @RequestMapping(value = "/{file_name}/{file_name}", method = RequestMethod.GET)
    public void download(HttpServletResponse response, 
    		@PathVariable(value="file_name") String file_name) throws IOException {

    	System.out.println("filename" + file_name);
    	String zipPath = "./" + file_name + ".zip";
    	File f = new File(zipPath);
    	if(f.exists()){
            File file = new File(zipPath); //"./016/016.zip" >> ./fileName/fileName.zip
            InputStream is = new FileInputStream(file);
            
            // MIME type of the file
            response.setContentType("application/octet-stream");
            // Response header
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + file.getName() + "\"");
            // Read from the file and write into the response
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            os.close();
            is.close();
    	}

    	

    }
	

    
}

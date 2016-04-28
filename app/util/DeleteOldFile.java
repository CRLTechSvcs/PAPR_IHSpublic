package util;

import java.io.File;

public class DeleteOldFile {

	    public void DeleteFiles() {
	        File file = new File("D:/Documents/NetBeansProjects/printing~subversion/fileupload/web/resources/pdf/");
	        System.out.println("Called deleteFiles");
	        DeleteFiles(file);
	        File file2 = new File("D:/Documents/NetBeansProjects/printing~subversion/fileupload/Uploaded/");
	        //DeleteFilesNonPdf(file2);
	    }

	    
	    public void DeleteFiles(File file) {
	        System.out.println("Now will search folders and delete files,");
	        if (file.isDirectory()) {
	            System.out.println("Date Modified : " + file.lastModified());
	            for (File f : file.listFiles()) {
	                DeleteFiles(f);
	            }
	        } else {
	            file.delete();
	        }
	    }
	 
	    /*
	     * long diff = new Date().getTime() - file.lastModified();

		if (diff > x * 24 * 60 * 60 * 1000) {
    	file.delete();
		*/
	     
}

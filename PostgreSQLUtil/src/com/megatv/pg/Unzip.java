package com.megatv.pg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Unzip {

	
	 private static final int BUFFER_SIZE = 4096;
	    /**
	     * Extracts a zip file specified by the zipFilePath to a directory specified by
	     * destDirectory (will be created if does not exists)
	     * @param zipFilePath
	     * @param destDirectory
	     * @throws IOException
	     */
	    public void unzip(String zipFilePath, String destDirectory) throws IOException {
	        File destDir = new File(destDirectory);
	        if (!destDir.exists()) {
	            destDir.mkdirs();
	        }
	        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	        ZipEntry entry = zipIn.getNextEntry();
	        // iterates over entries in the zip file
	        while (entry != null) {
	            String filePath = destDirectory + File.separator + entry.getName();
	            if (entry.isDirectory()) {
	                // if the entry is a directory, make the directory
	                File dir = new File(filePath);
	                dir.mkdirs();
	            } else {
	            	// if the entry is a file, extracts it
	                File folderPath = new File(new File(filePath).getParent());
	    	        if (!folderPath.exists()) {
	    	        	folderPath.mkdirs();
	    	        }

	            	extractFile(zipIn, filePath);
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
	    }
	    /**
	     * Extracts a zip entry (file entry)
	     * @param zipIn
	     * @param filePath
	     * @throws IOException
	     */
	    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
	        byte[] bytesIn = new byte[BUFFER_SIZE];
	        int read = 0;
	        while ((read = zipIn.read(bytesIn)) != -1) {
	            bos.write(bytesIn, 0, read);
	        }
	        bos.close();
	    }
	
}
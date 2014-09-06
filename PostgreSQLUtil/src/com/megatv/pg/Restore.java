package com.megatv.pg;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class Restore {

	Properties props;
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}	
	

	
	public void restore(){

		Service service = new Service();
		service.setProps(this.getProps());
		

		
		//Check if service is running adn stop it....
		if(service.isServiceRunning()){
			service.stopService();
		}
		
		//Keep a backup of the existing data folder
		archiveDataFolder();
		
		//Clear the data folder;
		clearDataFolder(null);
		
		//unzip backup file to data folder
		unzipBackupToDataFolder();
		
		//Rename the recovery.done to recovery.conf
		renameRecoveryFile();

		//Start the PG service
		service.startService();
		
		
	}
	
	
	
	public void archiveDataFolder(){
		String src = props.getProperty("datafolder");
		String dest = props.getProperty("backupfolder") +"\\"+"before_restore_data.zip";
		Zip zip = new Zip();
		zip.setExcludeFolder("dummyfolderthatnotexists") ;// do not exclude anything 
		zip.zipFiles(src, dest);
		
	}
	
	public void clearDataFolder(File folder){
		if (folder == null){
			String dataFolder = props.getProperty("datafolder");
			folder = new File(dataFolder);
		}
		File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	            	clearDataFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    
	    //Do not delete data directory and pg_xlog directory
	    if(!folder.getName().equals(props.getProperty("logdirname")) 
	    		&& !folder.getName().equals(props.getProperty("datadirname"))){  
	    	folder.delete();
	    }
	}
	
	public void unzipBackupToDataFolder(){
		Unzip unzip = new Unzip();
		String datafolder = props.getProperty("datafolder");
		String backupfolder = props.getProperty("backupfolder");
		
		String destinationFolder = new File(datafolder).getParent().toString();
		
		//Unzip the backup into data folder;
		try {
			
			String zipFile = backupfolder + "backup_base_f54c2f6c-93ed-4e2c-a032-f7dc5fc49211.zip";
			unzip.unzip(zipFile, destinationFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	
	public void renameRecoveryFile(){
		String datafolder = props.getProperty("datafolder");
		String recoverydone = datafolder + "recovery.done";
		String recoveryconf = datafolder + "recovery.conf";
		
		File recoveryDoneFile  = new File(recoverydone);
		File recoveryConfFile  = new File(recoveryconf);
		
		recoveryDoneFile.renameTo(recoveryConfFile);
		
	}
	
}
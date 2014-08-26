package com.megatv.pg;

import java.io.File;
import java.util.Properties;

public class Restore {

	Properties props;
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}	
	

	
	public boolean restore(){
		
		
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
		
		
		return true;
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
			String dataFolder = props.getProperty("testdatafolder");
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
		
	}
	
	
	public void renameRecoveryFile(){
		
	}
	
}
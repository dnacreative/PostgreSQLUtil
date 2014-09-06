package com.megatv.pg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Backup {
	Properties props;
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public boolean createBackup(boolean fullbackup){
	
		boolean completed = true;
	 	Connection con = null;
	    Statement st = null;
	    ResultSet rs = null;
	
	
	    String url = getProps().getProperty("url");
	    String user = getProps().getProperty("dbuser");
	    String password = getProps().getProperty("dbpassword");
	
	    Logger lgr = Logger.getLogger(Backup.class.getName());
	
	    try {
	        con = DriverManager.getConnection(url, user, password);
	        lgr.log(Level.INFO, "Connected to PostgreSQL");
	
	        st = con.createStatement();
	        rs = st.executeQuery("SELECT VERSION()");
	        
	        if (rs.next()) {
	            System.out.println(rs.getString(1));
	            lgr.log(Level.INFO, "PostgreSQL version " + rs.getString(1) );
	        }
	

	        /*
	        String uuid = UUID.randomUUID().toString();
            String backupLabel = "backup_" + uuid;
            String backupFile = "backup_base_" + uuid +".zip";
            */

            
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd-hh-mm-ss");
            
			String datetime = ft.format(dNow); 
            String backupLabel = "backup_" + datetime;
            String backupFile = "backup_base_" + datetime +".zip";
            
                        
            
            //Start the backup
            lgr.log(Level.INFO, "Starting backup with label " + backupLabel );
            rs = st.executeQuery("SELECT pg_start_backup('"+ backupLabel +"');");
            if (rs.next()) {
                System.out.println(rs.getString(1));
                lgr.log(Level.INFO, "Backup with label " + backupLabel + " started " );
            }
            
            
            if(fullbackup){
	            //Create the base backup for data folder
	            lgr.log(Level.INFO, "Starting base backup (" + backupFile + ")");
	            
	            /* Does not include empty directories	            
				ZipUtils appZip = new ZipUtils();
	            appZip.generateFileList(new File(ZipUtils.SOURCE_FOLDER));
	            appZip.zipIt(ZipUtils.OUTPUT_ZIP_FILE);
	            */
	            
	            /* Includes empty directories*/
	            Zip zip = new Zip();
	            
	            String dataFolder = getProps().getProperty("datafolder");
	            String backupFolder = getProps().getProperty("backupfolder");
	            String excludeFolder = getProps().getProperty("excludefolder");
	            zip.setExcludeFolder(excludeFolder);
	            
	            
	            zip.zipFiles(dataFolder, backupFolder + backupFile);
	            zip = null;
	            
	            lgr.log(Level.INFO, "Base backup completed (" + backupFile + ")");
            }

            
            //End of the backup
            lgr.log(Level.INFO, "Completing backup with label " + backupLabel );
            rs = st.executeQuery("SELECT pg_stop_backup();");
            if (rs.next()) {
                System.out.println(rs.getString(1));
                lgr.log(Level.INFO, "Backup with label " + backupLabel + " completed " );
            }
	
	        
	
	    } catch (SQLException ex) {
	        lgr = Logger.getLogger(Util.class.getName());
	        lgr.log(Level.FATAL, ex.getMessage(), ex);
	        completed = false;
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (st != null) {
	                st.close();
	            }
	            if (con != null) {
	                con.close();
	            }
	
	        } catch (SQLException ex) {
	            lgr = Logger.getLogger(Util.class.getName());
	            lgr.log(Level.WARN, ex.getMessage(), ex);
	            completed = false;
	        }
	    }
	    return completed;
	}
}
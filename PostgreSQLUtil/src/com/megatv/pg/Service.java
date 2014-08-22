package com.megatv.pg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Service {

	protected static final String  SERVICE_NAME = "postgresql-x64-9.3"; 

	Properties props;
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}	
	

	
	/**
	 *  REF http://stackoverflow.com/questions/9075098/start-windows-service-from-java
	 	String[] script = {"cmd.exe", "/c", "sc", "query", APP_SERVICE_NAME, "|", "find", "/C", "\"RUNNING\""};//to check whether service is running or not
		String[] script = {"cmd.exe", "/c", "sc", "start", SERVICE_NAME};//to start service
		String[] script = {"cmd.exe", "/c", "sc", "stop", SERVICE_NAME};//to stop service
	 * 
	 */
	
	
	
	
	public void startService(){
		String[] script = {"cmd.exe", "/c", "sc", "start", SERVICE_NAME};//to start service
		try {
			Process p = Runtime.getRuntime().exec(script);
			p = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void stopService(){
		String[] script = {"cmd.exe", "/c", "sc", "stop", SERVICE_NAME};//to stop service
		try {
			Process p = Runtime.getRuntime().exec(script);
			p = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public boolean isServiceRunning(){
		String[] script = {"cmd.exe", "/c", "sc", "query", SERVICE_NAME, "|", "find", "/C", "\"RUNNING\""};
		boolean isRunning = false;
		
		try {
			Process p = Runtime.getRuntime().exec(script);
				p.waitFor();
			BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
			String line=reader.readLine(); 
			while(line!=null){ 
				if(line.equals("1")){
					isRunning = true;	
				}
				line=reader.readLine();
			}
			p = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return isRunning;
	}
	
}
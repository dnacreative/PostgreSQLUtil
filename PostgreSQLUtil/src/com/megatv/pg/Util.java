package com.megatv.pg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/*Main class for project*/
public class Util {

	private static enum Command {
		HELP, BACKUP, RESTORE
	}

	public static void main1(String[] args) {
		Service sc = new Service();
		if (sc.isServiceRunning()) {
			sc.stopService();
		} else {
			sc.startService();
		}
		System.exit(0);
	}

	public static void main(String[] args) {

		Command command = interpretCommandArguments(args);

		Logger lgr = Logger.getLogger(Util.class.getName());
		lgr.log(Level.INFO, "Initializing..");
		Properties prop = new Properties();
		InputStream input = null;

		try {
			// load a properties file
			input = new FileInputStream("properties.conf");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		lgr.log(Level.INFO, "Initializing tasks service...");
		Service service = new Service();
		service.setProps(prop);

		lgr.log(Level.INFO, "Initializing backup service...");
		Backup backup = new Backup();
		backup.setProps(prop);

		lgr.log(Level.INFO, "Initializing restore service...");
		Restore restore = new Restore();
		restore.setProps(prop);

		lgr.log(Level.INFO, "Initialization completed...");

		switch (command) {
			case HELP:
				showHelp();
				break;
			case BACKUP:
				backup.createBackup();
				break;
			case RESTORE:
				break;
			default:
				showHelp();
				break;
		}

		// rsr.clearDataFolder(null);
	}

	private static Command interpretCommandArguments(String[] args) {
		if (args.length == 0) {
			return Command.HELP;
		} else {
			String command = args[0];
			switch (command) {
			case "-help":
				return Command.HELP;
			case "-backup":
				return Command.BACKUP;
			case "-restore":
				return Command.RESTORE;
			default:
				return Command.HELP;
			}
		}
	}
	
	private static void showHelp() {
		System.out.println("Usage: java -jar postgresqlutil.jar [options]");
		System.out.println("  options:");
		System.out.println("	-backup			generate online backup.");
		System.out.println("	-restore		restore backup.");
		System.out.println("	-help			show usage help");
	}

}
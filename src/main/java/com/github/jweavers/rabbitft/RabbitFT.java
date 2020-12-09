package com.github.jweavers.rabbitft;

import java.io.File;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.github.jweavers.rabbitft.client.FileTransfer.MODE;

/**
 * @author ravi
 * 
 *         Contract definition for Rabbit File Transfer. All main API would be
 *         defined here.
 * 
 */
public abstract class RabbitFT {

	private static final String LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n";
	private Level _fileLogging = Level.INFO;
	private Level _consoleLogging = Level.INFO;

	/**
	 * To upload list of file(s) through specific channel. It will automatically
	 * determine possible threads required to process all files.
	 * 
	 * @param files List of files, which needs to be uploaded
	 */
	public abstract void upload(List<File> files);

	/**
	 * 
	 * To upload list of file(s) through specific channel with specified number of
	 * parallalism. But, this also depend number files and available cores to
	 * process this task.
	 * 
	 * @param files   - list of files, which needs to be uploaded
	 * @param threads - number of parallel thread(s)
	 */
	public abstract void upload(List<File> files, int threads);
	
	/**
	 * To upload list of file(s) through specific channel. It will automatically
	 * determine possible threads required to process all files.
	 * 
	 * @param files List of files, which needs to be uploaded
	 * @param mode transfer mode APPEND, OVERWITE
	 */
	public abstract void upload(List<File> files, MODE mode);

	/**
	 * 
	 * To upload list of file(s) through specific channel with specified number of
	 * parallalism. But, this also depend number files and available cores to
	 * process this task.
	 * 
	 * @param files   - list of files, which needs to be uploaded
	 * @param threads - number of parallel thread(s)
	 * @param mode transfer mode APPEND, OVERWITE
	 */
	public abstract void upload(List<File> files, int threads, MODE mode);

	protected void initLogging() {
		ConsoleAppender console = new ConsoleAppender(); // create appender
		// configure the appender

		console.setLayout(new PatternLayout(LOG_PATTERN));
		console.setThreshold(_consoleLogging);
		console.activateOptions();
		// add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(console);

		RollingFileAppender rollingAppender = new RollingFileAppender();
		rollingAppender.setName("FileLogger");
		rollingAppender.setFile("rabbitft.log");
		rollingAppender.setMaxFileSize("5MB");
		rollingAppender.setMaxBackupIndex(10);
		rollingAppender.setLayout(new PatternLayout(LOG_PATTERN));
		rollingAppender.setThreshold(_fileLogging);
		rollingAppender.setAppend(true);
		rollingAppender.activateOptions();
		Logger.getRootLogger().addAppender(rollingAppender);
	}

	/**
	 * To enable debug mode on console
	 * 
	 * @param flag - Set true or false, to enable to disable
	 */
	public void setConsoleDebugMode(boolean flag) {
		if (flag)
			_consoleLogging = Level.DEBUG;
		else
			_consoleLogging = Level.INFO;
	}

	/**
	 * To enable debug mode for log file
	 * 
	 * @param flag - Set true or false, to enable to disable
	 */
	public void setFileDebugMode(boolean flag) {
		if (flag)
			_fileLogging = Level.DEBUG;
		else
			_fileLogging = Level.INFO;
	}

}

package com.rabbitft;

import java.io.File;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

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

	public abstract void upload(List<File> files);

	public abstract void upload(List<File> files, int threads);

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

	public void setConsoleDebugMode(boolean flag) {
		if (flag)
			_consoleLogging = Level.DEBUG;
		else
			_consoleLogging = Level.INFO;
	}

	public void setFileDebugMode(boolean flag) {
		if (flag)
			_fileLogging = Level.DEBUG;
		else
			_fileLogging = Level.INFO;
	}

}

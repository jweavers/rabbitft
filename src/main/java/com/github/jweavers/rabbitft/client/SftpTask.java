package com.github.jweavers.rabbitft.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * @author ravi
 *
 *         Create SFTP task
 */
class SftpTask implements Task {

	private final File _file;
	private final String _sftpPath;
	private final BlockingQueue<SftpSession> _sftpConnectionPool;
	private final static Logger _logger = Logger.getLogger(SftpTask.class);

	/**
	 * @param _channel
	 * @param _file
	 * @param _path
	 */
	public SftpTask(BlockingQueue<SftpSession> _sftpPool, File _file, String _path) {
		super();
		this._file = _file;
		this._sftpPath = _path;
		this._sftpConnectionPool = _sftpPool;
	}

	/**
	 * Responsible for uploading a file through SFTP channel
	 */
	@Override
	public void uploadFile() {
		SftpSession _session = null;
		try (InputStream _inputStream = new FileInputStream(_file)) {
			while (_session == null) {
				_session = _sftpConnectionPool.take();
			}
			ChannelSftp _channelSftp = _session.sftpChannel();
			_channelSftp.cd(_sftpPath);
			_logger.debug("Current directory :" + _channelSftp.pwd());
			_logger.debug("Session connected :" + _channelSftp.isConnected());
			_channelSftp.put(_inputStream, _file.getName());
			_logger.debug("Uploading file to " + _file.getName());
		} catch (SftpException | IOException | InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}finally {
			_sftpConnectionPool.add(_session);
		}
	}

}

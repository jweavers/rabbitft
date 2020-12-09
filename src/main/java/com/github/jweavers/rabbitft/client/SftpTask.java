package com.github.jweavers.rabbitft.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.github.jweavers.rabbitft.client.FileTransfer.MODE;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

/**
 * @author ravi
 *
 *         Create SFTP task
 */
class SftpTask implements Task {

	private final File _file;
	private final MODE _transferMode;
	private final String _sftpPath;
	private final BlockingQueue<SftpSession> _sftpConnectionPool;
	private final static Logger _logger = Logger.getLogger(SftpTask.class);

	/**
	 * @param _channel
	 * @param _file
	 * @param _path
	 */
	public SftpTask(MODE _transferMode, BlockingQueue<SftpSession> _sftpPool, File _file, String _path) {
		super();
		this._file = _file;
		this._sftpPath = _path;
		this._sftpConnectionPool = _sftpPool;
		this._transferMode = _transferMode;
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
			if (_sftpPath != null && !_sftpPath.isEmpty())
				_channelSftp.cd(_sftpPath);
			_logger.debug("Current directory :" + _channelSftp.pwd());
			_logger.debug("Session connected :" + _channelSftp.isConnected());
			_logger.debug("Uploading file " + _file.getName() + " using  session " + _channelSftp.getId());

			_channelSftp.put(_inputStream, _file.getName(), getSftpMode(_transferMode));
			_logger.info("File uploaded : " + _file.getName());
		} catch (SftpException | IOException | InterruptedException e) {
			_logger.error(e.getMessage(), e);
		} finally {
			_sftpConnectionPool.add(_session);
		}
	}

	/**
	 * @param transferMode - file transfer mode
	 * @return SFTP transfer mode.
	 */
	private int getSftpMode(MODE transferMode) {
		switch (transferMode) {
		case OVERWRITE:
			return ChannelSftp.OVERWRITE;
		case APPEND:
			return ChannelSftp.APPEND;
		}
		return ChannelSftp.OVERWRITE;
	}

}

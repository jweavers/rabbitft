package com.rabbitft.client;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.rabbitft.Constants;
import com.rabbitft.RabbitFT;
import com.rabbitft.server.SftpContext;

/**
 * @author ravi
 * 
 *         Create SFTP client for transfer file through SFTP channel
 */
class SftpClient extends RabbitFT {

	private final SftpContext _context;
	private ExecutorFacade _executorFacade;
	private final BlockingQueue<SftpSession> _sftpConnectionPool;
	private final static Logger _logger = Logger.getLogger(SftpClient.class);

	public SftpClient(SftpContext ctx) {
		this._context = ctx;
		this._sftpConnectionPool = new LinkedBlockingQueue<>();
	}

	/**
	 * connect SFTP client
	 */
	private void connect() {
		try {
			JSch jSch = new JSch();
			_logger.debug("Connecting SFTP using " + _context);
			if (_context.getSftpKeyPath() != null) {
				jSch.addIdentity(_context.getSftpKeyPath());
				_logger.debug("Key added from :" + _context.getSftpKeyPath());
			}
			Session _sftpSession = jSch.getSession(_context.getSftpUser(), _context.getSftpHost(),
					_context.getSftpPort());
			if (_context.getSftpKeyPath() == null && _context.getSftpPassword() != null) {
				_sftpSession.setPassword(_context.getSftpPassword());
				_logger.debug("Ready for SFTP session using password");
			}
			Properties config = new Properties();
			config.put(Constants.STRICTHOSTKEYCHECKING, "no");
			_sftpSession.setConfig(config);
			_sftpSession.connect();
			_logger.debug("Successfully created SFTP session");
			Channel _channel = _sftpSession.openChannel(Constants.SFTP);
			_channel.connect();
			_logger.debug("Successfully connected to SFTP server using current session");
			_sftpConnectionPool.add(new SftpSession(_sftpSession, _channel));
		} catch (Exception e) {
			_logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 */
	@Override
	public void upload(List<File> files, int threads) {
		initLogging();
		init(threads);
		try {
			for (File file : files) {
				_executorFacade.submit(new SftpTask(_sftpConnectionPool, file, _context.getSftpFolder()));
			}
			_executorFacade.close();
		} catch (Exception e) {
			_logger.error(e.getMessage(), e);
		} finally {
			_logger.debug("Disconnecting SFTP session");
			while (!_sftpConnectionPool.isEmpty()) {
				_sftpConnectionPool.poll().disconnect();
			}
		}
	}

	/**
	 *
	 */
	@Override
	public void upload(List<File> files) {
		upload(files, files.size());
	}

	/**
	 * @param threads
	 */
	private void init(int threads) {
		_executorFacade = new ExecutorFacade(threads);
		int _sftpPoolSize = _executorFacade.getThreadPoolSize();
		while (_sftpPoolSize-- > 0) {
			connect();
		}
	}
}

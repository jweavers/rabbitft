package com.github.jweavers.rabbitft.client;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

class SftpSession {

	private ChannelSftp _channelSftp;
	private final Session _sftpSession;
	private final Channel _channel;
	private final static Logger _logger = Logger.getLogger(SftpSession.class);

	public SftpSession(Session _sftpSession, Channel _channel) {
		super();
		this._sftpSession = _sftpSession;
		this._channel = _channel;
	}

	public ChannelSftp sftpChannel() {
		if (_channelSftp == null)
			_channelSftp = (ChannelSftp) _channel;
		return _channelSftp;
	}

	/**
	 * disconnect SFTP client
	 */
	public void disconnect() {
		if (_channelSftp != null) {
			_channelSftp.disconnect();
			_channelSftp.exit();
		}
		if (_channel != null) {
			_channel.disconnect();
		}
		if (_sftpSession != null) {
			_sftpSession.disconnect();
		}
		_logger.debug("Successfully disconnected session with SFTP server");
	}
}

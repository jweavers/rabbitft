package com.rabbitft.server;

/**
 * @author ravi
 *
 */
public class SftpContext implements ConnectionContext {

	private final String _sftpHost;
	private final int _sftpPort;
	private final String _sftpUser;
	private final String _sftpFolder;
	private byte[] _sftpPassword;
	private String _sftpKeyPath;

	/**
	 * @param hostName
	 * @param portNo
	 * @param userName
	 * @param sftpFolder
	 */
	public SftpContext(String hostName, int portNo, String userName, String sftpFolder, byte[] password) {
		this._sftpHost = hostName;
		this._sftpPort = portNo;
		this._sftpUser = userName;
		this._sftpFolder = sftpFolder;
		this._sftpPassword = password;
	}

	public SftpContext(String hostName, int portNo, String userName, String sftpFolder, String keyPath) {
		this._sftpHost = hostName;
		this._sftpPort = portNo;
		this._sftpUser = userName;
		this._sftpFolder = sftpFolder;
		this._sftpKeyPath = keyPath;
	}

	public String getSftpHost() {
		return _sftpHost;
	}

	public int getSftpPort() {
		return _sftpPort;
	}

	public String getSftpUser() {
		return _sftpUser;
	}

	public byte[] getSftpPassword() {
		return _sftpPassword;
	}

	public String getSftpFolder() {
		return _sftpFolder;
	}

	public String getSftpKeyPath() {
		return _sftpKeyPath;
	}

	@Override
	public String toString() {
		return "SftpContext [_sftpHost=" + _sftpHost + ", _sftpPort=" + _sftpPort + ", _sftpUser=" + _sftpUser
				+ ", _sftpFolder=" + _sftpFolder + "]";
	}

}

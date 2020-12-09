package com.github.jweavers.rabbitft.server;

/**
 * @author ravi
 * 
 *         To create SFTP Connection context.
 */
public class SftpContext implements ConnectionContext {

	private final String _sftpHost;
	private final int _sftpPort;
	private final String _sftpUser;
	private final String _sftpFolder;
	private byte[] _sftpPassword;
	private String _sftpKeyPath;

	/**
	 * Create SFTP connection context using username and password.
	 * 
	 * @param hostName   - SFTP host name.
	 * @param portNo     - SFTP port number.
	 * @param userName   - SFTP user name.
	 * @param sftpFolder - SFTP folder path. If require navigation to specific SFTP
	 *                   folder. It can be blank for current directory.
	 * @param password   - SFTP password
	 */
	public SftpContext(String hostName, int portNo, String userName, String sftpFolder, byte[] password) {
		this._sftpHost = hostName;
		this._sftpPort = portNo;
		this._sftpUser = userName;
		this._sftpFolder = sftpFolder;
		this._sftpPassword = password;
	}

	/**
	 * 
	 * Create SFTP connection context using username and key.
	 * 
	 * @param hostName   - SFTP host name.
	 * @param portNo     - SFTP port number.
	 * @param userName   - SFTP user name.
	 * @param sftpFolder - SFTP folder path. If require navigation to specific SFTP
	 *                   folder. It can be blank for current directory.
	 * @param keyPath    - SFTP key path
	 */
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

package com.rabbitft.server;

import com.rabbitft.Constants;

/**
 * @author ravi
 *
 */
public final class SharepointContext implements ConnectionContext{

	private final String ACCESS_CONTROL_URL = Constants.HTTPS + "://accounts.accesscontrol.windows.net/";
	private final String ACCESS_TOKEN_VER = "/tokens/OAuth/2";
	private final String SHAREPOINT_RESOURCE = "00000003-0000-0ff1-ce00-000000000000/";
	private final String _client_Id;
	private final String _bearer_realm;
	private final String _client_secret;
	private final String _server_domain;
	private final String _folder_path;
	private boolean _overwriteAllowed;
	private String _authUrl;

	/**
	 * @param client
	 * @param secret
	 * @param server_loc
	 * @param upload_folder
	 * @param bearer
	 */
	public SharepointContext(String client, String secret, String server_loc, String upload_folder, String bearer) {
		this._client_secret = secret;
		this._bearer_realm = bearer;
		this._server_domain = server_loc;
		this._client_Id = client;
		this._folder_path = upload_folder;
		this._overwriteAllowed = true;
	}

	/**
	 * @param authUrl
	 * @param client
	 * @param secret
	 * @param server_loc
	 * @param upload_folder
	 * @param bearer
	 */
	public SharepointContext(String authUrl, String client, String secret, String server_loc, String upload_folder,
			String bearer) {
		this(client, secret, server_loc, upload_folder, bearer);
		this._authUrl = authUrl;
	}

	public String getClientId() {
		return _client_Id + "@" + _bearer_realm;
	}

	public String getAuthUrl() {
		if (_authUrl == null || _authUrl.isEmpty()) {
			_authUrl = ACCESS_CONTROL_URL + _bearer_realm + ACCESS_TOKEN_VER;
		}
		return _authUrl;
	}

	public String getResource() {
		return SHAREPOINT_RESOURCE + _server_domain.split("/")[0] + "@" + _bearer_realm;
	}

	public String getClientSecret() {
		return _client_secret;
	}

	public String getServerDomain() {
		return _server_domain;
	}

	public String getFolderPath() {
		return _folder_path;
	}

	public void setOverwriteAllowed(boolean overwrite) {
		this._overwriteAllowed = overwrite;
	}

	public boolean isOverwriteAllowed() {
		return _overwriteAllowed;
	}
}

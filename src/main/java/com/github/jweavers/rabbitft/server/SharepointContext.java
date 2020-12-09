package com.github.jweavers.rabbitft.server;

import com.github.jweavers.rabbitft.Constants;

/**
 * @author ravi
 *
 * To create Sharepoint connection context
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
	private String _authUrl;

	/**
	 * @param client - Sharepoint client id
	 * @param secret - Secret of sharepoint client
	 * @param server_loc - Sharepoint domain i.e <i>mysharepoint.com</i>
	 * @param upload_folder - Folder path
	 * @param bearer - Sharepoint client tenant id.
	 */
	public SharepointContext(String client, String secret, String server_loc, String upload_folder, String bearer) {
		this._client_secret = secret;
		this._bearer_realm = bearer;
		this._server_domain = server_loc;
		this._client_Id = client;
		this._folder_path = upload_folder;
	}

	/**
	 * @param authUrl - Url to retrieve access token for the client.
	 * @param client - Sharepoint client id
	 * @param secret - Secret of sharepoint client
	 * @param server_loc - Sharepoint domain i.e <i>mysharepoint.com</i>
	 * @param upload_folder - Folder path
	 * @param bearer - Sharepoint client tenant id.
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
}

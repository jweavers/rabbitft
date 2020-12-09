package com.github.jweavers.rabbitft.client;

import java.io.File;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.github.jweavers.rabbitft.Constants;
import com.github.jweavers.rabbitft.client.FileTransfer.MODE;

/**
 * @author ravi
 * 
 *         Defines sharepoint channel task
 */
class SharepointTask implements Task {

	private final MODE _transferMode;
	private final File _file;
	private final String _accessToken;
	private final String _sharepointPath;
	private final static Logger _logger = Logger.getLogger(SharepointTask.class);

	/**
	 * @param transferMode    - file transfer mode
	 * @param _file           - File to be uploaded
	 * @param _accessToken    - Sharepoint access token
	 * @param _sharepointPath - Sharepoint url path
	 */
	public SharepointTask(MODE transferMode, File _file, String _accessToken, String _sharepointPath) {
		super();
		this._transferMode = transferMode;
		this._file = _file;
		this._accessToken = _accessToken;
		this._sharepointPath = _sharepointPath;
	}

	/**
	 * Responsible for uploading a file
	 */
	@Override
	public void uploadFile() {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = null;
		try {
			String sharepointUrl = String.format(_sharepointPath, _file.getName(), (MODE.OVERWRITE == _transferMode));
			post = new HttpPost(sharepointUrl);
			post.setHeader(Constants.AUTHORIZATION, String.format(Constants.BEARER, _accessToken));

			post.setEntity(new FileEntity(_file));

			CloseableHttpResponse response = httpclient.execute(post);
			try {
				_logger.info(response.getStatusLine().getStatusCode() + " " + _file.getName());
			} finally {
				if (response != null)
					response.close();
			}

		} catch (Exception e) {
			_logger.error("Unable to process file : " + _file.getName(), e);
		} finally {
			if (post != null)
				post.completed();
		}
	}

}

package com.github.jweavers.rabbitft.client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.github.jweavers.rabbitft.Constants;

/**
 * @author ravi
 * Defines sharepoint channel task
 */
class SharepointTask implements Task {

	private final boolean _isOverwriteAllowed;
	private final File _file;
	private final String _accessToken;
	private final String _sharepointPath;
	private final static Logger _logger = Logger.getLogger(SharepointTask.class);

	/**
	 * @param overwriteAllowed
	 * @param _file
	 * @param _accessToken
	 * @param _sharepointPath
	 */
	public SharepointTask(boolean overwriteAllowed, File _file, String _accessToken, String _sharepointPath) {
		super();
		this._isOverwriteAllowed = overwriteAllowed;
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
		byte[] b;
		HttpPost post = null;
		try {
			String sharepointUrl = String.format(_sharepointPath, _file.getName(), _isOverwriteAllowed);
			post = new HttpPost(sharepointUrl);
			post.setHeader(Constants.AUTHORIZATION, String.format("Bearer %s", _accessToken));

			b = Files.readAllBytes(Paths.get(_file.getPath()));
			post.setEntity(new ByteArrayEntity(b));
			HttpResponse response = httpclient.execute(post);
			_logger.debug(response.getStatusLine().getStatusCode() + " " + _file.getName());
		} catch (Exception e) {
			_logger.error("Unable to process file : " + _file.getName(),e);
		} finally {
			if (post != null)
				post.completed();
		}
	}

}

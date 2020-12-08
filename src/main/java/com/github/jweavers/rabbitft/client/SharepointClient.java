package com.github.jweavers.rabbitft.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.github.jweavers.rabbitft.Constants;
import com.github.jweavers.rabbitft.RabbitFT;
import com.github.jweavers.rabbitft.server.SharepointContext;
import com.google.gson.Gson;

/**
 * @author ravi
 * 
 * Create Sharepoint client for transfer file through Sharepoint channel
 */
class SharepointClient extends RabbitFT {

	private final SharepointContext _context;
	private String _accessToken;
	private ExecutorFacade _executorFacade;
	private String _uploadService;
	private StringBuilder _sharepointPath;

	private final String ADD_FILE_SERVICE = "/Files/Add(url='%s',overwrite=%s)";
	private final String RELATIVE_PATH_API = "/_api/web/GetFolderByServerRelativeUrl('";
	private final String EXIST_API = "/Exists";
	private final static Logger _logger = Logger.getLogger(SharepointClient.class);

	public SharepointClient(SharepointContext ctx) {
		this._context = ctx;
		this._sharepointPath = new StringBuilder();
	}

	/**
	 * Connecting to sharepoint and create access token for transaction.
	 */
	private void connect() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(_context.getAuthUrl());
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS));
		params.add(new BasicNameValuePair(Constants.RESOURCE, _context.getResource()));
		params.add(new BasicNameValuePair(Constants.CLIENT_ID, _context.getClientId()));
		params.add(new BasicNameValuePair(Constants.CLIENT_SECRET, _context.getClientSecret()));
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = httpclient.execute(post);
			_logger.debug(
					"Requesting for token and sharepoint responded as " + response.getStatusLine().getStatusCode());
			String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			Gson gson = new Gson();
			SharepointResponse resp = gson.fromJson(json, SharepointResponse.class);
			_accessToken = resp.getAccess_token();
			_logger.debug("Access token for sharepoint successfully created.");
			_logger.info("Successfully connected to Sharepoint server");
		} catch (ParseException | IOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * upload files through sharepoint channel
	 */
	@Override
	public void upload(List<File> files, int threads) {
		initLogging();
		init(threads);
		connect();
		if (validatePath()) {
			for (File file : files) {
				_executorFacade
						.submit(new SharepointTask(_context.isOverwriteAllowed(), file, _accessToken, _uploadService));
			}
			_logger.info("Upload request has been completed successfully.");
		} else {
			_logger.info("Unable to locate " + _context.getFolderPath() + " folder on sharepoint server.");
		}
		_executorFacade.close();
	}

	/**
	 * @param threads
	 */
	private void init(int threads) {
		_executorFacade = new ExecutorFacade(threads);
		_sharepointPath.append(Constants.HTTPS).append("://").append(_context.getServerDomain())
				.append(RELATIVE_PATH_API).append(_context.getFolderPath()).append("')");
		_uploadService = _sharepointPath.toString() + ADD_FILE_SERVICE;
		_logger.debug(_sharepointPath.toString());
	}

	/**
	 *
	 */
	@Override
	public void upload(List<File> files) {
		upload(files, files.size());
	}

	private boolean validatePath() {
		HttpGet _httpGet = null;
		boolean _isAccessible = false;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			String sharepointUrl = _sharepointPath.append(EXIST_API).toString();

			_httpGet = new HttpGet(sharepointUrl);
			_httpGet.setHeader(Constants.AUTHORIZATION, String.format(Constants.BEARER, _accessToken));

			HttpResponse response = httpclient.execute(_httpGet);
			_isAccessible = (HttpStatus.SC_OK == response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			_logger.error(e);
		} finally {
			if (_httpGet != null)
				_httpGet.completed();
		}
		return _isAccessible;
	}

}

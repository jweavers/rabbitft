package com.rabbitft.client;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.rabbitft.Constants;
import com.rabbitft.RabbitFT;
import com.rabbitft.server.SharepointContext;

/**
 * @author ravi
 * 
 *         Create Sharepoint client for transfer file through Sharepoint channel
 */
class SharepointClient extends RabbitFT {

	private final SharepointContext _context;
	private String _accessToken;
	private StringBuilder _sharepointPath = new StringBuilder();
	private ExecutorFacade _executorFacade;
	private final static Logger _logger = Logger.getLogger(SharepointClient.class);

	public SharepointClient(SharepointContext ctx) {
		this._context = ctx;
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
			_logger.debug("Requesting for token and sharepoint responded as "+ response.getStatusLine().getStatusCode());
			String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
			Gson gson = new Gson();
			SharepointResponse resp = gson.fromJson(json, SharepointResponse.class);
			_accessToken = resp.getAccess_token();
			_logger.debug("Access token for sharepoint successfully created.");
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
		String path = _sharepointPath.toString();
		for (File file : files) {
			_executorFacade.submit(new SharepointTask(_context.isOverwriteAllowed(), file, _accessToken, path));
		}
		_executorFacade.close();
	}

	/**
	 * @param threads
	 */
	private void init(int threads) {
		_executorFacade = new ExecutorFacade(threads);
		_sharepointPath.append(Constants.HTTPS).append("://").append(_context.getServerDomain())
				.append("/_api/web/GetFolderByServerRelativeUrl('Documents/").append(_context.getFolderPath())
				.append("')/Files/Add(url='%s',overwrite=%s)");
		_logger.debug(_sharepointPath.toString());
	}

	/**
	 *
	 */
	@Override
	public void upload(List<File> files) {
		upload(files, files.size());
	}

}

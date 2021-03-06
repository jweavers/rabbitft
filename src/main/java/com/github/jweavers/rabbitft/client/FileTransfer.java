package com.github.jweavers.rabbitft.client;

import com.github.jweavers.rabbitft.RabbitFT;
import com.github.jweavers.rabbitft.server.ConnectionContext;
import com.github.jweavers.rabbitft.server.SftpContext;
import com.github.jweavers.rabbitft.server.SharepointContext;

/**
 * @author ravi 
 * This class is responsible for creating new instance for specific 
 * transfer mode.
 */
public final class FileTransfer {

	public static enum MODE {
		OVERWRITE, APPEND
	};

	private FileTransfer() {
	}

	/**
	 * It is responsible for creating new channel instance.
	 * 
	 * @param ctx, connection instance for specific channel.
	 * @return it returns new instance for specific channel instance.
	 */
	public static RabbitFT newChannelInstance(ConnectionContext ctx) {
		if (ctx instanceof SftpContext)
			return new SftpClient((SftpContext) ctx);
		if (ctx instanceof SharepointContext)
			return new SharepointClient((SharepointContext) ctx);

		throw new RuntimeException("Invalid Transfer Mode.");
	}
}

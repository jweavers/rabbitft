package com.rabbitft.client;

import com.rabbitft.RabbitFT;
import com.rabbitft.server.ConnectionContext;
import com.rabbitft.server.SftpContext;
import com.rabbitft.server.SharepointContext;

/**
 * @author ravi This class is responsible for creating new instance for specific
 *         transfer mode.
 */
public final class FileTransfer {

	private FileTransfer() {
	}

	/**
	 * It is responsible for creating new channel instance.
	 * 
	 * @param ctx, connection instance for specific channel.
	 * @param mode define transfer mode
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

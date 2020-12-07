package com.github.jweavers.rabbitft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.jweavers.rabbitft.RabbitFT;
import com.github.jweavers.rabbitft.client.FileTransfer;
import com.github.jweavers.rabbitft.server.ConnectionContext;
import com.github.jweavers.rabbitft.server.SftpContext;
import com.github.jweavers.rabbitft.server.SharepointContext;

public class Test {

	public static void main(String[] args) throws IOException {
		// https://<sharepoint_domain>/_layouts/15/user.aspx
		String server_loc = "<sharepoint_domain>";
		String upload_folder = "POC";
		String client = "b3b9e9f9-055d-4e";
		String secret = "2ADMRQnjuHWjsyo6geyGFit";
		String bearer = "b3f4f7c2-72ce-4192-";
		ConnectionContext ctx = new SftpContext("192.168.0.101", 22, "ravi", "/Users/ravi/Desktop/sftp2/",
				"xxxxyz".getBytes());
				
		RabbitFT postmaster = FileTransfer.newChannelInstance(ctx);
		postmaster.setConsoleDebugMode(true);
		List<File> files = new ArrayList<>();
		Files.newDirectoryStream(Paths.get("C:\\Users\\sharmar\\Desktop\\Sharepoint"), file->file.toFile().getName().endsWith("txt")).forEach(s -> files.add(s.toFile()));
		postmaster.upload(files,2);
	}
}

package com.rabbitft;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.rabbitft.client.FileTransfer;
import com.rabbitft.server.ConnectionContext;
import com.rabbitft.server.SftpContext;
import com.rabbitft.server.SharepointContext;

public class Test {

	public static void main(String[] args) throws IOException {
		// https://amadeusworkplace-my.sharepoint.com/personal/ravikant_sharma2_amadeus_com/_layouts/15/user.aspx
		String server_loc = "amadeusworkplace-my.sharepoint.com/personal/ravikant_sharma2_amadeus_com";
		String upload_folder = "POC";
		String client = "b3b9e9f9-055d-4e4e-960b-7c8df5385e3d";
		String secret = "2ADMRQnjuHWjsyo6geyGFitHaqqBrVYpBMZxxK9E8wI=";
		String bearer = "b3f4f7c2-72ce-4192-aba4-d6c7719b5766";
		ConnectionContext ctx = new SftpContext("192.168.0.101", 22, "ravi", "/Users/ravi/Desktop/sftp2/",
				"Ravisharma@90".getBytes());
				
		RabbitFT postmaster = FileTransfer.newChannelInstance(ctx);
		postmaster.setConsoleDebugMode(true);
		List<File> files = new ArrayList<>();
		Files.newDirectoryStream(Paths.get("C:\\Users\\sharmar\\Desktop\\Sharepoint"), file->file.toFile().getName().endsWith("txt")).forEach(s -> files.add(s.toFile()));
		postmaster.upload(files,2);
	}
}

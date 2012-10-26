package com.mobilityspot.batchpasswordchanger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Ssh2Client {
	private JSch shell;
	private Session session;
	private Channel channel;
	private static OutputStream out;
	private static InputStream in;

	public void connect(String username, String password, String host, int port)
			throws JSchException, IOException, InterruptedException {
		shell = new JSch();
		session = shell.getSession(username, host, port);
		session.setPassword(password);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect();

		channel = session.openChannel("shell");
		channel.setInputStream(null);
		channel.setOutputStream(null);

		in = channel.getInputStream();
		out = channel.getOutputStream();
		((ChannelShell) channel).setPtyType("vt102");
		channel.connect();
	}
	
	

	public String send(String command) throws IOException, InterruptedException {
		byte[] tmp = new byte[1024];

		//out.write((command + ";echo \"z4a3ce4f3317Z\"").getBytes());
		out.write((command).getBytes());
		out.write(("\n").getBytes());
		out.flush();

		String result = "";
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;

				result = result + (new String(tmp, 0, i));
			}
			//if (result.indexOf("z4a3ce4f3317Z") != -1) {
			if (result.indexOf("\n") != -1) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (Exception ee) {
			}
		}
		return result;
	}

	public boolean isConnected() {
		return (channel != null && channel.isConnected());
	}

	public void disconnect() {
		if (isConnected()) {
			channel.disconnect();
			session.disconnect();
		}
	}
}
package services;

import com.jcraft.jsch.*;
import devicesInformation.SwitchInformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class connectionHelper {
    SwitchInformation switchInfo;
    Session session;
    InputStream in;
    ChannelExec  channel;


    public connectionHelper() {
        switchInfo = new SwitchInformation();
    }

    public void initConnection() throws JSchException, InterruptedException {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(switchInfo.getUser(), switchInfo.getHost(), 22);
        session.setPassword(switchInfo.getPassword());
        session.setConfig(config);
        session.connect();
        session.setTimeout(1000);
        System.out.println("Connected");
        
          System.out.println("openChannel");
        channel = (ChannelExec) session.openChannel("exec");
        System.out.println("setCommand");
		channel.setCommand("show version");

		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
		channel.setOutputStream(responseStream);
		channel.connect();
		
		  while (channel.isConnected()) {
	            Thread.sleep(100);
	        }
		String responseString = new String(responseStream.toByteArray());
		System.out.println("output is:\n");
		System.out.println(responseString);
		

    }

    public void setUpChannel(String command) throws JSchException, IOException, InterruptedException {
    	
		channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);

		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
		channel.setOutputStream(responseStream);
		channel.connect();
		String responseString = new String(responseStream.toByteArray());
		System.out.println("output is:\n");
		System.out.println(responseString);
    }

    public void readResponse(String command) throws JSchException, IOException, InterruptedException {
        initConnection();
        setUpChannel(command);
        try {
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        }

    }


}

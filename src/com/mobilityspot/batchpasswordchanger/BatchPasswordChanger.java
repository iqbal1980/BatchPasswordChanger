package com.mobilityspot.batchpasswordchanger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.jcraft.jsch.JSchException;


public class BatchPasswordChanger {
  
   
  	

  public static ArrayList<String> hostsFromFileToArray(String fileName) {
	  ArrayList<String> hostsList = new ArrayList<String>();

	  System.out.println(fileName);
	  try{
		  FileInputStream fstream = new FileInputStream(fileName.trim());
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  
		  while ((strLine = br.readLine()) != null)   {
			  System.out.println (strLine);
			  if(strLine != "" && strLine != null) {
				  hostsList.add(strLine);
			  }
		  }

		  in.close();
		    } catch (Exception e){
		  System.err.println("Error: " + e.getMessage());
		  }
		  
	return hostsList;
  }
  
  
  private static void changePassword(String user,String oldPassword, String newPassword, String host) throws JSchException, IOException, InterruptedException {
	  	Ssh2Client client = new Ssh2Client();
		client.connect(user,oldPassword, host, 22);
		
		System.out.println(client.send("passwd"));
		System.out.println(client.send(oldPassword));
		System.out.println(client.send(newPassword));
		System.out.println(client.send(newPassword));  
  }
  
  public static void main(String[] arg){
	  
	  
	if(arg.length < 4) {
		System.out.println("Usage:");  
		System.out.println("BatchPasswordChanger UserName hostsfile.txt oldPassword newPassword"); 
		System.exit(0);
	}
	  
	System.out.println("User =>"+arg[0]);
	System.out.println("Hosts file =>"+arg[1]);
	System.out.println("Old password =>"+arg[2]);
	System.out.println("New password =>"+arg[3]);
	
	String userName = arg[0];
	String hostsFile = arg[1];
	String oldPassword = arg[2];
	String newPassword = arg[3];
	
	ArrayList<String> hostsList = hostsFromFileToArray(hostsFile);
	
	for(String host : hostsList) {
	    
		try {
			changePassword(userName,oldPassword, newPassword, host);
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	    
	    
	}
  }


}
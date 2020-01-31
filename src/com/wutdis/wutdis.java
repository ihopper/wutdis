/*
 * Usage: wutdis [target] [starting port] [ending port]
 * 
 */
package com.wutdis;

import java.io.*;
import java.net.*;
import java.net.Socket;

public class wutdis {

	// Initialize variables
	private static int startPort 	= 0; //Begin the scan at port 0, unless otherwise specified.
	private static int endPort		= 100; //End the scan at port 10,000, unless otherwise specified. 
	private static String target	= ""; //The target server.
	
	public wutdis(String target, int startPort, int endPort) throws IOException {
		
		int currentPort = startPort;
		
		// Update the user
		System.out.println("*** Performing scan on " + target + " ***\n");

		// Run the scan loop
		// TODO Add multi-thread support
		for(int count = currentPort; count <= endPort; count++) {		
			
			// Connect to the server
			try(Socket socket = new Socket(target, currentPort)) {
		
				// Initialize input stream to receive the server response.
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
		
				// Initialize variable for reading data
				String line = null;
		
				// Read the response and display it for the user.
				while((line = reader.readLine()) != null && (line = reader.readLine()) != "") {
					System.out.println(target + ":" + currentPort + " (open) -> " + line);
				}
				
				// Close the socket and stream
				reader.close();
				socket.close();
				
			// Throw errors	
			} catch (UnknownHostException ex) {
				 
	            System.out.println("Server not found: " + ex.getMessage());
	 
	        } catch (IOException ex) {
	        	// In the event of an IOException, the port is unreachable (i.e. closed).
	            System.out.println(target + ":" + currentPort + " (closed) ");
	        }// End try/catch
			
			// Advance the port counter
			currentPort++;
			
		}// End for loop
		// Notify the user that the scan is complete
		System.out.println("\n*** Scan completed ***");
		
	}// End constructor

	static void showHelp() {
		// Print the help message
		System.out.println("Usage: \n"
				+ "\twutdis [target] [starting port] [ending port]\n\n"
				+ "Scans a range of port numbers on the target host and reports open ports/services.\n"
				+ "If no port numbers are given the scan range defaults to 0 to 100.\n");
	}
	
	public static void main(String[] args) throws IOException {
		// Check for input and parse the arguments, if they exist.
		if(args.length != 0) {
			target	= args[0];
		} else {
			showHelp();
			System.exit(0);
		}
		
		// Get the starting port
		if(args.length >= 2) {
			try{
				startPort = Integer.parseInt(args[1]);
			} 
			catch(NumberFormatException ex) {
				System.out.println("ERROR: Starting port must be an integer.\n");
				showHelp();
				System.exit(0);
			};
		}
		
		// Get the ending port
		if(args.length >= 3) {
			try {
				endPort = Integer.parseInt(args[2]);
			} 
			catch(NumberFormatException ex) {
				System.out.println("ERROR: Ending port must be an integer.\n");
				showHelp();
				System.exit(0);
			};
		}
		
		// Call the constructor
		wutdis scanner = new wutdis(target, startPort, endPort);

	}// End main

}// End class

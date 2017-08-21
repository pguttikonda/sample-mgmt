package com.kenss.utilities;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveClient {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "SkuMatch/1.0";

    /** Directory to store user credentials for this application. */
    //private static final java.io.File DATA_STORE_DIR = new java.io.File(
       		//System.getProperty("user.home"), ".credentials/drive-java-quickstart");
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/skumatch");
    		
    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this GoogleDriveClient.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-GoogleDriveClient
     */
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_READONLY);
        //Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        //InputStream in =
        	//	GoogleDriveClient.class.getResourceAsStream("client_secret.json");
        InputStream in =
        		GoogleDriveClient.class.getResourceAsStream(Constants.GDRIVE_CREDS_FILE);
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize(Constants.GDRIVE_USER_INFO);
        //System.out.println(
          //      "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public boolean downloadFileFromDrive(String fileName, String fileLocation) throws IOException {
        // Build a new authorized API client service.
        Drive service = getDriveService();

        if (fileName == null || fileName.isEmpty()) {
        	System.out.println("No master file name provided");
        	return false;
        }
        if (fileLocation == null || fileLocation.isEmpty())
        	fileLocation = "";
        
        //Check to see if the file exists locally. If it does, don't download again.
        //Path filePath = Paths.get (fileLocation + fileName + "_drive1");
        Path filePath = Paths.get (Constants.MASTER_PRODUCT_FILE_NAME);
        if (Files.exists(filePath)) {
        	return true;
        }
        
        //Accessing Google Drive and getting a list of files worked. Now download the master product list as a CSV file and save it to the right location,
        FileList xxx = service.files().list()
    									.setQ("name contains '" + Constants.MASTER_PRODUCT_FILE_NAME + "'")
    									.execute();
        if (xxx.isEmpty()) {
        	System.out.println("Cannot find the master product list in Drive?!?!?!?!");
        	return false;
        }
        else {
        	for (File tempFile : xxx.getFiles()) {
        		if (tempFile.getName().equalsIgnoreCase(Constants.MASTER_PRODUCT_FILE_NAME)){
        			//OutputStream outputStream = new ByteArrayOutputStream();
        			OutputStream outputStream = new FileOutputStream(fileLocation + Constants.MASTER_PRODUCT_FILE_NAME);
        	        service.files().export(tempFile.getId(), "text/csv")
        	                .executeMediaAndDownloadTo(outputStream);
        	        outputStream.close();
        	        return true;
        		}
        		
        	}
        }
        return false;
    }

}
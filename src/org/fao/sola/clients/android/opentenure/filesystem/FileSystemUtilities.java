/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.fao.sola.clients.android.opentenure.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.fao.sola.clients.android.opentenure.OpenTenureApplication;

import android.content.Context;
import android.os.Environment;

public class FileSystemUtilities {


	private static String _CLAIMS_FOLDER = "claims";
	private static String _CLAIMANTS_FOLDER = "claimants";
	private static String _CLAIM_PREFIX = "claim_";
	private static String _CLAIMANT_PREFIX = "claimant_";
	private static String _CLAIM_METADATA = "metadata";
	private static String _ATTACHMENT_FOLDER = "attachment_folder";
	private static String _OPEN_TENURE_FOLDER = "Open Tenure";
	



	/**
	 * 
	 * Create the folder that contains all the cliams under the application file system
	 * 
	 * */

	public static boolean createClaimsFolder(){


		if(isExternalStorageWritable()){		

			try {
				Context context = OpenTenureApplication.getContext();	
				File appFolder = context.getExternalFilesDir(null);				
				new File(appFolder, _CLAIMS_FOLDER).mkdir();				
				File claimsFolder = new File(appFolder.getAbsoluteFile()+File.separator+_CLAIMS_FOLDER);

				if(claimsFolder.exists() && claimsFolder.isDirectory())
					return true;
				else
					return false;

			} catch (Exception e) {					
				return false;
			}			
		}
		else {
			return false;			
		}		

	}



	/**
	 * 
	 * Create the OpenTenure folder under the the public file system 
	 * Here will be exported the compressed claim
	 * 
	 * **/

	public static boolean createOpenTenureFolder(){


		if(isExternalStorageWritable()){

			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);			
			File ot = new File(path.getParentFile(),_OPEN_TENURE_FOLDER);		

			if (ot.mkdir() && ot.isDirectory()){

				System.out.println("Created Open Tenure Folder");
				return true;
			}		
			return false;					
		}

		else return false;

	}



	public static boolean createClaimantsFolder(){


		if(isExternalStorageWritable()){		

			try {
				Context context = OpenTenureApplication.getContext();	
				File appFolder = context.getExternalFilesDir(null);				
				new File(appFolder, _CLAIMANTS_FOLDER).mkdir();				
				File claimantsFolder = new File(appFolder.getAbsoluteFile()+File.separator+_CLAIMANTS_FOLDER);

				if(claimantsFolder.exists() && claimantsFolder.isDirectory())
					return true;
				else
					return false;

			} catch (Exception e) {					
				return false;
			}			
		}
		else {
			return false;			
		}		

	}



	public static boolean createClaimFileSystem(String claimID){

		File claimFolder = null;
		File claimsFolder = null;		

		try {

			claimsFolder = getClaimsFolder();

			new File(claimsFolder,_CLAIM_PREFIX+claimID).mkdir();

			claimFolder = new File(claimsFolder,_CLAIM_PREFIX+claimID);

			new File(claimFolder, _CLAIM_METADATA).mkdir();		
			new File(claimFolder, _ATTACHMENT_FOLDER).mkdir();

			System.out.println("Claim File System created " + claimFolder.getAbsolutePath());

		} catch (Exception e) {
			System.out.println("Error creating the file system of the claim!!!");
			return false ;
		}

		return( new File(claimFolder,_CLAIM_METADATA).exists() && 

				new File(claimFolder,_ATTACHMENT_FOLDER).exists()
				);		
	}


	public static boolean createClaimantFolder(String personId){

		try {
			new File(getClaimantsFolder(),_CLAIMANT_PREFIX+personId).mkdir();

		} catch (Exception e) {
			System.out.println("Error creating the file system of the claim!!!");
			return false ;
		}

		return new File(getClaimantsFolder(),_CLAIMANT_PREFIX+personId).exists();		
	}

	public static void delete(File file)
			throws IOException{

		if(file.isDirectory()){

			//directory is empty, then delete it
			if(file.list().length==0){

				file.delete();
				System.out.println("Directory is deleted : " 
						+ file.getAbsolutePath());

			}else{

				//list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					//construct the file structure
					File fileDelete = new File(file, temp);

					//recursive delete
					delete(fileDelete);
				}

				//check the directory again, if empty then delete it
				if(file.list().length==0){
					file.delete();
					System.out.println("Directory is deleted : " 
							+ file.getAbsolutePath());
				}
			}

		}else{
			//if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}
	
	
	public static void deleteCompressedClaim(String claimID)
			throws IOException{
		
		File oldZip = new File(FileSystemUtilities.getOpentenureFolder().getAbsolutePath()+File.separator+"Claim_"+claimID+".zip");		
		delete(oldZip);		
	}

	public static boolean removeClaimantFolder(String personId){

		try {
			delete(new File(getClaimantsFolder(),_CLAIMANT_PREFIX+personId));		
		} catch (Exception e) {
			return false;
		}
		return true;		
	}


	public static File getClaimsFolder(){

		Context context = OpenTenureApplication.getContext();	
		File appFolder = context.getExternalFilesDir(null);				
		return new File(appFolder, _CLAIMS_FOLDER);

	}

	public static File getClaimantsFolder(){

		Context context = OpenTenureApplication.getContext();	
		File appFolder = context.getExternalFilesDir(null);				
		return new File(appFolder, _CLAIMANTS_FOLDER);

	}


	public static File getClaimFolder(String claimID){		
		return new File(getClaimsFolder(), _CLAIM_PREFIX + claimID);
	}


	public static File getClaimantFolder(String personId){		
		return new File(getClaimantsFolder(), _CLAIMANT_PREFIX + personId);
	}

	public static File getMetadataFolder(String claimID){		
		return new File(getClaimFolder(claimID), _CLAIM_METADATA);
	}	

	public static File getAttachmentFolder(String claimID){		
		return new File(getClaimFolder(claimID), _ATTACHMENT_FOLDER);
	}
	
	public static File getOpentenureFolder(){		
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);		
		return new File(path.getParentFile(),_OPEN_TENURE_FOLDER);
	}



	public static File copyFileInAttachFolder(String claimID,File source){

		File dest = null;

		try {

			dest = new File(getAttachmentFolder(claimID),source.getName());		
			dest.createNewFile();

			System.out.println(dest.getAbsolutePath());
			byte[] buffer = new byte[1024];

			FileInputStream reader = new FileInputStream(source);
			FileOutputStream writer = new FileOutputStream(dest);

			BufferedInputStream br= new BufferedInputStream(reader);


			while( (br.read(buffer) ) != -1 ) {
				writer.write(buffer); 
			}

			reader.close(); 
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dest;
	}



	protected static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}




}
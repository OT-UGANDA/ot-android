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
package org.fao.sola.clients.android.opentenure.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.fao.sola.clients.android.opentenure.OpenTenureApplication;

public class Attachment {
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	Database db = OpenTenureApplication.getInstance().getDatabase();
	
	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Attachment(){
		this.attachmentId = UUID.randomUUID().toString();
	}
	
	@Override
	public String toString() {
		return "Attachment [attachmentId=" + attachmentId
				+ ", uploaded=" + uploaded.toString() + ", claimId=" + claimId
				+ ", description=" + description + ", fileName="
				+ fileName + ", fileType=" + fileType + ", mimeType="
				+ mimeType + ", MD5Sum=" + MD5Sum + ", path=" + path + "]";
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMD5Sum() {
		return MD5Sum;
	}
	public void setMD5Sum(String mD5Sum) {
		MD5Sum = mD5Sum;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public static int createAttachment(Attachment attachment) {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase().getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("INSERT INTO ATTACHMENT(ATTACHMENT_ID, UPLOADED, CLAIM_ID, DESCRIPTION, FILE_NAME, FILE_TYPE, MIME_TYPE, MD5SUM, PATH) VALUES (?,?,?,?,?,?,?,?)");
			statement.setString(1, attachment.getAttachmentId());
			statement.setBoolean(2, attachment.getUploaded());
			statement.setString(3, attachment.getClaimId());
			statement.setString(4, attachment.getDescription());
			statement.setString(5, attachment.getFileName());
			statement.setString(6, attachment.getFileType());
			statement.setString(7, attachment.getMimeType());
			statement.setString(8, attachment.getMD5Sum());
			statement.setString(9, attachment.getPath());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int create() {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = db.getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("INSERT INTO ATTACHMENT(ATTACHMENT_ID, UPLOADED, CLAIM_ID, DESCRIPTION, FILE_NAME, FILE_TYPE, MIME_TYPE, MD5SUM, PATH) VALUES (?,?,?,?,?,?,?,?,?)");
			statement.setString(1, getAttachmentId());
			statement.setBoolean(2, getUploaded());
			statement.setString(3, getClaimId());
			statement.setString(4, getDescription());
			statement.setString(5, getFileName());
			statement.setString(6, getFileType());
			statement.setString(7, getMimeType());
			statement.setString(8, getMD5Sum());
			statement.setString(9, getPath());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public static int updateAttachment(Attachment attachment) {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase().getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("UPDATE ATTACHMENT SET UPLOADED=?, CLAIM_ID=?, DESCRIPTION=?, FILE_NAME=?, FILE_TYPE=?, MIME_TYPE=?, MD5SUM=?, PATH=? WHERE ATTACHMENT_ID=?");
			statement.setBoolean(1, attachment.getUploaded());
			statement.setString(2, attachment.getClaimId());
			statement.setString(3, attachment.getDescription());
			statement.setString(4, attachment.getFileName());
			statement.setString(5, attachment.getFileType());
			statement.setString(6, attachment.getMimeType());
			statement.setString(7, attachment.getMD5Sum());
			statement.setString(8, attachment.getPath());
			statement.setString(9, attachment.getAttachmentId());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int update() {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = db.getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("UPDATE ATTACHMENT SET UPLOADED=?, CLAIM_ID=?, DESCRIPTION=?, FILE_NAME=?, FILE_TYPE=?, MIME_TYPE=?, MD5SUM=?, PATH=? WHERE ATTACHMENT_ID=?");
			statement.setBoolean(1, getUploaded());
			statement.setString(2, getClaimId());
			statement.setString(3, getDescription());
			statement.setString(4, getFileName());
			statement.setString(5, getFileType());
			statement.setString(6, getMimeType());
			statement.setString(7, getMD5Sum());
			statement.setString(8, getPath());
			statement.setString(9, getAttachmentId());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public static int deleteAttachment(Attachment attachment) {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase().getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("DELETE ATTACHMENT WHERE ATTACHMENT_ID=?");
			statement.setString(1, attachment.getAttachmentId());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public int delete() {

		int result = 0;
		Connection localConnection = null;

		try {

			localConnection = db.getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("DELETE ATTACHMENT WHERE ATTACHMENT_ID=?");
			statement.setString(1, getAttachmentId());
			
			result = statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public static Attachment getAttachment(String attachmentId) {

		Attachment attachment = null;

		Connection localConnection = null;
		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase().getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("SELECT UPLOADED, CLAIM_ID, DESCRIPTION, FILE_NAME, FILE_TYPE, MIME_TYPE, MD5SUM, PATH FROM ATTACHMENT DOC WHERE DOC.ATTACHMENT_ID=?");
			statement.setString(1, attachmentId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				attachment = new Attachment();
				attachment.setAttachmentId(attachmentId);
				attachment.setUploaded(rs.getBoolean(1));
				attachment.setClaimId(rs.getString(2));
				attachment.setDescription(rs.getString(3));
				attachment.setFileName(rs.getString(4));
				attachment.setFileType(rs.getString(5));
				attachment.setMimeType(rs.getString(6));
				attachment.setMD5Sum(rs.getString(7));
				attachment.setPath(rs.getString(8));
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return attachment;
	}

	public static List<Attachment> getAttachments(String claimId) {

		List<Attachment> attachments = new ArrayList<Attachment>();

		Connection localConnection = null;
		try {

			localConnection = OpenTenureApplication.getInstance().getDatabase().getConnection();
			PreparedStatement statement = localConnection
					.prepareStatement("SELECT ATTACHMENT_ID, UPLOADED, DESCRIPTION, FILE_NAME, FILE_TYPE, MIME_TYPE, MD5SUM, PATH FROM ATTACHMENT ATT WHERE ATT.CLAIM_ID=?");
			statement.setString(1, claimId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Attachment attachment = new Attachment();
				attachment.setAttachmentId(rs.getString(1));
				attachment.setUploaded(rs.getBoolean(2));
				attachment.setClaimId(claimId);
				attachment.setDescription(rs.getString(3));
				attachment.setFileName(rs.getString(4));
				attachment.setFileType(rs.getString(5));
				attachment.setMimeType(rs.getString(6));
				attachment.setMD5Sum(rs.getString(7));
				attachment.setPath(rs.getString(8));
				attachments.add(attachment);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			if (localConnection != null) {
				try {
					localConnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return attachments;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Boolean getUploaded() {
		return uploaded;
	}

	public void setUploaded(Boolean uploaded) {
		this.uploaded = uploaded;
	}

	String attachmentId;
	Boolean uploaded = Boolean.valueOf(false);
	String claimId;
	String description;
	String fileName;
	String fileType;
	String mimeType;
	String MD5Sum;
	String path;

}
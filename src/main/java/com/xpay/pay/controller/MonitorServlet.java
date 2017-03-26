package com.xpay.pay.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.ByteProcessor;
import com.google.common.io.Files;

public class MonitorServlet extends HttpServlet {

	private static final long serialVersionUID = 6483608024962524202L;

	private static final String[] filePaths = { "/etc/nginx/nginx.conf", "/etc/nginx/sites-enabled/xpay",
			"/usr/local/xpay/pay/config.properties" };
	private static final String logFileNameParameter = "logFileName";
	private static final String extracLogKeyParameter = "key";
	public static final String defaultKey = "Eq4FX5XDdnP9eg9netT0QHCUrrgLU4T8";
	private static final String needZipParamter = "zip";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if(defaultKey.equals(req.getParameter(extracLogKeyParameter))){
			handleExtractLogRequest(req, resp);
			return;
		}

		PrintWriter pw = resp.getWriter();

		for (String filePath : filePaths) {
			File file = new File(filePath);
			printFile(pw, file);
		}

		pw.flush();
		IOUtils.closeQuietly(pw);
	}

	/**
	 * printFile: TODO
	 * 
	 * @param pw
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void printFile(PrintWriter pw, File file) throws FileNotFoundException, IOException {

		if (file.exists()) {
			if (file.isFile()) {
				FileInputStream fis = new FileInputStream(file);

				pw.println();
				pw.println();
				pw.println();
				pw.println();
				pw.println();
				String contentStart = "==========" + file.getAbsolutePath();
				pw.println(StringUtils.rightPad(contentStart, 100, '='));
				String string = IOUtils.toString(fis);
				pw.println(string);
				IOUtils.closeQuietly(fis);
				String contentEnd = "=========== end of file";
				pw.println(StringUtils.rightPad(contentEnd, 100, '='));
				pw.println();
			}
		} else {

			pw.println();
			pw.println();
			pw.println();
			pw.println();
			pw.println();
			pw.println(StringUtils.rightPad("", 100, 'X'));
			String content = "XXXXXXXXXX     " + file.getAbsolutePath() + " NOT FOUND     ";
			pw.println(StringUtils.rightPad(content, 100, 'X'));
			pw.println(StringUtils.rightPad("", 100, 'X'));
			pw.println();
			pw.println();

		}

	}
	
	public boolean validateFile(String fileName){
		return fileName.matches("^p.*\\.log.*$") || fileName.matches("^a.*\\.log.*$");
	}
	
	public void handleExtractLogRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String logFileName = req.getParameter(logFileNameParameter);
		if(StringUtils.isNotBlank(logFileName) && validateFile(logFileName)){
			String serverDir = System.getProperty("catalina.home");
			if(StringUtils.isNotBlank(serverDir)){
				boolean needZip = Boolean.parseBoolean(req.getParameter(needZipParamter));
				File file = new File(String.join(File.separator, serverDir, "logs", logFileName));
				if(!needZip){
					PrintWriter pw = resp.getWriter();
					printFile(pw, file);
				}else{
					resp.setContentType("application/zip");
					resp.setHeader("Content-Disposition", "attachment; filename=\"DATA.ZIP\"");
					final ZipOutputStream outputStream = new ZipOutputStream(resp.getOutputStream());
					outputStream.putNextEntry(new ZipEntry(logFileName));
					
					Files.readBytes(file, new ByteProcessor<Void>() {
						@Override
						public boolean processBytes(byte[] buf, int off, int len) throws IOException {
							outputStream.write(buf, off, len);
							return true;
						}
						@Override
						public Void getResult() {
							return null;
						}
					});
					outputStream.close();
				}
			
			} else {
				PrintWriter pw = resp.getWriter();
				pw.print("cannot find {catalina.home} system property");
				pw.flush();
				IOUtils.closeQuietly(pw);
			}
		}
	}
}

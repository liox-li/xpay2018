package com.xpay.pay.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class HeartbeatServlet extends HttpServlet {
	
	private static final long serialVersionUID = 6161570329223911380L;

	/**
	 * Handle incoming GETs
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String json = "{\"status\":\"ok\",\"totalMemory\":\"${totalMemory}\",\"freeMemory\":\"${freeMemory}\",\"availableProcessors\":\"${availableProcessors}\"}";
    	
    	Runtime rt = Runtime.getRuntime();
    	int totalMemory = (int)(rt.totalMemory() >> 20);    	
    	int freeMemory = (int)(rt.freeMemory() >> 20);
    	int availableProcessors = rt.availableProcessors();
    	json = StringUtils.replace(json,"${totalMemory}", totalMemory + "M");
    	json = StringUtils.replace(json,"${freeMemory}", freeMemory + "M");
    	json = StringUtils.replace(json,"${availableProcessors}", Integer.toString(availableProcessors));
    	response.getWriter().println(json);
    	response.flushBuffer();
    	
    	String stat = request.getParameter("stat");
    	String fileName = request.getParameter("file");
    	if(stat != null && stat.equalsIgnoreCase("true"))
    	{
    		response.getWriter().println();
    		response.getWriter().println();
    		response.getWriter().println();
    		try {
				printStats(fileName, response);
			} catch (Exception e) {
			}
    		response.flushBuffer();
    	}
    }
    
    
    private void printStats(String fileName, HttpServletResponse response) throws Exception
	{
		//String fileName = "/Users/markchen/Downloads/mbaas-access-log-20141128.txt";
		File file = new File(fileName);
		HashMap<String, StatObject> statMap = new HashMap<String, StatObject>();
		try {
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String  lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					
					//{"version":"V1.0.1","applicationId":"MBaas","requestId":"08dfa4c6-6ac6-4ee7-b5c2-0656e7c4dd1f","userId":null,"clientId":null,"threadName":"http-nio-8080-exec-25",
					//"nodeId":"mbaas-elb-dev-104792870.us-east-1.elb.amazonaws.com:8080","remoteIp":"127.0.0.1","bytesSent":"38455","protocol":"HTTP/1.0",
					//"headers":{"agent":"platform/iPhone OS platform_version/8.1 carrier_code/ carrier_name/ device_name/x86_64 device_id/CDB9788E-0644-43EF-BDD4-2947BDFD7E8C sdk_version/1.0.1 app_name/BBStudent app_version/1.0 timezone/PST","language":"en"},"schema":"AccessLogEvent",
					//"time":"2014-00-26T12:00:54-0054","responseMillis":"21947","method":"GET","query":null,"path":"/mbaas/api/v1/streams/search","status":"200"}
					if(lineTxt == null || lineTxt.isEmpty())
					{
						continue;
					}
					int k = lineTxt.indexOf("\"responseMillis\"");
					if(k > 0)
					{
						lineTxt = lineTxt.substring(k);
					}
					lineTxt = lineTxt.replace("\"", "");
					lineTxt = lineTxt.replace("responseMillis:", "");
					lineTxt = lineTxt.replace("method:", "");
					lineTxt = lineTxt.replace("query:", "");
					lineTxt = lineTxt.replace("path:", "");
					lineTxt = lineTxt.replace("status:", "");
					lineTxt = lineTxt.replace("}", "");
				//	System.out.println(lineTxt);
					
					String[] strs = lineTxt.split(",");
					String url = strs[3];
					if(!url.startsWith("/mbaas/api/v1"))
					{
						continue;
					}
					if(url.startsWith("/mbaas/api/v1/courses/"))
					{
						url = "/mbaas/api/v1/courses/";
					}
					long time = Long.parseLong(strs[0]);
					String status = strs[4];

					StatObject so = statMap.get(url);
					if(so == null)
					{
						so = new StatObject();
						statMap.put(url, so);
					}
					so.callNumber ++;
					so.totalTime += time;
					if(status != null && status.startsWith("5"))
					{
						if(status.startsWith("502") && time < 1000)
						{						
							//don't count "COURSE_NOT_ACCESSIBLE" case, it returns with code 502 and less than 1 sec
						}
						else
						{
							so.timeout ++;
						}
//						so.timeout ++;
					}
				}
				read.close();
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println(statMap);
		Set<String> keySet = statMap.keySet();
		response.getWriter().println("   API-URL                 , Call-times  ,   Avg_time(ms)   ,Timeout/Exception Times, Error Rate(%)");
		for(String key: keySet)
		{
			response.getWriter().println(key + "," + statMap.get(key));
		}
		
	}
    
    static public class StatObject
	{
		int callNumber = 0;
		long totalTime = 0;
		int timeout = 0;
		
		public String toString()
		{
			return "" + callNumber + "," + (totalTime/callNumber) + "," + timeout + "," + (100*timeout/callNumber);
		}
	}

}

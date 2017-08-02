package com.xpay.pay.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xpay.pay.util.JsonUtils;

public class HeartbeatServlet extends HttpServlet {
	
	private static final long serialVersionUID = 6161570329223911380L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServerStatus status = getServerStatus();
    	response.getWriter().println(JsonUtils.toJson(status));
    	response.flushBuffer();
    }
   
    private ServerStatus getServerStatus() {
    	ServerStatus status = new ServerStatus();
    	Runtime rt = Runtime.getRuntime();
    	int totalMemory = (int)(rt.totalMemory() >> 20);
    	int freeMemory = (int)(rt.freeMemory() >> 20);
    	int availableProcessors = rt.availableProcessors();
    	status.setTotalMemory(totalMemory);
    	status.setFreeMemory(freeMemory);
    	status.setAvailableProcessors(availableProcessors);
    	return status;
    }
    
    static class ServerStatus {
    	private String status = "ok";
    	private int totalMemory;
    	private int freeMemory;
    	private int availableProcessors;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public int getTotalMemory() {
			return totalMemory;
		}
		public void setTotalMemory(int totalMemory) {
			this.totalMemory = totalMemory;
		}
		public int getFreeMemory() {
			return freeMemory;
		}
		public void setFreeMemory(int freeMemory) {
			this.freeMemory = freeMemory;
		}
		public int getAvailableProcessors() {
			return availableProcessors;
		}
		public void setAvailableProcessors(int availableProcessors) {
			this.availableProcessors = availableProcessors;
		}
    }
}

package com.amaker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.amaker.res.MyConfig;

public class Check implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		/**
		 * Some about token will be done ,but it will be empty for debug
		 * 
		 * */
		//
		
		//Char Encoding
		req.setCharacterEncoding(MyConfig.CHAR_SET);
		resp.setCharacterEncoding(MyConfig.CHAR_SET);
		
		chain.doFilter(req, resp);
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}

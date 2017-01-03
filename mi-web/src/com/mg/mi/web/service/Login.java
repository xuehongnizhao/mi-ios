package com.mg.mi.web.service;

import javax.servlet.http.HttpServletRequest;

public class Login implements ServiceImpl {

	@Override
	public String run(HttpServletRequest req) {
		// TODO Auto-generated method stub
		String u = req.getParameter("user");
		String p = req.getParameter("pwd");
		
		return null;
	}

}

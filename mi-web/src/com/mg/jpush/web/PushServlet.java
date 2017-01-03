package com.mg.jpush.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mg.jpush.JdPush;



/**
 * Servlet implementation class push
 */
public class PushServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String appKey ="ba67598c7fc3879c5e77f738";
	private static final String masterSecret = "f4dacc421366cedea8a3a158";
	
	/**
     * Default constructor. 
     */
    public PushServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see Servlet#init(ServletConfig)
	 */
    public void init(ServletConfig config) throws ServletException {
    	// TODO Auto-generated method stub
    	System.out.println("push serverlet init\n");
    	super.init(config);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String title = "用户测试";
		Set<String> aliases = new HashSet<String>();
		aliases.add(id);
		JdPush.sendPushSelect(aliases, title);
		System.out.println("sucess");
	}

}

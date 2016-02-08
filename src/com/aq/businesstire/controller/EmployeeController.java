package com.aq.businesstire.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Controller
public class EmployeeController {
	
	
	@RequestMapping("/tables")
	public ModelAndView tables(HttpServletRequest request,HttpServletResponse responses)
	{
		ModelAndView mav=new ModelAndView();
		mav.setViewName("insert.jsp");
		return mav;
		
	}
	@RequestMapping("/querythem")
	public ModelAndView querythem(HttpServletRequest request,HttpServletResponse responses)
	{
		ModelAndView mav=new ModelAndView();
		mav.setViewName("retrieve.jsp");
		return mav;
	}
	
	@RequestMapping("/empinsert")
	public ModelAndView empController(HttpServletRequest request,HttpServletResponse responses){ 
		String name= request.getParameter("name");
		int age =Integer.parseInt(request.getParameter("age"));
		String gender= request.getParameter("gender");
		ModelAndView mav=new ModelAndView();
		String msg=null;
		try {
			Client client = Client.create();
			String names = URLEncoder.encode (name,"UTF-8").replace("+", "%20");
			String genders = URLEncoder.encode (gender,"UTF-8").replace("+", "%20");
			WebResource webResource = client.resource("http://localhost:8080/EmployeeOrderRest/api/insertEmp/"+names+"/"+age+"/"+genders);
			ClientResponse response = webResource.head();
		if (response.getStatus() == 200){
			msg="Employee entry inserted Successfully";
					}
			else {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("welcome.jsp");
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping("/orderinsert")
	public ModelAndView ordersController(HttpServletRequest request,HttpServletResponse responses){ 
		String name= request.getParameter("name");
		String desc= request.getParameter("description");
		ModelAndView mav=new ModelAndView();
		String msg=null;
		try {
			Client client = Client.create();
			String names = URLEncoder.encode (name,"UTF-8").replace("+", "%20");
			String descr= URLEncoder.encode (desc,"UTF-8").replace("+", "%20");
			WebResource webResource = client.resource("http://localhost:8080/EmployeeOrderRest/api/insertOrder/"+names+"/"+descr);
			ClientResponse response = webResource.head();
		if (response.getStatus() == 200){
			msg="Order entry inserted Successfully";
					}
			else {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("welcome.jsp");
		mav.addObject("msg",msg);
		return mav;
	}
	
	@RequestMapping("/display")
	public ModelAndView display(HttpServletRequest request,HttpServletResponse responses){ 
		String table= request.getParameter("table");
		String name= request.getParameter("name");
		ModelAndView mav=new ModelAndView();
		String msg=null;
		try {
			Client client = Client.create();
			String names = URLEncoder.encode (name,"UTF-8");
			WebResource webResource = client.resource("http://localhost:8080/EmployeeOrderRest/api/display/"+table+"/"+names);
			ClientResponse response = webResource.head();
		if (response.getStatus() == 200){
			
			JSONObject json = readJsonFromUrl("http://localhost:8080/EmployeeOrderRest/api/display/"+table+"/"+names);
			msg=json.toString();
		}
			else {
				throw new RuntimeException("papa Failed : HTTP error code : " + response.getStatus());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.setViewName("welcome.jsp");
		mav.addObject("msg",msg);
		return mav;
	}
	
	 private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }

	 public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
		  }
}

package com.google.sps.servlets;

//import com.google.cloud.translate.Translate;
//import com.google.cloud.translate.TranslateOptions;
//import com.google.cloud.translate.Translation;
//import com.google.sps.data.Comment;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.DatastoreOptions;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;


@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

private String errorMessage = "Username Already Exists: Please Try A Different Username.";
private boolean userNameTaken = false;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
 
      Gson gson = new Gson();

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;");

      if (userNameTaken){
        response.getWriter().println(gson.toJson(errorMessage));
      } 
      userNameTaken = false;
   }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

 //user's inputted username and password
     String useName = request.getParameter("usnm");
     String pword = request.getParameter("psw");

     Entity accountEntity = new Entity("Account");
    accountEntity.setProperty("username", useName);
    accountEntity.setProperty("password", pword);
   

//check to make sure username not already taken
    Filter propertyFilter =
    new FilterPredicate("username", FilterOperator.EQUAL, useName);
    
    Query query = new Query("Account").setFilter(propertyFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
//searches database for matching username
    for (Entity entity : results.asIterable()) {
    
      String aName = (String) entity.getProperty("username");
      String aPword = (String) entity.getProperty("password");

    if (useName.equals(aName)){
        userNameTaken = true;
        //response.sendRedirect("/signup.html");
        //break;
    }

    }

    if(userNameTaken){
        response.sendRedirect("/signup.html");
    } else {
        //if username not taken, put the username and password into the database
    datastore.put(accountEntity);


    //now that user exists in the system, forward them to the 
    //doPost of the login servlet, which should log them in
    request.getRequestDispatcher("/login").forward(request, response);

    }
    






        //  HttpSession session = request.getSession();
        //  String toLogin = (String)session.getAttribute("user");
        //  request.setAttribute("toLogout", toLogout);
        //  request.getRequestDispatcher("/login").include(request, response);

    //response.sendRedirect("/viewdecks.html");
 




      }



  }

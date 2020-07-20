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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

//true if failed login attempt, else false
//logout resets to false.
private boolean loginFail = false;

private String errorMessage = "Login Error: Please Try Again.";

//for keeping track of who has logged in
private int temporaryToken = 0;
private HashMap<String, Integer> loggedInUsersAndToken = new HashMap<String, Integer>();

 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //if page reloaded, display login error message (due to failed login attempt)
      

      Gson gson = new Gson();

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;");

      if (loginFail){
        response.getWriter().println(gson.toJson(errorMessage));
      } 


//part for logout
//if a user is requesting the log in page, then they are trying to logout
HttpSession session = request.getSession();
         String toLogout = (String)session.getAttribute("user");
//String toLogout = (String)request.getAttribute("toLogout");
      if(toLogout != null && !toLogout.equals("")){
session.setAttribute("user", "");
session.setAttribute("selectedDeck", "");            
resetLoginFail();
revokeToken(toLogout);
response.sendRedirect("/index.html");

      }



   }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

//user's inputted username and password
    String useName = request.getParameter("usnm");
    String pword = request.getParameter("psw");
 
    long timestamp = System.currentTimeMillis();

    

//database of user accounts
//Query query = new Query("Account");

Filter propertyFilter =
    new FilterPredicate("username", FilterOperator.EQUAL, useName);
    
    Query query = new Query("Account").setFilter(propertyFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    boolean loginSuccessp = false;
//searches database for matching username/password combo
    for (Entity entity : results.asIterable()) {
    
      String aName = (String) entity.getProperty("username");
      String aPword = (String) entity.getProperty("password");

    if (useName.equals(aName) && pword.equals(aPword)){
        loginSuccessp = true;
        break;
    }

    }
//if success, then sends user to main page, else redirects
//to login, where error message is displayed.
    if (loginSuccessp){
        //TODO: stores local session state on server indicating
        //that user has logged in
        //Session.put(<username>, <token>);
        loginFail = false;

        //token uniqueness check:
        verifyTokenUniqueness();

        HttpSession session = request.getSession();
        session.setAttribute("user", useName);
        session.setAttribute("token", temporaryToken);

        loggedInUsersAndToken.put(useName, temporaryToken);

        temporaryToken++;

        response.sendRedirect("/viewdecks.html");
    } else {
         loginFail = true;
response.sendRedirect("/index.html");



     }



  }

/**
*Ensures that the token 
*stored for the newly logged
* in user is not being used already
*
*/
  private void verifyTokenUniqueness(){
      HashSet<Integer> tokensInUse = new HashSet<Integer>(loggedInUsersAndToken.values());
      while(tokensInUse.contains(temporaryToken)){
          temporaryToken++;
      }

  }

/**
*Accessed by /logout, which resets loginFail to False
*
*/
  public void resetLoginFail(){
      loginFail = false;
  }

/**
*Accessed by /logout, which revokes token
* (logs out user by removing them and their token from the 
*loggedInUsersAndToken hashmap)
*
*/
  public void revokeToken(String userToLogout){
      //HttpSession session = request.getSession();
      //String userToLogout = session.getAttribute("user");
      loggedInUsersAndToken.remove(userToLogout);
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
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
@WebServlet("/createcard")
public class CreateCardServlet extends HttpServlet {

private boolean cardAdded = false;

private String addMessage = "New Card added at timestamp: ";

//for keeping track of who has logged in
//private int temporaryToken = 0;
//private HashMap<String, Integer> loggedInUsersAndToken = new HashMap<String, Integer>();

 

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      //if card added, indicate when added.
      long timestamp = System.currentTimeMillis();

      String messageToPrint = addMessage + timestamp;

      Gson gson = new Gson();

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;");

      if (cardAdded){
        response.getWriter().println(gson.toJson(messageToPrint));
      } 
cardAdded = false;





   }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

//user's inputted username and password
    String frontText = request.getParameter("frnt");
    String backText = request.getParameter("bck");
 
    long timestamp = System.currentTimeMillis();
    
    Query query = new Query("Card").addSort("cardID", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    long nextCardID = 0;

    for (Entity entity : results.asIterable()) {
    
      nextCardID = ((long) entity.getProperty("cardID")) + 1;
      break;

    }
//*********
//temporary:
HttpSession session = request.getSession();
        //session.setAttribute("selectedDeck", "");
        if(!session.getAttribute("selectedDeck").equals("")){
//*********
    Entity cardEntity = new Entity("Card");
    cardEntity.setProperty("front", frontText);
    cardEntity.setProperty("back", backText);
    cardEntity.setProperty("timestamp", timestamp);
    cardEntity.setProperty("cardID", nextCardID);
    cardEntity.setProperty("creatorID", (String)session.getAttribute("user"));
    cardEntity.setProperty("deckID", (String)session.getAttribute("selectedDeck"));
//***********************************************************
//IMPORTANT: WHEN DECK CREATION IS IMPLEMENTED, SHOULD STORE
//IN THE SESSION WHICH DECK IS CURRENTLY BEING LOOKED AT SO THAT
//DECKID CAN ACCESS IT^^^^^^^^^^^^^^^
//(deckIDs should be unique)
//***********************************************************


    datastore.put(cardEntity);
    cardAdded = true;
  }
    response.sendRedirect("/createcard.html");
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


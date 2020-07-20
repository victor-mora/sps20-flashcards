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
@WebServlet("/createdeck")
public class CreateDeckServlet extends HttpServlet {

private boolean deckAdded = false;

private boolean attemptMade = false;

private String addMessage = "New Deck added at timestamp: ";

private String errMessage = "Deck not added. Deck with this name already exists in your decks.";

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

      if (attemptMade){
          attemptMade = false;
          if(deckAdded){
              deckAdded = false;
response.getWriter().println(gson.toJson(messageToPrint));
          } else {
response.getWriter().println(gson.toJson(errMessage));
          }
        
      } 
//cardAdded = false;





   }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

      attemptMade = true;

//user's inputted username and password
    String inputtedName = request.getParameter("potentialDeckName");
 
    long timestamp = System.currentTimeMillis();
    
    Query query = new Query("Deck");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    //long nextCardID = 0;
    boolean nameTaken = false;

    for (Entity entity : results.asIterable()) {

        String thisDecksName = (String) entity.getProperty("deckID");
    
      if(inputtedName.equals(thisDecksName)){
          nameTaken = true;
          break;
      }

    }

    if(nameTaken){
        response.sendRedirect("/createdeck.html");
    } else {
        //*********
//so that they can immediately add cards to this deck if they want:
HttpSession session = request.getSession();
        session.setAttribute("selectedDeck", inputtedName);
//*********

    
    Entity deckEntity = new Entity("Deck");
    deckEntity.setProperty("timestamp", timestamp);
    deckEntity.setProperty("creatorID", (String)session.getAttribute("user"));
    deckEntity.setProperty("deckID", (String)session.getAttribute("selectedDeck"));
//***********************************************************
//IMPORTANT: WHEN DECK CREATION IS IMPLEMENTED, SHOULD STORE
//IN THE SESSION WHICH DECK IS CURRENTLY BEING LOOKED AT SO THAT
//DECKID CAN ACCESS IT^^^^^^^^^^^^^^^
//(deckIDs should be unique)
//***********************************************************


    datastore.put(deckEntity);
    deckAdded = true;
    

    response.sendRedirect("/createdeck.html");
  }
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
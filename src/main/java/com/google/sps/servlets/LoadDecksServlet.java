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

import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;

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
import java.util.Collection;
import java.util.Random;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/loaddecks")
public class LoadDecksServlet extends HttpServlet {


private PreparedQuery results = null;    
    
//private HashSet<Entity> cardsInDeck = new HashSet<Entity>();
 
 //private Entity currCard = null;

 //private String messageToReturn = "Ran out of cards in deck. Please reshuffle";

 //private boolean litep = false;

 //private boolean fullp = false;

 //private boolean moveOnp = false;

 //private boolean reshufflep = false;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


      HttpSession session = request.getSession();
      //*********Pull in decks if first time loading******
      if(results == null){
          Filter propertyFilterOne =
    new FilterPredicate("creatorID", FilterOperator.EQUAL, (String)session.getAttribute("user"));

    
    Query query = new Query("Deck").setFilter(propertyFilterOne);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //PreparedQuery results = datastore.prepare(query);
    results = datastore.prepare(query);

      }
      ArrayList<String> deckNames = new ArrayList<String>();

      for(Entity entity : results.asIterable()){

          deckNames.add((String) entity.getProperty("deckID"));
      }

      Gson gson = new Gson();

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;");

        results = null;
          response.getWriter().println(gson.toJson(deckNames));
     

          
      





   }



    @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

// //user's inputted username and password
//     String frontText = request.getParameter("frnt");
//     String backText = request.getParameter("bck");
 
//     long timestamp = System.currentTimeMillis();
    
//     Query query = new Query("Card").addSort("cardID", SortDirection.DESCENDING);
//     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//     PreparedQuery results = datastore.prepare(query);

//     long nextCardID = 0;

//     for (Entity entity : results.asIterable()) {
    
//       nextCardID = ((long) entity.getProperty("cardID")) + 1;
//       break;

//     }
// //*********
// //temporary:
// HttpSession session = request.getSession();
//         session.setAttribute("selectedDeck", 0);
// //*********
//     Entity cardEntity = new Entity("Card");
//     cardEntity.setProperty("front", frontText);
//     cardEntity.setProperty("back", backText);
//     cardEntity.setProperty("timestamp", timestamp);
//     cardEntity.setProperty("cardID", nextCardID);
//     cardEntity.setProperty("creatorID", (String)session.getAttribute("user"));
//     cardEntity.setProperty("deckID", (Integer)session.getAttribute("selectedDeck"));
// //***********************************************************
// //IMPORTANT: WHEN DECK CREATION IS IMPLEMENTED, SHOULD STORE
// //IN THE SESSION WHICH DECK IS CURRENTLY BEING LOOKED AT SO THAT
// //DECKID CAN ACCESS IT^^^^^^^^^^^^^^^
// //(deckIDs should be unique)
// //***********************************************************


//     datastore.put(cardEntity);
//     cardAdded = true;

//     response.sendRedirect("/createcard.html");
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
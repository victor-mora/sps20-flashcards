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
@WebServlet("/study")
public class StudyServlet extends HttpServlet {


private PreparedQuery results = null;    
    
private HashSet<Entity> cardsInDeck = new HashSet<Entity>();
 
 private Entity currCard = null;

 private String messageToReturn = "Ran out of cards in deck. Please reshuffle";

 //private boolean litep = false;

 //private boolean fullp = false;

 //private boolean moveOnp = false;

 //private boolean reshufflep = false;
 

 public void reloadCards(HttpSession session){
//HttpSession session = request.getSession();

     Filter propertyFilterOne =
    new FilterPredicate("creatorID", FilterOperator.EQUAL, (String)session.getAttribute("user"));

    Filter propertyFilterTwo =
    new FilterPredicate("deckID", FilterOperator.EQUAL, (String)session.getAttribute("selectedDeck"));

    // Use CompositeFilter to combine multiple filters
CompositeFilter superFilter =
    CompositeFilterOperator.and(propertyFilterOne, propertyFilterTwo);
    
    Query query = new Query("Card").setFilter(superFilter);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    //PreparedQuery results = datastore.prepare(query);
    results = datastore.prepare(query);

    cardsInDeck = new HashSet<Entity>();

            for(Entity card : results.asIterable()){
                cardsInDeck.add(card);
            }

            currCard = null;
 }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
     HttpSession session = request.getSession();

     //*********Pull in deck if first time loading******
      if(results == null){
          reloadCards(session);
      }

      int iterr = 0;
//checks to make sure user hasnt switched decks
      for(Entity card : results.asIterable()){
          iterr++;
         if(!((String)card.getProperty("deckID"))
         .equals(session.getAttribute("selectedDeck"))){
             reloadCards(session);
         }
                break;
            }

//^^^^^ handles case where deck is empty, 
//so the above reload gets skipped upon switching decks

            if(iterr == 0){
                reloadCards(session);
            }

            //iterr = 0;




        if(((String)session.getAttribute("reshufflep")).equals("YES")){
            //reshuffle and reset
            //cardsInDeck = new HashSet<Entity>(results.asIterable());
            session.setAttribute("cardBack", "");
            session.setAttribute("reshufflep", "NO");
            

            cardsInDeck = new HashSet<Entity>();

            for(Entity card : results.asIterable()){
                cardsInDeck.add(card);
            }

    
            currCard = null;
            //session.setAttribute("reshufflep", "NO");
            //session.setAttribute("cardBack", "");

            //litep = false;
            //fullp = false;

        }

        if(((String)session.getAttribute("moveOnp")).equals("YES")){
            currCard = null;
            session.setAttribute("moveOnp", "NO");
            session.setAttribute("cardBack", "");
            
   

        }

//right now onload checks card ur on. if not on a card, picks a new card.
//if still no card, sends message that reshuffle needed.
//else displays front of card in front-container
//TODO: 
//[]implement lite reveal, full reveal, 
//[]move to next card (just make currCard null), 
//[]and reshuffle (cardsInDeck = new HashSet<Entity>(results.asIterable());)
//[]maybe make more servlets that can request dispatcher and send over variables
//[]to fill in the back containers? or store in session?
//test

//int cdsize = cardsInDeck.size();

      if(currCard == null){
          //make random card selection
          int i = 0;
          int cdsize = cardsInDeck.size();

          Random rand = new Random();

          int randInt = 0;

          try{

              randInt = rand.nextInt(cdsize);

          } catch(Exception e){
              randInt = 0;
          }

         

         //ensure reshuffle message is displayed?
          if(cdsize == 0){
              currCard = null;
          } else {

              for(Entity en : cardsInDeck){
              if(i == randInt){
                 currCard = en;
                 cardsInDeck.remove(en);
                 break;
             } 
              i++;
          }

          }

        
      }

    

      

      Gson gson = new Gson();

        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;");

        //if still null, then they need to reshuffle

      if(currCard == null){
          session.setAttribute("cardBack", "");
          response.getWriter().println(gson.toJson(messageToReturn));
      }else {
          session.setAttribute("cardBack", (String)currCard.getProperty("back"));

          response.getWriter().println(gson.toJson((String)currCard.getProperty("front")));

          //test to see if gets rid of old back card when next card is activated
           // response.sendRedirect("/createcard.html");
      }





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

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function getErr() {
  fetch('/login').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;

    
  });
}

function getErrSignUp() {
  fetch('/signup').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;

    
  });
}

function cardAddMessage() {
  fetch('/createcard').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('ea-container');
  greetingContainer.innerText = comList;

    
  });
  loadDeckDropdown();
  showCurrentDeck();

}

function deckAddMessage() {
  fetch('/createdeck').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;

    
  });
  loadDeckDropdown();
}

function getCardFront() {
  fetch('/study').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('front-container');
  greetingContainer.innerText = comList;

    
  });
}

function getCardBack() {
  fetch('/back').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('back-container');
  greetingContainer.innerText = comList;

    
  });
}

function reshuffle() {
  fetch('/reshuffle').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('front-container');
  greetingContainer.innerText = comList;

    
  });

  getCardBack();
}

function nextCard() {
  fetch('/nextCard').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('front-container');
  greetingContainer.innerText = comList;

    
  });

  getCardBack();
}

function liteReveal() {
  fetch('/litereveal').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('back-container');
  greetingContainer.innerText = comList;

    
  });
  getCardFront();
}

function fullReveal() {
  fetch('/fullreveal').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('back-container');
  greetingContainer.innerText = comList;

    
  });
  getCardFront();
}

function loadDeckDropdown() {
  fetch('/loaddecks').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const comListElement = document.getElementById('deckSelect');
  comList.forEach((line) => {
      var option = document.createElement("option");
  option.text = line;
  comListElement.add(option);
      //comListElement.appendChild(createListElement(line));
    });

    
  });

}


function chooseDeck() {
        const deckCode = document.getElementById('deckSelect').value;
        const emContainer = document.getElementById('em-container');
        emContainer.innerText = '';

        const params = new URLSearchParams();
        params.append('deckCode', deckCode);

        fetch('/setdeck?' + params.toString()).then(response => response.json()).then((comList) => {
    const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;
    //comList.forEach((line) => {
      //comListElement.appendChild(createListElement(line));
    //});
  });

      }


function chooseDeckAndMove() {
        const deckCode = document.getElementById('deckSelect').value;
        const emContainer = document.getElementById('em-container');
        emContainer.innerText = '';

        const params = new URLSearchParams();
        params.append('deckCode', deckCode);

        fetch('/setdeck?' + params.toString()).then(response => response.json()).then((comList) => {
    const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;
    //comList.forEach((line) => {
      //comListElement.appendChild(createListElement(line));
    //});
  });

  window.location.href = "viewcards.html";

      }


function loadCardDropdown() {
  fetch('/loadcards').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const comListElement = document.getElementById('cardSelect');
  comList.forEach((line) => {
      var option = document.createElement("option");
  option.text = line;
  comListElement.add(option);
      //comListElement.appendChild(createListElement(line));
    });

    
  });

}

function showCurrentDeck() {
  fetch('/currdeck').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;

    
  });

}

function logUserOut() {
  fetch('/logout').then(response => response.json()).then((comList) => {
    // comList is an object, not a string, so we have to
    // reference its fields to create HTML content


const greetingContainer = document.getElementById('em-container');
  greetingContainer.innerText = comList;

    
  });

}
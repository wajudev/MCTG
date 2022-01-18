# `Monster Trading Cards Game (MTCG) - Protocol`

- Author: if20b054 - WS2021 SWEN1/116183 Project  - FH Technikum Wien

- An HTTP/REST-based server is built to be a platform for trading and battling with and
  against each other in a magical card-game world. 

## `Technical Steps (Designs, Failures & Selected Solutions)`

# `Design`

`Server` 
- TCP Listener
  Creates a ServerSocket on port 10001 and starts a new thread for each request. This thread sends a buffered reader 
  with the request to the unwrapper and gets a RequestContext returned. Which is further transmitted to the ResponseHandler 
  for processing. The response is finally sent to the client in the ResponseHandler.

- RequestHandler
  Gets a buffered reader and tries to store its content in the RequestContext. 
  The Http Verb, Http Version, URI, header values and payload are available for this.

- RequestContext
  The RequestContext class contains information about the HTTP request in the HttpContext property. 
  It contains information about the route that matched the current request in the RouteData property.

- ResponseContext
  It contains information about variables for the outgoing Http Response and exposes the ContentLength.

- Controllers - Battle, Card, Trade, User
  They provide access to the application behavior that was typically defined through a service interface. 
  They interpret user input and transform it into a model that is represented to the user by the view.

`MTCG`
- User
  The user class contains all information about the user.

- Card
  The card contains all information and behaviour about a card: ID, Name, Damage, CardType, MonsterType ElementType etc. 
  Acts as a super class for both Monster & Spell card class. 

- Deck
  The deck is a collection of cards that can be interacted with. 
  Cards can be added, removed, randomly drawn and further queried whether cards are currently available in the deck. 
  Once a card is in a deck it's deemed locked and can only be used for battle

- Stack
  The Stack is also a collection of cards that can be interacted with, Stacks hold more cards than a deck(4 cards only) and are available for trades.

- CardType 
  Enum with attributes of either Spell or Monster.

- MonsterType 
  Enum with the following attributes: Dragon, FireElf, Goblin, Knight, Kraken, Ork, Wizard.

- ElementType
  Enum with the following attributes: Water, Fire, Normal.

- Trade
  
- Battle
- Serializers

`Database`
The database consists of 4 tables.
Users Table with all user data.
Sessions Table contains client token and where they were last logged in. 
Cards Table with all data about the cards and, if available, the owner with a foreign key on the userID of users, and their associated package id.
Trade table with the information of a trade as well as the foreign key on the cardID of the cards and also the userID associated with trade.

SQL statements for creating the database are attached to the GitRepository.
database.sql
- DatabaseServices -> UserService, CardService & TradeService
  Holds all the information for the database, connects and makes these requesting classes available.


# `Integration Test`
 The provided .bat file for this course was converted to .sh to fit my system requirements, although not 
 fully automated (Read Failures and Selected Solutions). Postman was also used to run test number "13 show configured deck different representation", 
 since I couldn't figure out the correct syntax to run it in the shell file. 
 All tests were successful except test 17 (Read Failures and Selected Solutions) 

# `Failures and Selected Solutions`
The two biggest sources of error were the lack of knowledge about the structure of a rest server 
and little to no Java experiences prior to this course. 
Understanding the structure of the server took some time and raised most of the questions. Many attempts finally led to a working result.
 
Another problem was , I wasn't sure what to do with the curl script and started programming a console application
things got really messy, and I ended up running the script using scratch files, then postman before converting the bat file to shell script.

Test 13, isn't running in the .sh, but works fine on postman and in scratch files.

As mentioned above , test 17 wasn't successful, I'm yet to find a solution to the problem (Empty response body, causing a NULL POINTER Exception).
However, in the meantime, I have written unittests for the class(Battle). Hopefully, there's enough time to solve it.

Anytime I let the curl script (.sh file) run, the server shuts down after executing a few commands.
everything seems to be working fine, if I copy and paste in the terminal and not run everything at once.  

# `Unit Tests`

# `Time Tracking`
The time invested in this project was significantly high, this was expected due to reasons like lack of programming experience in JAVA etc. 
However for the most part, there was a lot spent on being in a constant state of confusion as to how everything was to be built and my initial approach 
is different to the one of the current version.

The time spent on this project is estimated to be 80hrs 
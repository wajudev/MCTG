# `Monster Trading Cards Game (MTCG) - Protocol`

- Author: if20b054 - WS2021 SWEN1/116183 Project  - FH Technikum Wien

- An HTTP/REST-based server is built to be a platform for trading and battling with and
  against each other in a magical card-game world. 

# `Git`
  https://github.com/wajudev/MCTG.git

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
- `User`
  The user class contains all information about the user.

- `Card`
  The card contains all information and behaviour about a card: ID, Name, Damage, CardType, MonsterType ElementType etc. 
  Acts as a super class for both Monster & Spell card class. 

- `Deck`
  The deck is a collection of cards that can be interacted with. 
  Cards can be added, removed, randomly drawn and further queried whether cards are currently available in the deck. 
  Once a card is in a deck it's deemed locked and can only be used for battle

- `Deck`
  The Stack is also a collection of cards that can be interacted with, Stacks hold more cards than a deck(4 cards only) and are available for trades.

- `CardType` 
  Enum with attributes of either Spell or Monster.

- `MonsterType` 
  Enum with the following attributes: Dragon, FireElf, Goblin, Knight, Kraken, Ork, Wizard.

- `ElementType`
  Enum with the following attributes: Water, Fire, Normal.

- `Trade`
  Cards can be traded for other cards if they meet certain criteria. 

- `Battle`
  Battle logic and all information concerning battles

- `Serializers`
  Objectmappers
  Helps with the conversion of Java Classes to JSON Formats and vice versa.
  

`Database`
- The database consists of 4 tables.
- `Users Table` with all user data.
- `Sessions Table` contains client token and where they were last logged in. 
- `Cards Table` with all data about the cards and, if available, the owner with a foreign key on the userID of users, and their associated package id.
- `Trade Table` with the information of a trade as well as the foreign key on the cardID of the cards and also the userID associated with trade.

  SQL statements for creating the database are attached to the GitRepository.
  database.sql
- DatabaseServices -> UserService, CardService & TradeService
  Holds all the information for the database, connects and makes these requesting classes available.


# `Integration Test`
  
  The provided .bat file for this course was converted to .sh to fit my OS requirements, although not 
  fully automated (Read Failures and Selected Solutions). Postman was also used to run test number 
  "13 show configured deck different representation", 
  since I couldn't figure out the correct syntax to run it in the shell file. 
  

# `Failures and Selected Solutions`

  The two biggest sources of error were the lack of knowledge about the structure of a rest api server 
  and little to no Java experiences prior to this course. 
  Understanding the structure of the server took some time and raised most of the questions. Many attempts finally led to a working result.
 
  Another problem was , I wasn't sure what to do with the curl script and started programming a console application
  things got really messy, and I ended up running the script using scratch files, then postman before converting the .bat file to .sh.
  Anytime I let the curl script (.sh file) run, the server shuts down after executing a few commands.
  everything seems to be working fine, if I copy and paste in the terminal and not run the script at once.

  Test 13, isn't running in the .sh file, but works fine on postman and in scratch files.

  My Battle logic is really flawed, that's something that could be improved.


# `Unit Tests`
  I wrote just enough unittests to fulfill the project requirements, Some tests can alter the database, 
  but I made sure they can be deleted after running the test. It came to my knowledge rather late, that testing the 
  Database is not really a necessary thing to do. There are no unittests for the HTTP Server, I found it rather 
  unnecessary to test this part of the project(maybe I should, I might figure out why the HTTP server keeps crashing).
  Most importantly , my unittests were selected to make sure key parts of the project were functioning. for example, Decks
  were tested to make sure the correct amount of cards is in there at all times, or that the card inserted in the database has the
  correct cardType or The battle logic works as I have programmed it to.

# `Unique / Optional Features `
   - MonsterType or ElementType are randomized if not specified
   - If no Card is offered in trade, the buyer(whoever sends the request) pays 5 coins.
   - Amount of games won, lost or drawn are documented.
   - Last logged In, exists in database.
   - Players ranked by Elo on scoreboard.
   - Win/Loss Ratio implemented
   
# `Time Tracking`
  The time invested in this project was significantly high, this was expected due to various reasons e.g. lack of programming experience in JAVA prior to this course etc. 
  However for the most part, there was a lot spent on being in a constant state of confusion as to how everything was to be built. Comparing my initial approach to the
  current version, one will see how much was changed.

  The time spent on this project is estimated to be **92hrs** 

# `Lesson Learned`
  - The best way to learn a programming language is by practising and writing more code, my knowledge/Skills
     in JAVA has significantly increased due to this project.
  - Test! Test!! Test!!!
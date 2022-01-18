#!/bin/sh

echo "create users:"
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"
echo
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"altenhof\", \"password\":\"markus\"}"
echo
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"admin\",  \"password\":\"istrator\"}"
echo
echo "should fail:"
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"
echo
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"different\"}"
echo
echo
echo "user login:"
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"daniel\"}"
echo
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"altenhof\", \"password\":\"markus\"}"
echo
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"admin\",    \"password\":\"istrator\"}"
echo
echo "should fail:"
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"username\":\"kienboec\", \"password\":\"different\"}"
echo
echo
echo "4 add new packages , admin only"
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\": 10.0}, {\"id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 50.0}, {\"id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"name\":\"WaterSpell\", \"damage\": 20.0}, {\"id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 45.0}, {\"id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"name\":\"FireSpell\", \"elementType\":\"Fire\", \"damage\": 25.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"644808c2-f87a-4600-b313-122b02322fd5\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\":  9.0}, {\"id\":\"4a2757d6-b1c3-47ac-b9a3-91deab093531\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 55.0}, {\"id\":\"91a6471b-1426-43f6-ad65-6fc473e16f9f\", \"name\":\"WaterSpell\", \"damage\": 21.0}, {\"id\":\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 55.0}, {\"id\":\"f8043c23-1534-4487-b66b-238e0c3c39b5\", \"name\":\"WaterSpell\", \"elementType\":\"Water\", \"damage\": 23.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"b017ee50-1c14-44e2-bfd6-2c0c5653a37c\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\": 11.0}, {\"id\":\"d04b736a-e874-4137-b191-638e0ff3b4e7\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 70.0}, {\"id\":\"88221cfe-1f84-41b9-8152-8e36c6a354de\", \"name\":\"WaterSpell\", \"damage\": 22.0}, {\"id\":\"1d3f175b-c067-4359-989d-96562bfa382c\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 40.0}, {\"id\":\"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\", \"name\":\"RegularSpell\", \"elementType\":\"Normal\", \"damage\": 28.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"ed1dc1bc-f0aa-4a0c-8d43-1402189b33c8\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\": 10.0}, {\"id\":\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 50.0}, {\"id\":\"55ef46c4-016c-4168-bc43-6b9b1e86414f\", \"name\":\"WaterSpell\", \"damage\": 20.0}, {\"id\":\"f3fad0f2-a1af-45df-b80d-2e48825773d9\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 45.0}, {\"id\":\"8c20639d-6400-4534-bd0f-ae563f11f57a\", \"name\":\"WaterSpell\", \"elementType\":\"Water\", \"damage\": 25.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"d7d0cb94-2cbf-4f97-8ccf-9933dc5354b8\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\":  9.0}, {\"id\":\"44c82fbc-ef6d-44ab-8c7a-9fb19a0e7c6e\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 55.0}, {\"id\":\"2c98cd06-518b-464c-b911-8d787216cddd\", \"name\":\"WaterSpell\", \"damage\": 21.0}, {\"id\":\"951e886a-0fbf-425d-8df5-af2ee4830d85\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 55.0}, {\"id\":\"dcd93250-25a7-4dca-85da-cad2789f7198\", \"name\":\"FireSpell\", \"elementType\":\"Fire\", \"damage\": 23.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"b2237eca-0271-43bd-87f6-b22f70d42ca4\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"elementType\":\"Water\", \"damage\": 11.0}, {\"id\":\"9e8238a4-8a7a-487f-9f7d-a8c97899eb48\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 70.0}, {\"id\":\"d60e23cf-2238-4d49-844f-c7589ee5342e\", \"name\":\"WaterSpell\", \"damage\": 22.0}, {\"id\":\"fc305a7a-36f7-4d30-ad27-462ca0445649\", \"name\":\"Ork\", \"monsterType\":\"Ork\", \"damage\": 40.0}, {\"id\":\"84d276ee-21ec-4171-a509-c1b88162831c\", \"name\":\"RegularSpell\", \"elementType\":\"Normal\", \"damage\": 28.0}]"
echo
echo
echo "5 buy packages kienboec"
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo
echo "should fail no money:"
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d ""
echo
echo
echo "6 add new packages, admin only"
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"damage\": 10.0}, {\"id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"name\":\"RegularSpell\", \"elementType\":\"Normal\", \"damage\": 50.0}, {\"id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"name\":\"Knight\", \"monsterType\":\"Knight\", \"damage\": 20.0}, {\"id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"name\":\"RegularSpell\", \"elementType\":\"Normal\", \"damage\": 45.0}, {\"id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"name\":\"FireElf\", \"monsterType\":\"FireElf\", \"damage\": 25.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\", \"damage\":  9.0}, {\"id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"name\":\"FireSpell\", \"elementType\":\"Fire\", \"damage\": 55.0}, {\"id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"name\":\"Knight\", \"monsterType\":\"Knight\", \"damage\": 21.0}, {\"id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"name\":\"FireSpell\", \"elementType\":\"Fire\", \"damage\": 55.0}, {\"id\":\"a1618f1e-4f4c-4e09-9647-87e16f1edd2d\", \"name\":\"FireElf\", \"monsterType\":\"FireElf\", \"damage\": 23.0}]"
echo
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Basic admin-mtcgToken" -d "[{\"id\":\"2272ba48-6662-404d-a9a1-41a9bed316d9\", \"name\":\"WaterGoblin\", \"monsterType\":\"Goblin\",  \"damage\": 11.0}, {\"id\":\"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\", \"name\":\"Dragon\", \"monsterType\":\"Dragon\", \"damage\": 70.0}, {\"id\":\"166c1fd5-4dcb-41a8-91cb-f45dcd57cef3\", \"name\":\"Knight\", \"monsterType\":\"Knight\", \"damage\": 22.0}, {\"id\":\"237dbaef-49e3-4c23-b64b-abf5c087b276\", \"name\":\"WaterSpell\", \"elementType\":\"Water\", \"damage\": 40.0}, {\"id\":\"27051a20-8580-43ff-a473-e986b52f297a\", \"name\":\"FireElf\", \"monsterType\":\"FireElf\", \"damage\": 28.0}]"
echo
echo "7 buy packages altenhof"
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo
echo "should fail: no money"
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d ""
echo
echo "8 show all acquired cards kienboec"
curl -X GET http://localhost:10001/cards --header "Authorization: Basic kienboec-mtcgToken"
echo "should fail: no token"
curl -X GET http://localhost:10001/cards
echo
echo "9 show all acquired cards altenhof"
curl -X GET http://localhost:10001/cards --header "Authorization: Basic altenhof-mtcgToken"
echo
echo "10 show unconfigured deck"
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "11 configure deck"
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"27051a20-8580-43ff-a473-e986b52f297a\", \"237dbaef-49e3-4c23-b64b-abf5c087b276\", \"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\", \"166c1fd5-4dcb-41a8-91cb-f45dcd57cef3\"]"
echo
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "[\"d04b736a-e874-4137-b191-638e0ff3b4e7\", \"1d3f175b-c067-4359-989d-96562bfa382c\", \"dcd93250-25a7-4dca-85da-cad2789f7198\", \"55ef46c4-016c-4168-bc43-6b9b1e86414f\"]"
echo
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "should fail and show original from before:"
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"171f6076-4eb5-4a7d-b3f2-2d650cc3d237\"]"
echo
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "should fail ... only 3 cards set"
curl -X PUT http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "[\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"d60e23cf-2238-4d49-844f-c7589ee5342e\"]"
echo
echo "12 show configured deck"
curl -X GET http://localhost:10001/deck --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/deck --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "13 show configured deck different representation"
echo "kienboec"
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: Basic kienboec-mtcgToken"
echo
echo
echo "altenhof"
curl -X GET http://localhost:10001/deck?format=plain --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "14 edit user data"
echo
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic altenhof-mtcgToken"
echo
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"bio\": \"me playin...\", \"image\": \":-)\"}"
echo
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "{\"bio\": \"me codin...\",  \"image\": \":-D\"}"
echo
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic altenhof-mtcgToken"
echo
echo "should fail:"
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Basic altenhof-mtcgToken"
echo
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "{\"name\": \"Hoax\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
echo
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"name\": \"Hoax\", \"Bio\": \"me codin...\",  \"Image\": \":-D\"}"
echo
curl -X GET http://localhost:10001/users/someGuy  --header "Authorization: Basic kienboec-mtcgToken"
echo
echo "15 stats"
curl -X GET http://localhost:10001/stats --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/stats --header "Authorization: Basic altenhof-mtcgToken"
echo
echo "16 scoreboard"
curl -X GET http://localhost:10001/score --header "Authorization: Basic kienboec-mtcgToken"
echo
echo
echo "17 battle"
start /b "kienboec battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic kienboec-mtcgToken"
start /b "altenhof battle" curl -X POST http://localhost:10001/battles --header "Authorization: Basic altenhof-mtcgToken"
ping localhost -n 10 >NUL 2>NUL
echo "18 Stats"
echo "kienboec"
curl -X GET http://localhost:10001/stats --header "Authorization: Basic kienboec-mtcgToken"
echo
echo "altenhof"
curl -X GET http://localhost:10001/stats --header "Authorization: Basic altenhof-mtcgToken"
echo
echo
echo "19 scoreboard"
curl -X GET http://localhost:10001/score --header "Authorization: Basic kienboec-mtcgToken"
echo
echo
echo "20 trade"
echo "check trading deals"
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo
echo "create trading deal"
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"cardToTrade\": \"fc305a7a-36f7-4d30-ad27-462ca0445649\", \"type\": \"monster\", \"minimumDamage\": 15}"
echo
echo "check trading deals"
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic altenhof-mtcgToken"
echo
echo "delete trading deals"
curl -X DELETE http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Authorization: Basic kienboec-mtcgToken"
echo
echo


echo "21 check trading deals"
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X POST http://localhost:10001/tradings --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"cardToTrade\": \"fc305a7a-36f7-4d30-ad27-462ca0445649\", \"type\": \"monster\", \"minimumDamage\": 15}"
echo "check trading deals"
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/tradings  --header "Authorization: Basic altenhof-mtcgToken"
echo
echo "try to trade with yourself should fail"
curl -X POST http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "\"65ff5f23-1e70-4b79-b3bd-f6eb679dd3b5\""
echo
echo "try to trade"
echo
curl -X POST http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0 --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken" -d "\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\""
echo
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic kienboec-mtcgToken"
echo
curl -X GET http://localhost:10001/tradings --header "Authorization: Basic altenhof-mtcgToken"
echo


echo "end..."

ping localhost -n 100 >NUL 2>NUL




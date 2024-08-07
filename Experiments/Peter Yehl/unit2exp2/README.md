SpringBoot version 2.4.0
JAVA JDK version 11

# SpringBoot JPA: One to One relation example
### One to One relations
In object-oriented programming, real-life entities are represented as classes, and in MySQL, they are stored as tables. JPA/Hibernate is a framework that matches a Class to Table(
each variable represents a column) to ease the manipulation of data in Java. Entities almost always interact with one another and establish individual relationships among 
themselves Eg: A Person will have exactly one social security number(One to One), A person can have multiple cars(One to Many), etc. This example is created with the assumption
that one person can have only one laptop(made by poor people). It shows how a one to one relation can be represented between entities using JPA/Hibernate.
### Pre-requisites

0. Go through the springboot_unit1_2_hellopeople
1. Maven has to be installed on command line OR your IDE must be configured with maven
2. Java version 11
3. Install MySQL database (service & workbench) on localhost, create new schema 'mydb'

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean package</code></br>
<code>java -jar target/onetoone-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Available End points from POSTMAN: CRUDL
1. CREATE requests - 
POST request: 
    1. /laptops - Create a new Laptop. The request has to be of type application/JSON input format 
    {
        "cpuClock" : "1.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "1000000"
    }
    2. /users - Create a new User with laptop. Request Format : application/json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com",
        laptop   : {
            "cpuClock" : "2.3",
            "cpuCores"  : "4",
            "ram"   : "8",
            "manufacturer" : "Hp",
            "cost" : "600"
        }
    }.
    3. /users - Create user without laptop. Request Format : application / json
    {
        "name" : "John",
        "email"  : "johndoe@somemail.com"
    }
2. READ requests -
GET request:
    1. /laptops/{id} - Get laptop object for provided id
    2. /users/{id} - Get user object with associated laptop for provided user id

3. UPDATE requests -
PUT request : 
    1. /laptops/{id} - Update info on a laptop whose id is provided on the URL. The modified info is sent in the body of the message
    {
        "cpuClock" : "2.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "2000000"
    }
    2. /users/{id} - Update User and optionally a lptop associated with him/her for id provided in the url
    {
        "name" : "John",
        "email"  : "johndoe@newmail.com",
        laptop   : {
            "id" : "5"
            "cpuClock" : "3.3",
            "cpuCores"  : "8",
            "ram"   : "16",
            "manufacturer" : "new Laptop",
            "cost" : "1000"
        }
    }.

4. DELETE a record - 
 DELETE request:
    1. /laptops/{id} - Delete the entry with id in te url
    2. /users/{id} - delete a user with the id provided in the url

5. LIST all -  
GET request
    1. /laptops - Get/List all the laptops stored in the db
    2. /users - Get/List all the users along with their associated laptops

6. Other end points:
GET request:  /oops   --- this shows you what happens when your code throws an exception.

#

### Note :
### 1. /laptops APIs are defined in Laptop/LaptopController, 
### 2. /users API is defined in the User/UserController

# 
## Some helpful links:
[SpringBoot Entity](https://www.baeldung.com/jpa-entities)   
[SpringBoot OneToOne](https://www.baeldung.com/jpa-one-to-one)    

# Tutorials

ALL tutorials for cs309 will be in this repo.
- Each topic will be on a different branch.
- ~~GIT wiki will be used for documentation.~~

---
## Accessing tutorials 
You can access each tutorial by switching to its respective branch. To do so, you can use your terminal/Git bash. 

1. First, make sure you create a Personal Access Token, [details](https://docs.google.com/document/d/1F9ByRqWt5y2jzo04fn0pHOLoXhgp3EMEtRnIUH3_UhM/edit#heading=h.dvi4on3jn6fo)

2. Then, clone this repository

```bash
git clone https://git.las.iastate.edu/cs309/tutorials.git
```

3. Change the directory to the root of this repository  

```bash
cd </path/to/tutorials/directory>  
```

4. Next, switch to the desired branch. For example, to switch to "springboot_unit1_1_helloworld", use the following command:

```bash
git checkout springboot_unit1_1_helloworld
```

This should switch the local repository to the "springboot_unit1_1_helloworld" branch. For complete list of topics, check the section "Topics" below.

---

## Topics


### :book: UNIT-1

<!-- └── **Setup (TODO)**: 
 * [unit1_setup_guide](https://git.las.iastate.edu/cs309/tutorials/) `| Android Studio | IntelliJ | Postman |`
 -->
└── **Frontend**
* [android_unit1_1_helloworld](https://git.las.iastate.edu/cs309/tutorials/-/tree/android_unit1_1_helloworld) `A "hello world" example for testing environment setup`
* [android_unit1_2_intent](https://git.las.iastate.edu/cs309/tutorials/-/tree/android_unit1_2_intent) `A "counter" example for toying with intent (switch between activities)`
* [android_unit1_3_login_signup](https://git.las.iastate.edu/cs309/tutorials/-/tree/android_unit1_3_login_signup) `A "login & signup" example. Only an example of the interface`

└── **Backend**
*  [springboot_unit1_1_helloworld](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_1_helloworld) `Getting started with SpringBoot`
*  [springboot_unit1_2_hellopeople](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_2_hellopeople) `Sending and Receiving data using GET, POST, PUT and DELETE APIs`
*  [springboot_unit1_3_petclinic_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit1_3_petclinic_example) 

---

### :book: UNIT-2

└── **Setup**: 
 * [Setting up MySQL on the remote server
](https://docs.google.com/presentation/d/1uNB26IgH-g3DP01sUwuyE2eOBe_0C5W9ofvTet5tDDY/edit#slide=id.p1)
 * [Deploy .jar file to the server
](https://docs.google.com/presentation/d/1q5ODoDvphSkqv3GXGv_l2odI9tCJ3ht63SvVQ5pjC3o/edit#slide=id.p1)
 * [Connect SpringBoot to MySQL DB
](https://docs.google.com/presentation/d/1ZqOwobX1qySx1zVxj8a_CAs6RzQY4ygqc9Tl4NDlnh0/edit#slide=id.p)

└── **Frontend**
* [android_unit2_1_volley](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_1_volley) `example for: string / jsonObj / jsonArray / image  request calls`
* [android_unit2_2_volley](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_2_volley) `example for: jsonObj / jsonArray request calls with data parsed into TextView and ListView`
* [android_unit2_3_volley](https://git.las.iastate.edu/cs309/tutorials/-/tree/android_unit2_3_volley) `example for: string GET / jsonObj POST request calls`
*  [RoundTrip_(with_retrofit2_and_h2)](https://git.las.iastate.edu/cs309/tutorials/-/tree/RoundTrip_(with_retrofit2_and_h2)) `Uses Retrofit2 to load all the Trivias and posts new trivias by json body and path. Video link: https://youtu.be/eUPFmgp6FKk`

└── **Backend**
*  [springboot_unit2_1_onetoone  (uses H2 database)](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone) `Example one-to-one relation, H2 database usage and CRUDL using SpringBoot`
*  [springboot_unit2_1_onetoone_mysql](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_1_onetoone_mysql) `MySQL database connectivity using spring boot + one-to-one relation + CRUDL using SpringBoot`
*  [springboot_unit2_2_onetomany](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_2_onetomany) `Example one-to-many relations + querying using repository + CRUDL using Spring Boot`
*  [springboot_unit2_3_filerequests](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_3_filerequests) `Handling File requests/Multi-Part requests using SpringBoot`
*  [RoundTrip_(with_retrofit2_and_h2)](https://git.las.iastate.edu/cs309/tutorials/-/tree/RoundTrip_(with_retrofit2_and_h2)) `Adds new Trivia questions and Answers by path and body and displays all Trivias. Video link: https://youtu.be/CEiUCDWygD0. To see how to plug into this project into MySQL watch this: https://youtu.be/hyFo1_jryL0`

---

### :book: UNIT-3

└── **Setup**: 
 * [unit3_setup_guide](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit2_4_swagger_ui) `| Java Doc | Swagger API |`

└── **Frontend**
* [unit3_1_websockets](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit3_1_websockets) `Simple Implementation of websocket for both ends`
* [unit3_2_image_upload](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit3_2_image_handling) `Simple Implementation Mutipart file (image) upload for both ends`


└── **Backend**
*  [springboot_unit3_1_manytomany](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_1_manytomany) `Example many-to-many relations`
*  [springboot_unit3_2_complex_relations_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_2_complex_relations_example) `Example many-to-many relations + complex queries using repository + CRUDL using spring boot`
*  [springboot_unit3_3_manytomany_selfrelations](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_3_manytomany_selfrelations) `Self Relations among entities in Spring Boot`
* [unit3_1_websockets](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit3_1_websockets) `Simple Implementation of websocket for both ends`
* [unit3_2_websockets](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit3_2_websockets) `backend websocket with storing message`
* [unit3_2_image_upload](https://git.las.iastate.edu/cs309/tutorials/-/tree/unit3_2_image_handling) `Simple Implementation Mutipart file (image) upload for both ends`

---

### :book: UNIT-4

└── **Setup**: 
*  [unit4_setup_guide](https://git.las.iastate.edu/cs309/tutorials/-/tree/testing) `| Code Coverage Report |`
*  [cicd_example](https://git.las.iastate.edu/cs309/tutorials/-/tree/cicd_example) `| CI/CD |`

└── **Frontend**
* [testing](https://git.las.iastate.edu/cs309/tutorials/tree/testing) `testing examples`

└── **Backend**
*  [testing](https://git.las.iastate.edu/cs309/tutorials/-/tree/testing) `testing examples1`


---

### Legacy
* [android_unit2_4_Phase 1,2,3,4](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit2_4_phases) `Example to send requests using volley(to simple URL, JSON response, and Image)`
* [android_unit3_2_service_and_singleton](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_2_service_and_singleton) `Example to Create a singleton class for request queue, and a listner for responses`
*  [springboot_unit3_4_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/-/tree/springboot_unit3_4_mockito_testing)

* [android_unit3_1_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_1_mockito_testing) `Example to test android code with Mockito. UPDATE: Updated java to 11, gradle to 7.0.3, android sdk to 30 and fixed mockito dependencies - should work now`

* [android_tutorials_master](https://git.las.iastate.edu/cs309/tutorials/tree/android_tutorials_master) `Docs for Volley, CI/CD, Mockito and websocket`

*  [springboot_unit3_4_mockito_testing](https://git.las.iastate.edu/cs309/tutorials/tree/springboot_unit3_4_mockito_testing) `Example code to test a springboot application with mockito`
*  [android_unit3_2_service_and_singleton](https://git.las.iastate.edu/cs309/tutorials/tree/android_unit3_2_service_and_singleton) `Example code to test an android application with mockito. UPDATE: updated java to 11, gradle to 7.0.3, junit versions changed, tests fixed to work now with the latest version of Android Studio`
* [unit4_1_websockets](https://git.las.iastate.edu/cs309/tutorials/tree/unit4_1_websockets)

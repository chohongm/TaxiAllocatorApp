# TaxiAllocatorApp

APP DESIGN

1. What is this?

- An android app for allocating drivers to ride requesters.

2. What are the core logic/workflow of the app?

- A user can create an account as either rider or driver.
- A rider can create a ride request (All ride requests are displayed on the list. Both rider and driver can view the list.).
- Driver can accept any available ride request.

3. What tables/schema are used? (Acronyms: PK = primary key, AI: auto-increment, FK: foreign key)

- User

  - username (PK) | password | rider_id (FK) | driver_id (FK)

- Rider

  - rider_id (PK, AI) | username (FK) | request_id (FK)

- Driver

  - driver_id (PK, AI) | username (FK) | request_id (FK)

- RideRequest

  - request_id (PK, AI) | rider_id (FK) | request_timestamp | location | driver_id (FK) | accepted_timestamp | completed_timestamp

4. Briefly explain the reasoning behind the schema decision.
- Minimized redundancy by avoiding adding a column that can be referenced from another table.
- Used integer type identity for primary key to reduce database read/write operational cost.

5. What APIs are used and when are they used?
- Data operations layer APIs (This is the service level API class):
  - createUserAsRider: Creates user as rider; Inserts user and rider into the respective tables.
  - createUserAsDriver: Creates user as driver; Inserts user and driver into the respective tables.
  - createRideRequest: Creates a new ride request; Inserts a new ride request and updates the rider with the request info.
  - acceptRideRequest: Updates a ride request with the driver and accepted timestamp. Updates the driver with the request info.

- Database layer APIs (Internal/Not-public)
  - When a user creates an account:
    - insertUserAsRider: Inserts a new user into user table as a rider.
    - insertUserAsDriver: Inserts a new user into user table as a driver.
    - insertRider: Inserts a new rider into rider table.
    - insertDriver: Inserts a new driver into driver table.

  - When a rider creates a ride request: createRideRequest
    - insertRideRequest: Inserts a new ride request into ride request table. 
      (*Note: A new ride request is a request created by a rider which has yet been accepted by a driver.)
    - updateRider: Updates a rider by associating a requestId.

  - When a driver accepts a ride request: acceptRideRequest
    - updateDriver: Updates a driver by associating a requestId.
    - updateRideRequest: Updates a ride request by associating a driver and an accepted timestamp.

  - When the app displays a list of all existing ride requests to a user: 
    - getAllRideRequests: Gets the list of all ride requests sorted by request timestamp in descending order.

  - When above APIs need to read data:
    - getUser: Gets a user by userId.
    - getRider: Gets a driver by riderId.
    - getDriver: Gets a driver by driverId.
    - getRideRequest: Gets a ride request by requestId.

LANGUAGES & TOOLS USED

- Java for both frontend and backend implementation, Android Studio (v3.5.1; Latest released version on 2019-10-16), and SQLite for database (built-in for android studio).

DEMO VIDEO LINK

- https://youtu.be/UIYCMM6MMtw

FEATURE GOALS (O = Complete; X = Incomplete)
- Register account:
  - Parameters are username in email format, password, and rider/driver selection. (O)
  - An existing email cannot be used for creating a new account. (O)
- Login:
  - Use email and password to login. (O)
- Ride request list view: 
  - All existing ride requests should be displayed to any user. (O)
  - A listed ride request can either be in pending or accepted state. (O)
  - A ride request should display whether it is in pending or accepted state. (O)
  - A ride request should display both requested and accepted timestamps. (O)
  - Most recent ride request should be at the top. (O)
- Ride requesting:
  - A rider can create a request for a ride. (O)
  - Creating a request takes locaiton as parameter. (O)
  - The location should be less than equal to 100 letters in length. (O)
- Driver accepting:
  - A driver can accept a pending request. (O)
  - Driver can accept only one request at the same time. (O & X; There is a minor bug needs to be fixed.)

POINTS OF IMPROVEMENT

1. Need to transactionalize multiple write operations to avoid broken data scenarios.
2. Need to organize code structure better by using more constants. 
3. Find better way to send information from one activity to the next.
4. Discuss about adding redundancy to some tables in order to reduce data reads.

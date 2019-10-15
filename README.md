# TaxiAllocatorApp

APP DESIGN

1. What is this app for?

- An android app for allocating drivers to riders.

2. How does it work?

- A user can create an account as either rider or driver.
- A rider can create a ride request (All ride requests are displayed on the list. Both rider and driver can view the list.).
- Driver can accept any available ride request.

3. What tables/schema are used? (Acronyms: PK = primary key, AI: auto-increment, FK: foreign key)

User
username (PK, AI) | password | rider_id (FK) | driver_id (FK)

Rider
rider_id (PK, AI) | username (FK) | request_id (FK)

Driver
driver_id (PK, AI) | username (FK) | request_id (FK)

RideRequest
request_id (PK, AI) | rider_id (FK) | request_timestamp | location | driver_id (FK) | accepted_timestamp | completed_timestamp

4. Briefly explain the reasoning behind the schema decision.
- Minimized redundancy by avoiding adding a column that can be referenced from another table.
- Used integer type identity for primary key to reduce database read/write operational cost.

5. What APIs are used and when are they used?

When a user creates an account:
- insertUserAsRider: Inserts a new user into user table as a rider.
- insertUserAsDriver: Inserts a new user into user table as a driver.
- insertRider: Inserts a new rider into rider table.
- insertDriver: Inserts a new driver into driver table.

When a rider creates a ride request:
- insertRideRequest: Inserts a new ride request into ride request table. 
  (*Note: A new ride request is a request created by a rider which has yet been accepted by a driver.)
- updateRider: Updates a rider by associating a requestId.

When a driver accepts a ride request:
- updateDriver: Updates a driver by associating a requestId.
- updateRideRequest: Updates a ride request by associating a driver and an accepted timestamp.

When the app displays a list of all existing ride requests to a user.
- getAllRideRequests: Gets the list of all ride requests sorted by request timestamp in descending order.

When above APIs need to read data:
- getUser: Gets a user by userId.
- getRider: Gets a driver by riderId.
- getDriver: Gets a driver by driverId.
- getRideRequest: Gets a ride request by requestId.

LANGUAGES & TOOLS USED

- Java for both frontend and backend implementation, Android Studio, and SQLite for database (built-in in android studio).

DEMO VIDEO LINK

- Pending.

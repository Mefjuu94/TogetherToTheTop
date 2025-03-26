# Together To The Top ⛰️ (TTTT) -> project still ongoing!
# Adress to connect: http://13.60.233.120:8080

**Together To The Top (TTTT)** is a web application that allows users to search for and join walking tours, as well as share their own. Users can browse, create and manage walking tours, interact with other users and use the interactive map to plan their routes.

## Features

### 1. **For unauthenticated users:**
- **Registration:** User can register with validation.
- **Login:** Secure login system for registered users.
- **Retrieve Password:** Password recovery for people who forgot it.
- **About Project Section:** Project information.
- **Contact Section:** Contact details of the project author.
- **Privacy Policy:** privacy policy for people who want to know what data is collected.

### 2. **For authenticated users:**
- **Browse walking tour offers:** View a list of available walking tours.
- **Trip Details:** View detailed information about the selected tour. - **Download GPX File:** Download the GPX file for a specific walking tour.
- **Join a Tour:** Join a tour and add yourself to the participants list.
- **Add Comments:** Leave comments about the walking tour.
- **Browse Your Tours:** See a list of walking tours you are on.
- **Browse Other User's Tours:** See tours created by other users.
- **Browse My rate:** rates, and comments created by other users.
- **Search by Destination:** Find tours to a specific destination.
- **Withdraw from a Tour:** Leave a walking tour you are currently on.
- **Create Your Own Walking Tour:**
- Specify the starting point.
- Define the peaks and points of interest (at least one).
- Set the date of the tour.
- Specify the group size (open or private).
- Specify whether carpooling is available.
- Specify whether pets are allowed.
- Add a description of the tour.

- **Profile Management:**
- View and edit your profile (except for editing reviews).

- **Map Integration:**
- **Interactive Map:** View an interactive map using the Mapy.cz API implementation.

- **Points of Interest (POI):** Include coordinates of objects like parkings, schools, restaurations, ect. to define start/end points or waypoints for your trip.

- **Search Functionality:** Search for places, peaks, and cities by name.

- **Route Calculation:** Automatic calculation a route with estimated time and distance.

- **User Search:** Search for other users by their name.

## Technologies Used

- **PostgreSQL:** A database management system for storing user data, trips, and other information.

- **Spring MVC:** A Java framework for creating web applications with a Model-View-Controller architecture. View using the Thymeleaf template engine.
- **Spring Security:** For secure authentication and user management.

- **Hibernate:** ORM (Object-Relational Mapping) for interacting with the database.

- **Gradle:** A tool for automating builds.

- **JUnit:** A framework for unit testing Java code.

- **Mockito:** A framework for creating mocks used for unit testing.

- **Docker:** For containerization and deployment.

## Installation

To run the application locally, follow these steps:

1. **Clone the repository:**

```bash
git clone https://github.com/Mefjuu94/TogetherToTheTop.git
cd TogetherToTheTop
```

2. **Configure the database:**

- Make sure you have **PostgreSQL** installed.
- Create a database in PostgreSQL and configure the connection settings in the `application.properties` file.

3. **Get the API KEYS:**

- Get API keys from https://leafletjs.com and https://www.thunderforest.com/docs/apikeys/ .
- Create a javascript file named "apiKeys.js" in
  
```bash
src/main/resources/static/js/apiKeys.js
```
Now map should work properly.

4. **Build the project using Gradle:**

```bash
./gradlew build
```

4. **Run the application:**

```bash
./gradlew bootRun
```

5. Open a browser and visit [http://localhost:8080](http://localhost:8080).

## Usage

- **Sign up** to create a new account or **log in** if you already have one.

- Once logged in, you can browse hiking tours, create your own, or join an existing one.

- Use the interactive map to search for destinations and calculate routes for your hiking tours.

- Engage with the community by adding comments, reviewing hikes, and interacting with other hikers.

## Contribution

If you would like to contribute to the project, feel free to fork the repository and submit a pull request with your improvements.

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature/your-feature`).
6. Create a new pull request.

## License

This project is closed-source and is not available for redistribution or modification without the author's permission.

---

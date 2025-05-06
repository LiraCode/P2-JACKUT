# Jackut - Social Network System

**Jackut** is a simple social network application, designed to manage users, friends, and messages, with robust functionality and data persistence using serialization in **Java**.

Evaluation project for the **Object-Oriented Programming (P2)** course at the **Federal University of Alagoas**, taught by Professor ***Mario Hozano***.

---

## Main Features

1. **User Management**
    - Account creation with login, password, and name.
    - User authentication with session control.
    - Profile editing, allowing modification of attributes such as name and password.

2. **Friendship Management**
    - Adding friends with sending and accepting friendship requests.
    - Friends list and verification of existing friendships.

3. **Messages and Posts**
    - Sending messages from one user to another.
    - Reading messages in order of arrival (FIFO).

4. **Data Persistence**
    - Saving and loading system data using serialized files (`usuarios.ser, comunidades.ser`).

5. **Testing with EasyAccept**
    - Integration with the EasyAccept framework for acceptance testing.

6. **Relationship Management**
    - Fan-idol relationships: users can add others as idols and become their fans
    - Crush relationships: users can add others as crushes with mutual notification
    - Enemy relationships: users can mark others as enemies, blocking interactions

7. **Community Management**
    - Creation and management of communities
    - Joining and leaving communities
    - Community messaging system

---
## Milestones

***02-04-2025 -*** **Milestone 1 | (User Stories 1 to 4).**
- Basic user management
- Friendship functionality
- Message system
- Data persistence

***05-05-2025 -*** **Milestone 2 | (User Stories 5 to 9).**
- Community creation and management
- Advanced relationship types (fan-idol, crush, enemy)
- Community messaging
- remove user and any relationships that involve the user

---


## Design Patterns Used

- **Facade Pattern**: Implemented in the Facade class to provide a simplified interface
- **Repository Pattern**: Used for data access and persistence
- **Service Pattern**: Implemented to encapsulate business logic
- **Singleton Pattern**: Implicitly used in repositories
- **Strategy Pattern**: Used for different relationship types implementation

---

## UML Class Diagram

A comprehensive UML class diagram is available in the project documentation, showing the relationships between the main classes of the system.

---

## Project Structure

```plaintext
br/ufal/ic/p2/jackut/
├── facade/
│   └── Facade.java         # Main class for system operations
├── models/
│   ├── User.java           # Class representing users
│   ├── Message.java        # Class encapsulating messages sent between users
│   ├── Friends.java        # Class managing friendship relationships
│   └── Community.java      # Class representing communities
├── repositories/
│   ├── UserRepository.java # Repository for user data management
│   └── CommunityRepository.java # Repository for community data management
├── services/
│   ├── AuthService.java    # Service for authentication operations
│   ├── UserService.java    # Service for user operations
│   ├── FriendshipService.java # Service for friendship operations
│   ├── MessageService.java # Service for message operations
│   ├── RelationshipService.java # Service for other relationship types
│   ├── CommunityService.java # Service for community operations
│   └── SystemService.java  # Service for system operations
├── exceptions/             # Package containing custom exceptions
├── Main.java               # Main class for running tests
├── tests/                  # Folder with test files for EasyAccept
├── usuarios.ser            # Serialized file containing persisted user data (generated at runtime)
└── comunidades.ser         # Serialized file containing persisted community data (generated at runtime)
```
Obs: Tested with openJDK 24 in IntelliJ IDEA 2024.3.2.1 - Info 05/04/25


---

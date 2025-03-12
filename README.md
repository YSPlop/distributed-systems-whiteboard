# Distributed Whiteboard Application
A collaborative real-time whiteboard application built with Java RMI for COMP90015 Distributed Systems.

## Overview
This distributed whiteboard application enables multiple users to collaborate in real-time through a shared drawing canvas. It follows a client-server architecture using Java RMI (Remote Method Invocation) for communication between components.

## Key Features
- Real-time collaborative drawing workspace
- Multi-user support with role-based access (Manager/User)
- Chat functionality for communication between users
- Drawing tools with color selection
- File operations (New/Open/Save/Close)
- User management (kick users, end session)
- Scrollable message and user lists
- Synchronized state across all clients

## System Architecture
- **Central Server**: Manages all client connections, commands, chat history and user information
- **Client-Server Communication**: Uses Java RMI for remote object interaction
- **Threading**: Multi-threaded design for concurrent processing of:
  - Drawing operations
  - Chat messages 
  - User management

## Technical Implementation
### Server Components
- RMI Registry for remote object lookup
- Remote objects for:
  - User management (IRemoteUserList)
  - Command processing (RemoteCP)
  - Message handling

### Client Components
- GUI interface for whiteboard and chat
- Three concurrent threads:
  - DrawingThread: Handles drawing operations
  - MessengerThread: Manages chat functionality  
  - UserThread: Updates user list

### Synchronization
- Uses both explicit locks (ReentrantLock) and implicit synchronization
- Prevents race conditions in multi-threaded environment
- Different strategies for:
  - Drawing operations (immediate local update, then server commit)
  - Messages and user lists (server commit, then poll for updates)

## System Requirements
- Java Runtime Environment (JRE) 8 or higher
- Network connectivity
- Operating System: Windows/Mac/Linux

## Installation & Setup
1. Download the project files
2. Navigate to the artifacts directory:
```bash
cd out/artifacts
```
## Running the Application

### 1. Start the Server
```bash
java -cp . -jar server_jar/server.jar 127.0.0.1 5859
```

Parameters:
- `127.0.0.1`: Server IP address
- `5859`: Port number

### 2. Start Manager Client (First Client)
```bash
java -cp . -jar client_jar/client.jar 127.0.0.1 5859 userA
```


Parameters:
- `127.0.0.1`: Server IP address
- `5859`: Port number
- `username`: Unique identifier for the client

## Manager Features
- Create/Open/Save/Close whiteboard
- Kick users from session
- End entire session
- All drawing capabilities

## User Features
- Join existing whiteboard sessions
- Draw on shared canvas
- Chat with other users
- View connected users list

## Project Structure
project/
├── out/
│ └── artifacts/
│ ├── client_jar/
│ │ └── client.jar
│ └── server_jar/
│ └── server.jar
├── src/
│ ├── client/
│ ├── remote/
│ └── server/


## Communication Protocol
Uses Java RMI (Remote Method Invocation) for:
- Remote object access
- Method invocation across JVM boundaries
- Distributed object communication

## Advantages of Design
1. Dedicated resources per client
2. Centralized server management
3. Enhanced security through server-controlled access
4. Easy maintenance and updates
5. Efficient for small to medium user groups
6. Quick state synchronization

## Troubleshooting
1. Ensure server is running before connecting clients
2. Verify correct IP address and port number
3. Check network connectivity
4. Ensure unique usernames for each client
5. Verify Java installation

## Authors
- Yukash Sivaraj (1054297)
- Xiaocong Zhang (1292460)

## License
Copyright © 2022 - All rights reserved

## Acknowledgments
Developed as part of COMP90015 Distributed Systems course project
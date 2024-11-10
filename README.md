# 💬 ServerChat

**ServerChat** is a terminal-based chat room application that allows multiple users to connect locally, enter a username, and start chatting with each other in real-time. Messages are visible to all users in the chat room, creating a shared conversation space with fun commands and interactive features.

## 📑 Table of Contents 

- [About the Project](#about-the-project)
- [Features](#features)
- [Commands](#commands)
- [How to Connect](#how-to-connect)
- [Planned Features](#planned-features)

## About the Project

ServerChat is a simple, terminal-based chat server built to foster quick and direct communication among users on the same network. Users connect through `localhost` on a specific port, choose a username, and instantly gain access to the chat room.

This project is ideal for anyone interested in networking fundamentals and real-time communication using sockets in Java.

## ✨ Features

- **Real-Time Chat**: All users can send and receive messages instantly.
- **User Commands**: Special commands enhance interaction and functionality.
- **User List Display**: Users can view the list of all connected users.
- **Fun Commands**: Share a random joke, send uppercase messages, and more!

## 🔧 Commands

Here’s a list of available commands:

- **/ls**: View the list of connected users.
- **/joke**: Send a random joke.
- **/scream**: Send your message in uppercase.
- **/quit**: Disconnect from the chat server.

> *Example:* Type `/joke` to send a random joke directly in the chat room.

## 🚀 How to Connect

1. **Run the Server**: Start the chat server locally on a specified port.
2. **Join the Server**: Open a terminal and connect to the server using `localhost` and the port number.
3. **Enter Your Name**: You will be prompted to enter a username, which will appear with your messages in the chat.
4. **Chat**: Start sending messages! Use commands (like `/ls` or `/joke`) for additional interaction.

## 🔮 Planned Features

- **Password Protection**: Add a password to restrict chat room access.
- **Whisper Command**: Send private messages to specific users.
- **Moderation Tools**: Implement features to allow moderators to manage the chat.

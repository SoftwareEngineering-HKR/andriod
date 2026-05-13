# Smart Home Android App

Android application for the Smart Home System project developed as part of the Software Engineering course [DA330B](https://www.hkr.se/kurs/da330b/) at Kristianstad University.

The app allows users to monitor and control smart home devices through a modern mobile interface built with Kotlin and Jetpack Compose.

## Related Repositories

This project is part of a larger smart home ecosystem consisting of multiple repositories.

- [Android Application](https://github.com/SoftwareEngineering-HKR/andriod)
- [Backend Service](https://github.com/SoftwareEngineering-HKR/backend)
- [Web Application](https://github.com/SoftwareEngineering-HKR/frontend)
- [Device Integration](https://github.com/SoftwareEngineering-HKR/IOT)


## Features

- Smart home device management
- Real-time device status updates
- WebSocket communication (persistent connection)
- User authentication
- Modern UI built with Jetpack Compose
- Material Design based interface


## Tech Stack

- Kotlin
- Jetpack Compose
- Android Studio
- Gradle
- Material Design 3
- WebSockets


## Getting Started

### Requirements

- Android Studio (latest stable version recommended)
with Android SDK installed
- JDK 11 (as required by project Gradle configuration)
- Android Emulator or physical Android device

> **Project configuration note:**
> - Minimum SDK: 24
> - Target SDK: 36
> - Compile SDK: 36
> - Kotlin JVM target: 11


### Running the Application
1. Clone the repository:

```bash
git clone https://github.com/SoftwareEngineering-HKR/andriod.git
```

2. Open the project in Android Studio

3. Allow Gradle sync to complete

4. Run the application using:
   - Android Emulator
   - or a connected physical Android device


## Backend Connection

The application requires the backend service to be running in order to function correctly.

Communication between the app and backend is handled through WebSocket communication (persistent connection).


## Screenshots

Screenshots and demo material will be added later in development.


## License

This project is licensed under the GNU General Public License v3.0.

See the [LICENSE](LICENSE) file for more information.

# Location-Access-Android-Sample
 A fully functional test App prepared for Adyen, Netherlands

Android Foursquare Location App

# Overview
This repository contains a solution to take-home technical test, featuring an Android application (the app module) that integrates with the Foursquare Places API. The app retrieves and displays a list of nearby venues based on the user's current location, showcasing modern Android development practices with a clean architecture.

# Business Logic
The app module implements a location-based venue browsing experience with the following core functionality:

Location Fetching: The app retrieves the user's current location using Google Play Services Location API, prompting for permission and location enablement if necessary.
Venue Retrieval: It queries the Foursquare Places API with the user's latitude and longitude to fetch a list of nearby venues.
Minimal UI: Displays venue details (name, categories, distance, address, timezone) in a clean, user-friendly list, with a fallback to a "Location Required" screen if location access is unavailable.
Error Handling: Manages network errors, permission denials, and disabled location services gracefully, guiding users to enable location settings when needed.
The business logic is structured using the Model-View-ViewModel (MVVM) pattern within a Clean Architecture framework:

Presentation Layer: Handles UI state and user interactions.
Domain Layer: Encapsulates use cases for fetching venues.
Data Layer: Manages API calls and data transformation.
Key Features
Reactive State Management: Updates the UI based on location and venue data changes in real-time.
Permission Handling: Requests and verifies location permissions seamlessly.
Minimal Change: While not directly applicable to the app, the underlying architecture supports extensibility (e.g., for future payment features like the cash register).
Technologies Used
The app leverages the latest Android technologies to ensure a robust, maintainable, and modern implementation:

Jetpack Compose: Declarative UI toolkit for building a responsive and minimal venue list interface.
Hilt: Dependency injection framework for managing dependencies across layers.
Kotlin Coroutines: Asynchronous programming for API calls, location fetching, and state updates.
Flow and StateFlow: Reactive streams for propagating state changes from the ViewModel to the UI.
Retrofit: HTTP client for interacting with the Foursquare Places API.
Moshi: JSON parsing library for deserializing API responses into Kotlin data classes.
OkHttp: Networking layer with interceptors for logging and debugging API requests.
Google Play Services Location: Provides accurate location data with minimal setup.
Coil: Image loading library for potential future enhancements (e.g., venue images).
JUnit 5: Unit testing framework for verifying business logic across layers.
MockK: Mocking library for isolating dependencies in unit tests.
Kotlin: Primary language, leveraging its modern features like coroutines and extension functions.
Architecture
The app follows a Clean Architecture approach:

Presentation Layer:
MainActivity: Single activity coordinating location permissions and UI setup.
VenueScreen: Stateless Compose UI displaying venues.
LocationRequiredScreen: Prompts users to enable location.
VenueViewModel: Manages UI state with StateFlow.
Domain Layer:
GetNearbyPlacesUseCase: Encapsulates venue-fetching logic.
NearbyPlace & NearbyPlaceLocation: Domain models.
Data Layer:
NearbyPlacesRepository: Bridges remote data source and domain.
NearbyPlacesRemoteDataSource: Fetches data from the Foursquare API.
NearbyPlacesMapper: Transforms API models to domain models.
PlacesService: Retrofit interface for API calls.
Setup
Clone the Repository: git clone <repository-url>
Add API Key: Insert your Foursquare API key into app/build.gradle.kts as a BuildConfig variable (API_KEY).
Build and Run: Open in Android Studio, sync Gradle, and run on an emulator or device (API 30+ recommended).
Testing
Unit tests cover key components:

ViewModel: Verifies state transitions (Loading, Success, Error).
UseCase: Ensures correct venue data retrieval.
Repository: Validates data mapping and flow.
RemoteDataSource: Confirms API response handling.
Run tests with: ./gradlew test

Notes
Built with the latest stable Android libraries and Kotlin features as of February 2025.
Focuses on quality over quantity, adhering to SOLID principles and modern Android best practices.

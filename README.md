# GitHub Repository Search App

This project is a mobile application that allows users to search through GitHub repositories, view repository details, and explore contributor profiles. The application is built using **MVVM architecture** with **Clean Architecture** principles and leverages **Jetpack Compose** for the UI.

## Features

- **Home Screen:**
  - Search bar to search for GitHub repositories using the GitHub API.
  - Displays search results in a paginated list, limiting results to 10 items per page.
  - Clicking on a repository navigates to the detailed view.

- **Repository Details Screen:**
  - Shows detailed information about the selected repository including:
    - Repository Image
    - Name
    - Project Link (opens in WebView)
    - Description
    - Contributors list
  - Clicking on a contributor displays their repositories.

- **Offline Support:**
  - Caches the first 15 search results for offline access using **Room Database**.

## Technology Stack

- **Jetpack Compose** for the UI.
- **MVVM Architecture** combined with **Clean Architecture** for better scalability and separation of concerns.
- **Coroutines** for managing background threads and asynchronous tasks.
- **Room** for local data caching and offline access.
- **Retrofit** for networking and API calls to GitHub.
- **LiveData** to observe and update UI based on data changes.

## Project Structure

The project follows Clean Architecture, with separation of concerns into different layers:

1. **Domain Layer**: Business logic and entities.
2. **Data Layer**: Repository pattern, managing data from both remote (GitHub API) and local (Room DB) sources.
3. **Presentation Layer**: ViewModels, UI components built with Jetpack Compose, and LiveData.

## How to Run the Project

1. Clone this repository:
   ```bash
  https://github.com/babuakash68/ComposeFirstApp.git

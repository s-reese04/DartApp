# 🎯 Countable – Dart App

![Android](https://img.shields.io/badge/Platform-Android-brightgreen?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)
![Status](https://img.shields.io/badge/Status-In%20Development-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

A clean and intuitive Android dart scoring app built with Kotlin. Countable lets you play 301 with multiple players, features a fully interactive dartboard, and automatically tracks scores round by round.


##  Features

-  **Interactive Dartboard** — tap directly on the board to register throws; all 20 segments, triple ring, double ring, bull and bullseye are fully clickable
-  **Multi-Player Support** — configure 1–10 players before the game starts; switch between players with a single tap
-  **Automatic Player Rotation** — after 3 throws the next player is activated automatically
-  **Double & Triple Toggles** — activate Double or Triple mode via switches before a throw; they reset automatically after each scored segment
-  **301 Game Mode** — every player starts at 301 and counts down; first to reach 0 wins
-  **Reset per Player** — reset the current player's score individually at any time
-  **Setup Screen** — dedicated homescreen to configure player count with a slider before the game begins


##  Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android SDK 26+
- Kotlin 1.9+

### Installation

1. Clone the repository
   ```bash
   git clone https://github.com/yourusername/countable-dart-app.git
   ```
2. Open the project in Android Studio
3. Let Gradle sync finish
4. Run on a physical device or emulator (API 26+)

---

##  How to Play

1. **Launch the app** — the setup screen appears
2. **Select player count** using the slider (1–10 players)
3. **Press Start** to begin the game
4. **Tap a segment** on the dartboard to register a throw
   - Optionally activate **Double** or **Triple** before throwing
   - After **3 throws** the next player's turn begins automatically
5. **Scores count down from 301** — the first player to reach 0 wins


##  Roadmap

- [ ] Checkout suggestions (e.g. which combination reaches exactly 0)
- [ ] Win detection and game-over screen
- [ ] Switch between Dartboard and Calculator View
- [ ] Sound effects on throw
- [ ] Game history / statistics
- [ ] Dark & light theme toggle

---



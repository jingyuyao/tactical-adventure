# Set up
Required dev tools:
- JDK 7+
- Intellij Idea
- Android SDK

## Android set up:
1. Download commandline tools from https://developer.android.com/studio/index.html#downloads
2. Unpack it to somewhere like $HOME/Documents/android
3. Create an environment variable that points to the previous location by adding this line to .profile
    `export ANDROID_HOME=$HOME/Documents/android` (need to re-login for it to take effect)
4. Use $ANDROID_HOME/tools/android to download the latest platform, platform-tools and build-tools

## Import & run
Follow this guide for Intellij Idea to import and run the project:
https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA

## Additional quirks:
- Make sure "Android Support" plugin is enabled for Intellij Idea
- Set the SDK for the android module to Android SDK in Project Structure...->Modules->android
- For your sanity don't use the Android emulator and run it on a real phone instead
- Set Java language level to 6 in project settings since that is what libgdx likes

# Project structure & guidelines
The game follows MVC and uses the command pattern to communicate between different components.

## Models
- All model classes should not reference controllers or views.
- All state change methods should be package private to consolidate logic within models package
- Receive commands via method invocation from controllers

## Views
- Only views should hold references to assets
- Subscribe to commands from models
- Set up controllers upon view creation (hum... maybe this needs to be factored out)

## Controllers
- Listen to user input
- Issue commands to models
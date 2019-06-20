## EarthQuake Watcher [![Pull Requests Welcome](https://img.shields.io/badge/PRs-welcome-red.svg?style=flat)](http://makeapullrequest.com) ![License](https://img.shields.io/github/license/mayokunthefirst/EarthQuake-Watcher.svg)

## Project Overview
EarthQuake Watcher is an Android application built using the [Google Maps API](https://developers.google.com/maps/documentation/) and fetches data from the [USGS API](https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php). This USGS API which is in the [GeoJSON](https://geojson.org/) format allows for encoding variety of geographic data structures. EarthQuake Watcher leverages on this API to allow users keep track and monitor earthquake events occuring around the world. EarthQuake Watcher presents data gotten from the USGS API on the Google Map for the user to interact with.

## Project Demo
<p align="center"><img src="https://github.com/mayokunthefirst/EarthQuake-Watcher/blob/master/gif/20190602_165709.gif"></a></p> 

## Getting Started
You will need to have a Google Map API from the [Google Developer Console](https://console.developers.google.com/google/maps-apis) to work with Google Maps. If you don’t already have an account, you will need to create one and also create a new project that uses the Map SDK for Android. After creating the project, create a new API key for that project. Use your API key as the value for _google_maps_key_ in the [google_maps_api.xml](https://github.com/mayokunthefirst/EarthQuake-Watcher/blob/master/app/src/debug/res/values/google_maps_api.xml) file under the values directory. Add the Package Name and the SHA-1 certificate fingerprint to the project.

<p align="center"><img src="https://user-images.githubusercontent.com/29807085/58762911-a2fcdc80-854c-11e9-8c90-006fa5ce9f93.png"></a></p> 

Alternatively, follow the directions in [this link](https://developers.google.com/maps/documentation/android/start#get-key) to get started.


## Prerequisites
* Android Studio IDE 3.0+
* Android SDK v28
* Gradle 5.1.1


## Libraries
* [Volley](https://developer.android.com/training/volley/index.html)


## Installing

- Fork the repository. 
- Clone the repo using the terminal command. Make sure you replace `username` with your GitHub username.
```bash
git clone https://github.com/username/EarthQuake-Watcher.git 
```


## Setting up EarthQuake Watcher
1. Download the EarthQuake Watcher project source. You can do this either by forking and cloning the repository (recommended if you plan on pushing changes) or by downloading it as a ZIP file and extracting it.

2. Open Android Studio, you will see a Welcome to Android window. Under Quick Start, select _Import Project (Eclipse ADT, Gradle, etc.)_

3. Navigate to the directory where you saved the android project, select the root folder of the project (the folder named "EarthQuake-Watcher"), and hit OK. Android Studio should now begin building the project with Gradle.

4. Once this process is complete and Android Studio opens, check the Console for any build errors.

  - _Note:_ If you receive a Gradle sync error titled, "failed to find ...", you should click on the link below the error message (if available) that says _Install missing platform(s) and sync project_ and allow Android studio to fetch you what is missing.

5. Once all build errors have been resolved, you should be all set to build the app and test it.

6. Follow the instructions in the *Getting Started* section of this project

7. To Build the app, go to _Build>Make Project_ (or alternatively press the Make Project icon in the toolbar).

8. If the app was built successfully, you can test it by running it on either a real device or an emulated one by going to _Run>Run 'app'_ or pressing the Run icon in the toolbar.

## Contributing

Contributions are always welcome. Simply fork the project and make a Pull Request.


## Author

* Mayokun Adeniyi   ![Twitter](https://img.shields.io/twitter/follow/mayokunadeniyi.svg?style=social)

## License
Licensed under the MIT license.




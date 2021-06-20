# gnss-collector (Android mobile app)
![Uploading app.gif…]()
gnss-collector builds on the [GNSSLogger project](https://github.com/google/gps-measurement-tools/tree/master/GNSSLogger) in order to collect raw GNSS data.

### App
This application is built using Kotlin and Dagger

### Database
Data is sent from the application to a cloud firestore database in real time. 

### Map
The current base map comes from Mapbox. 

## Requirements

- public_mapbox_key
- secret_mapbox_key
- Android studio
- Google play services json (firebase)

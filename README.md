<h1>GNSS Collector Android App</h1>

<h2>Description</h2>
The satellite constellation above our heads and floating in space, provide us with rich and often missing data on positioning, navigation, and timing services on a macro and micro scale. This application provides an easy to use interface to collect raw GNSS data which can then be further analysed using our <a href="https://github.com/Indicative-Data-Science/gnssmapper"> gnssmapper </a> python package.
</br></br>
This project can 
builds on the <a href="https://github.com/google/gps-measurement-tools"> Google GNSS measurement-tools app </a> in order to collect raw GNSS data.

<h2>Features</h2>
The current base map comes from Mapbox.

<h2>Development Environment</h2>
The app is written entirely in Kotlin and uses the Gradle build system.

<h2>Firebase</h2>
The app makes considerable use of the following Firebase components:

<ul>
  <li> <a href=""> Cloud firestore </a> stores all of our GNSS data and provides realtime data batching. </li>
  <li> <a href=""> Firebase remote config </a> enables in-app constraints to be managed.</li>
</ul>

<h2>Mapbox</h2>
The app makes considerable use of the following Mapbox components:
<ul>
  <li> Firestore database </li>
  <li> Firebase remote config </li>
  <li> Firebase auth </li>
</ul>

<h2>Running the app</h2>
<h3> Distribution </h3>
You can download a beta release of the app through our early testing sign up list.
<h3> Building locally </h3>
In order to run the app locally you will need the declare the following things:
<ul>
  <li> public_mapbox_key </li>
  <li> secret_mapbox_key </li>
  <li> Android studio </li>
  <li> Firebase account with firestore database </li>
  <li> Google play services json file </li>
</ul>

## How to configure

* Install OBS version 27.2.2
* Install [OBS Websocket plugin](https://github.com/obsproject/obs-websocket) version 4.9.1
* Install [this node module](https://github.com/obs-websocket-community-projects/obs-websocket-js) to connect to obs websockets. Make sure to install version 4.0.3.
* Open OBS
* Do `npm install` and `npm start` to load the slides locally. You should see a notification about a successful connection to the OBS websocket plugin. If you set a different password when installing the plugin you will need to update it in `plugin/obs/obs.js` on the line where the connection is started.
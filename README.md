![](README_assets/banner.png)
# IBM Ready App for Telecommunications

### Overview

IBM Ready App for Telecommunications demonstrates a new genre of mobile service provider where plans are controlled by the end user and not limited to a few choices. These dynamic service providers are starting to emerge all around the world. The app empowers the customer to control their mobile voice, text, and data plan while empowering the service provider to provide the right offers at the right time.
Please visit this [blog](https://developer.ibm.com/code/open/projects/ibm-ready-app-for-telecommunications/) for details on the business case.

### Setup Instructions

1. Download the adapters - UserLoginAdapter and TelcoUserAdapter. Build the adapters and deploy to server. For more details on building adapter please refer to this [link](https://mobilefirstplatform.ibmcloud.com/tutorials/ru/foundation/8.0/adapters/creating-adapters/#build-and-deploy-adapters)
2. Register the application TelcoReadyAppAndroid in Mobilefirst admin console using the following details


	Application Name : TelcoReadyAppAndroid	

	Package : com.ibm.mil.readyapps.telco

	version : 1.0

3. Download the TelcoReadyAppAndroid and import in Android Studio. Edit the mfpclient.properties and update wlServerHost and wlServerPort values to point to the MFP server
4.  Build and run the app on emulator or actual device.
5.  On the login screen, please enter the username and password same for example john/john for the authentication to work.
6.  The app shows up the base plan and the additional packs that are available as add-on
7.  As the add-ons are selected by the user, the base plan is updated and the same information is sent to the backend (dummy backend in this case) by the TelcoUserAdaper.
8.  Analytics data is captured throughout the app. So, if analytics service is configured for the MFP server, custom charts can be drawn to showcase the customer usage patterns.

### Points to note
1. If you receive "Error:com.android.builder.internal.aapt.AaptException: Failed to crunch file" when building the app on android studio on windows, this might be because of the 240 character limitation. Please try moving the project up the project file path.
2. For the hotspot to work, please ensure GPS is on in the device and the current location is marked. 


### License
IBM Ready App for Telecommunications is available under the IBM Ready Apps License Agreement. See the [License file](https://github.com/IBM-MIL/IBM-Ready-App-for-Telecommunications/blob/master/License.txt) for more details.

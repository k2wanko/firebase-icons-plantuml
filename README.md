# Firebase PlantUML Icons

## Examples

### activity

```puml
@startuml
left to right direction
!define FirebasePuml https://raw.githubusercontent.com/k2wanko/firebase-icons-plantuml/master/plantuml
!includeurl FirebasePuml/FirebaseCommon.puml
!includeurl FirebasePuml/FirebaseAll.puml

actor "Person" as personAlias
Firestore(db, "Database", "Document Database")
Functions(func, "onCreate", "node8")
Analytics(ga, "Analytics", "")
PerformanceMonitoring(monitor, "Performance Monitoring", "")


personAlias --> db
db --> func: firestore.create
func --> personAlias: push notification

personAlias --> ga
personAlias --> monitor
@enduml
```

![activity.svg](./assets/activity/activity.svg)

### sequence

```puml
@startuml
!define FirebasePuml https://raw.githubusercontent.com/k2wanko/firebase-icons-plantuml/master/plantuml
!includeurl FirebasePuml/FirebaseCommon.puml
!includeurl FirebasePuml/FirebaseAll.puml

actor "Person" as personAlias
FirestoreParticipant(db, "Database", "Document Database")
FunctionsParticipant(func, "onCreate", "node8")

personAlias --> db
db --> func: firestore.create
func --> personAlias: push notification
@enduml
```

![sequence.svg](./assets/sequence/sequence.svg)

# Icons
<!-- icons-begin -->
|  Product  |  Icon  | Rectangle | Participant |
| --------- | ------ | --------- | ----------- |
| Firebase Dynamic Links | ![](./plantuml/Firebase%20Dynamic%20Links/DynamicLinks.png) | DynamicLinks(alias, "", "") | DynamicLinksParticipant(alias, "", "") |
| Cloud Firestore | ![](./plantuml/Cloud%20Firestore/Firestore.png) | Firestore(alias, "", "") | FirestoreParticipant(alias, "", "") |
| Firebase Predictions | ![](./plantuml/Firebase%20Predictions/Predictions.png) | Predictions(alias, "", "") | PredictionsParticipant(alias, "", "") |
| ML Kit for Firebase | ![](./plantuml/ML%20Kit%20for%20Firebase/MLKit.png) | MLKit(alias, "", "") | MLKitParticipant(alias, "", "") |
| Firebase App Indexing | ![](./plantuml/Firebase%20App%20Indexing/AppIndexing.png) | AppIndexing(alias, "", "") | AppIndexingParticipant(alias, "", "") |
| Firebase Realtime Database | ![](./plantuml/Firebase%20Realtime%20Database/RealtimeDatabase.png) | RealtimeDatabase(alias, "", "") | RealtimeDatabaseParticipant(alias, "", "") |
| Firebase Crash Reporting | ![](./plantuml/Firebase%20Crash%20Reporting/CrashReporting.png) | CrashReporting(alias, "", "") | CrashReportingParticipant(alias, "", "") |
| Cloud Storage for Firebase | ![](./plantuml/Cloud%20Storage%20for%20Firebase/Storage.png) | Storage(alias, "", "") | StorageParticipant(alias, "", "") |
| Google Analytics | ![](./plantuml/Google%20Analytics/Analytics.png) | Analytics(alias, "", "") | AnalyticsParticipant(alias, "", "") |
| Firebase Remote Config | ![](./plantuml/Firebase%20Remote%20Config/RemoteConfig.png) | RemoteConfig(alias, "", "") | RemoteConfigParticipant(alias, "", "") |
| Firebase App Distribution | ![](./plantuml/Firebase%20App%20Distribution/AppDistribution.png) | AppDistribution(alias, "", "") | AppDistributionParticipant(alias, "", "") |
| Firebase Test Lab | ![](./plantuml/Firebase%20Test%20Lab/TestLab.png) | TestLab(alias, "", "") | TestLabParticipant(alias, "", "") |
| Firebase Authentication | ![](./plantuml/Firebase%20Authentication/Authentication.png) | Authentication(alias, "", "") | AuthenticationParticipant(alias, "", "") |
| Firebase Performance Monitoring | ![](./plantuml/Firebase%20Performance%20Monitoring/PerformanceMonitoring.png) | PerformanceMonitoring(alias, "", "") | PerformanceMonitoringParticipant(alias, "", "") |
| Firebase AB Testing | ![](./plantuml/Firebase%20AB%20Testing/ABTesting.png) | ABTesting(alias, "", "") | ABTestingParticipant(alias, "", "") |
| Firebase Invites | ![](./plantuml/Firebase%20Invites/Invites.png) | Invites(alias, "", "") | InvitesParticipant(alias, "", "") |
| Firebase Extensions | ![](./plantuml/Firebase%20Extensions/Extensions.png) | Extensions(alias, "", "") | ExtensionsParticipant(alias, "", "") |
| Cloud Functions for Firebase | ![](./plantuml/Cloud%20Functions%20for%20Firebase/Functions.png) | Functions(alias, "", "") | FunctionsParticipant(alias, "", "") |
| Firebase Crashlytics | ![](./plantuml/Firebase%20Crashlytics/Crashlytics.png) | Crashlytics(alias, "", "") | CrashlyticsParticipant(alias, "", "") |
| Firebase Hosting | ![](./plantuml/Firebase%20Hosting/Hosting.png) | Hosting(alias, "", "") | HostingParticipant(alias, "", "") |
| Firebase Cloud Messaging | ![](./plantuml/Firebase%20Cloud%20Messaging/Messaging.png) | Messaging(alias, "", "") | MessagingParticipant(alias, "", "") |
| Firebase In-App Messaging | ![](./plantuml/Firebase%20In-App%20Messaging/InAppMessaging.png) | InAppMessaging(alias, "", "") | InAppMessagingParticipant(alias, "", "") |
<!-- icons-end -->

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
| Firebase Dynamic Links | ![](./plantuml/Firebase Dynamic Links/DynamicLinks.png) | DynamicLinks(alias, "", "") | DynamicLinksParticipant(alias, "", "") |
| Cloud Firestore | ![](./plantuml/Cloud Firestore/Firestore.png) | Firestore(alias, "", "") | FirestoreParticipant(alias, "", "") |
| Firebase Predictions | ![](./plantuml/Firebase Predictions/Predictions.png) | Predictions(alias, "", "") | PredictionsParticipant(alias, "", "") |
| ML Kit for Firebase | ![](./plantuml/ML Kit for Firebase/MLKit.png) | MLKit(alias, "", "") | MLKitParticipant(alias, "", "") |
| Firebase App Indexing | ![](./plantuml/Firebase App Indexing/AppIndexing.png) | AppIndexing(alias, "", "") | AppIndexingParticipant(alias, "", "") |
| Firebase Realtime Database | ![](./plantuml/Firebase Realtime Database/RealtimeDatabase.png) | RealtimeDatabase(alias, "", "") | RealtimeDatabaseParticipant(alias, "", "") |
| Firebase Crash Reporting | ![](./plantuml/Firebase Crash Reporting/CrashReporting.png) | CrashReporting(alias, "", "") | CrashReportingParticipant(alias, "", "") |
| Cloud Storage for Firebase | ![](./plantuml/Cloud Storage for Firebase/Storage.png) | Storage(alias, "", "") | StorageParticipant(alias, "", "") |
| Google Analytics | ![](./plantuml/Google Analytics/Analytics.png) | Analytics(alias, "", "") | AnalyticsParticipant(alias, "", "") |
| Firebase Remote Config | ![](./plantuml/Firebase Remote Config/RemoteConfig.png) | RemoteConfig(alias, "", "") | RemoteConfigParticipant(alias, "", "") |
| Firebase App Distribution | ![](./plantuml/Firebase App Distribution/AppDistribution.png) | AppDistribution(alias, "", "") | AppDistributionParticipant(alias, "", "") |
| Firebase Test Lab | ![](./plantuml/Firebase Test Lab/TestLab.png) | TestLab(alias, "", "") | TestLabParticipant(alias, "", "") |
| Firebase Authentication | ![](./plantuml/Firebase Authentication/Authentication.png) | Authentication(alias, "", "") | AuthenticationParticipant(alias, "", "") |
| Firebase Performance Monitoring | ![](./plantuml/Firebase Performance Monitoring/PerformanceMonitoring.png) | PerformanceMonitoring(alias, "", "") | PerformanceMonitoringParticipant(alias, "", "") |
| Firebase AB Testing | ![](./plantuml/Firebase AB Testing/ABTesting.png) | ABTesting(alias, "", "") | ABTestingParticipant(alias, "", "") |
| Firebase Invites | ![](./plantuml/Firebase Invites/Invites.png) | Invites(alias, "", "") | InvitesParticipant(alias, "", "") |
| Firebase Extensions | ![](./plantuml/Firebase Extensions/Extensions.png) | Extensions(alias, "", "") | ExtensionsParticipant(alias, "", "") |
| Cloud Functions for Firebase | ![](./plantuml/Cloud Functions for Firebase/Functions.png) | Functions(alias, "", "") | FunctionsParticipant(alias, "", "") |
| Firebase Crashlytics | ![](./plantuml/Firebase Crashlytics/Crashlytics.png) | Crashlytics(alias, "", "") | CrashlyticsParticipant(alias, "", "") |
| Firebase Hosting | ![](./plantuml/Firebase Hosting/Hosting.png) | Hosting(alias, "", "") | HostingParticipant(alias, "", "") |
| Firebase Cloud Messaging | ![](./plantuml/Firebase Cloud Messaging/Messaging.png) | Messaging(alias, "", "") | MessagingParticipant(alias, "", "") |
| Firebase In-App Messaging | ![](./plantuml/Firebase In-App Messaging/InAppMessaging.png) | InAppMessaging(alias, "", "") | InAppMessagingParticipant(alias, "", "") |
<!-- icons-end -->

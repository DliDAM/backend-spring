package com.dlidam.configuration.fcm;

//@Configuration
//@Profile("!test && !local")
//public class ProdFcmConfiguration {
//
//    @Value("${fcm.key.path}")
//    private String FCM_PRIVATE_KEY_PATH;
//
//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        final FileInputStream refreshToken = new FileInputStream(FCM_PRIVATE_KEY_PATH);
//        final List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
//
//        if(firebaseApps.isEmpty()){
//            return makeNewInstance(refreshToken);
//        }
//
//        refreshToken.close();
//        return findExistingInstance(firebaseApps);
//    }
//
//    private FirebaseMessaging makeNewInstance(final FileInputStream refreshToken) throws IOException {
//        final FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(refreshToken))
//                .build();
//
//        refreshToken.close();
//
//        return FirebaseMessaging.getInstance(FirebaseApp.initializeApp(options));
//    }
//
//    private FirebaseMessaging findExistingInstance(final List<FirebaseApp> firebaseApps) {
//        return firebaseApps.stream()
//                .filter(app -> app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
//                .findAny()
//                .map(FirebaseMessaging::getInstance)
//                .orElseThrow(FcmNotFoundException::new);
//    }
//}

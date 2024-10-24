package com.dlidam.configuration.fcm;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
//@Profile("local")
public class LocalFcmConfiguration {

    @Bean
    public FirebaseMessaging firebaseMessaging(){
        final FirebaseApp firebaseApps = findFirebaseApps();

        return FirebaseMessaging.getInstance(firebaseApps);
    }

    private FirebaseApp findFirebaseApps() {
        final List<FirebaseApp> apps = FirebaseApp.getApps();

        if(!apps.isEmpty()){
            for(final FirebaseApp app : apps){
                if(FirebaseApp.DEFAULT_APP_NAME.equals(app.getName())){
                    return app;
                }
            }
        }

        final FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(new MockGoogleCredentials("test-token"))
                .setProjectId("test-project")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}

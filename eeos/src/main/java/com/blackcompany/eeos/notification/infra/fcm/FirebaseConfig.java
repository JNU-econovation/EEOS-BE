package com.blackcompany.eeos.notification.infra.fcm;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.credentials}")
	private String credentials;

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {

		FirebaseApp firebaseApp;

		if(FirebaseApp.getApps().isEmpty()) {
			ByteArrayInputStream serviceAccount = new ByteArrayInputStream(credentials.getBytes());

			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			firebaseApp = FirebaseApp.initializeApp(options);
		}else{
			firebaseApp = FirebaseApp.getInstance();
		}
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}

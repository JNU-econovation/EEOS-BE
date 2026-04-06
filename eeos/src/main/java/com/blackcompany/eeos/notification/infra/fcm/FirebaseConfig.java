package com.blackcompany.eeos.notification.infra.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.credentials}")
	private String credentials;

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {

		FirebaseApp firebaseApp;

		if (FirebaseApp.getApps().isEmpty()) {
			byte[] decoded = Base64.getDecoder().decode(credentials);
			ByteArrayInputStream serviceAccount = new ByteArrayInputStream(decoded);

			FirebaseOptions options =
					FirebaseOptions.builder()
							.setCredentials(GoogleCredentials.fromStream(serviceAccount))
							.build();

			firebaseApp = FirebaseApp.initializeApp(options);
		} else {
			firebaseApp = FirebaseApp.getInstance();
		}
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}

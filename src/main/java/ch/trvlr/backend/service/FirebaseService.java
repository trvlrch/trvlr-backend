package ch.trvlr.backend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class FirebaseService implements ApiService {

	private static final String serviceAccountFilename = "serviceAccountKey.json";
	private static final String databaseUrl = "https://trvlr-312df.firebaseio.com/";

	public FirebaseService() {
		//if (FirebaseAuth.getInstance() == null) {
			initialize();
		//}
	}

	private void initialize() {
		try (FileInputStream serviceAccount = new FileInputStream(getServiceAccountFilePath())) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					.setDatabaseUrl(databaseUrl)
					.build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			// TODO error handling
		}
	}

	private String getServiceAccountFilePath() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL serviceAccount = classLoader.getResource(serviceAccountFilename);
		if (serviceAccount != null) {
			return serviceAccount.getPath();
		} else {
			return "";
		}
	}

	public Boolean validateToken(String token) {
		FirebaseAuth.getInstance().verifyIdToken(token)
				.addOnSuccessListener(decodedToken -> {
					String uid = decodedToken.getUid();
					// TODO enable user
				});
		return true;

	}
}

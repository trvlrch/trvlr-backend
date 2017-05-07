package ch.trvlr.backend.service;

import ch.trvlr.backend.model.Traveler;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class FirebaseService extends ApiService implements AuthenticationInterface {

	private static final String serviceAccountFilename = "serviceAccountKey.json";
	private static final String databaseUrl = "https://trvlr-312df.firebaseio.com/";
	private static final String identityApiUrl = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/getAccountInfo";
	private static final String apiKey = "AIzaSyDsvs8gSMuNzwpLDG-CNn6FD8aZ_0c4Jds";

	private static FirebaseAuth auth = null;

	public FirebaseService() {
		if (auth == null) {
			FirebaseApp app = initialize();
			if (app != null) {
				auth = FirebaseAuth.getInstance(app);
			}
		}
	}

	private FirebaseApp initialize() {
		try (FileInputStream serviceAccount = new FileInputStream(getServiceAccountFilePath())) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					.setDatabaseUrl(databaseUrl)
					.build();

			return FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private String getServiceAccountFilePath() {
		// try loading path from env var first
		String path = System.getenv("FIREBASE_SERVICE_ACCOUNT_PATH");

		if (path == null) {
			// try with class loader if no env var is defined
			ClassLoader classLoader = getClass().getClassLoader();
			URL serviceAccount = classLoader.getResource(serviceAccountFilename);

			if (serviceAccount != null) {
				path = serviceAccount.getPath();
			} else {
				System.out.println("No Firebase conf");
				return "";
			}
		}

		return path;

	}

	public Traveler getUserByToken(String token) {
		if (validateToken(token)) {
			return getUserDetails(token);
		}
		return new Traveler();
	}

	private Boolean validateToken(String token) {
		Task task = FirebaseAuth.getInstance().verifyIdToken(token);

		// TODO refactor
		// wait for task to finish
		while (!task.isComplete()) {
		}

		FirebaseToken decodedToken = (FirebaseToken) task.getResult();
		return decodedToken.getUid() != null && !decodedToken.getUid().isEmpty();
	}

	private Traveler getUserDetails(String token) {
		try {
			URL url = new URL(identityApiUrl + "?key=" + apiKey);
			String json = "{\"idToken\":\"" + token + "\"}";

			JSONObject result = this.post(url, json);
			JSONArray users = result.getJSONArray("users");
			
			if (users.length() > 0) {
				JSONObject user = (JSONObject) users.get(0);
				// TODO move to traveler model
				String[] name = user.getString("displayName").split(" ");
				return new Traveler(name[0], name[1], user.getString("email"), user.getString("localId"));
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new Traveler();
	}
}


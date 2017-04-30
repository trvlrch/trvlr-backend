package ch.trvlr.backend.service;

import ch.trvlr.backend.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
public class FirebaseService implements ApiService {

	private static final String serviceAccountFilename = "serviceAccountKey.json";
	private static final String databaseUrl = "https://trvlr-312df.firebaseio.com/";
	private static final String identityApiUrl = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/getAccountInfo?";

	private static FirebaseAuth auth = null;
	
	public FirebaseService() {
		if (auth == null) {
			FirebaseApp app = initialize();
			auth = FirebaseAuth.getInstance(app);
		}
	}

	private FirebaseApp initialize() {
		try (FileInputStream serviceAccount = new FileInputStream(getServiceAccountFilePath())) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
					.setDatabaseUrl(databaseUrl)
					.build();

			System.out.println("---------");
			System.out.println(options.toString());
			System.out.println("---------");

			return FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			// TODO error handling
			System.out.println(e.getMessage());
			return null;
		}
	}

	private String getServiceAccountFilePath() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL serviceAccount = classLoader.getResource(serviceAccountFilename);
		if (serviceAccount != null) {
			System.out.println(serviceAccount.getPath());
			return serviceAccount.getPath();
		} else {
			return "";
		}
	}

	public Boolean validateToken(String token) {
		System.out.println("no");
		auth.verifyIdToken(token)
				.addOnSuccessListener(decodedToken -> {
					String uid = decodedToken.getUid();
					System.out.println("----------------------");
					System.out.println(uid);
					// TODO enable user
				});
		return true;
	}

	public User getUserDetail() {

	}

	private JSONObject uploadToServer() throws IOException, JSONException {
		String query = "https://example.com";
		String json = "{\"key\":1}";

		URL url = new URL(query);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");

		OutputStream os = conn.getOutputStream();
		os.write(json.getBytes("UTF-8"));
		os.close();

		// read the response
		InputStream in = new BufferedInputStream(conn.getInputStream());
		String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		JSONObject jsonObject = new JSONObject(result);

		in.close();
		conn.disconnect();

		return jsonObject;
	}


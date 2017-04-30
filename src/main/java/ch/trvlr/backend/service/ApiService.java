package ch.trvlr.backend.service;

import ch.trvlr.backend.model.Traveler;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * trvlr-backend
 *
 * @author Daniel Milenkovic
 */
abstract class ApiService {

	abstract Traveler getUserByToken(String token);

	protected JSONObject post(URL url, String json_body) throws IOException{

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");

		OutputStream os = conn.getOutputStream();
		os.write(json_body.getBytes("UTF-8"));
		os.close();

		// read the response
		InputStream in = new BufferedInputStream(conn.getInputStream());
		String result = IOUtils.toString(in, "UTF-8");
		JSONObject jsonObject = new JSONObject(result);

		in.close();
		conn.disconnect();

		return jsonObject;
	}
}

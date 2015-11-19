package freelance.android.erick.demoapplication.common;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import freelance.android.erick.demoapplication.model.Response;
import freelance.android.erick.demoapplication.model.Values;

/**
 * Created by erick on 19.11.15.
 */
public class API {
    public static abstract class CallbackGetMessages {
        public abstract void onSuccess(ArrayList<Values> model);

        public abstract void onError(String error);
    }

    public static abstract class CallbackResponse {
        public abstract void onSuccess(Response model);

        public abstract void onError(Response error);
    }

    public static void asyncGetMessages(final CallbackGetMessages callback) {
        new AsyncTask<Void, Void, ArrayList<Values>>() {
            @Override
            protected ArrayList<Values> doInBackground(Void... params) {
                String resultString = "";
                String responseLine;
                ArrayList<Values> returnModel = new ArrayList<>();

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("vkcheck.hol.es")
                        .appendPath("value")
                        .appendPath("getValues");
                String myUrl = builder.build().toString();

                String postData = "secret_key=" + Constants.REQUEST_API_KEY;

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(myUrl).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
                    connection.setDoOutput(true);

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(postData);
                    outputStream.flush();
                    outputStream.close();
                    connection.setConnectTimeout(10000);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((responseLine = bufferedReader.readLine()) != null) {
                        resultString += responseLine;
                    }
                    bufferedReader.close();

                    JSONObject jsonObject = new JSONObject(resultString);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (!code.equals("201")) {
                        callback.onError(message);
                        return null;
                    }

                    JSONArray response = new JSONArray(message);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject currLot = response.getJSONObject(i);
                        Values tempModel = new Values();
                        if (currLot.has("id"))
                            tempModel.setId(currLot.getString("id"));

                        if (currLot.has("text"))
                            tempModel.setText(currLot.getString("text"));

                        if (currLot.has("created_at"))
                            tempModel.setCreated_at(currLot.getString("created_at"));
                        returnModel.add(tempModel);
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return returnModel;
            }

            @Override
            protected void onPostExecute(ArrayList<Values> result) {
                callback.onSuccess(result);
            }
        }.execute();
    }

    public static void asyncAddMessage(final String text, final CallbackResponse callback) {
        new AsyncTask<String, Void, Response>() {
            @Override
            protected Response doInBackground(String... params) {
                String resultString = "";
                String responseLine;
                Response returnModel = new Response();

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("vkcheck.hol.es")
                        .appendPath("value")
                        .appendPath("addNewValue");
                String myUrl = builder.build().toString();

                String post = "secret_key=" + Constants.REQUEST_API_KEY + "&text=" + params[0];

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(myUrl).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Length", String.valueOf(post.length()));
                    connection.setDoOutput(true);

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(post);
                    outputStream.flush();
                    outputStream.close();
                    connection.setConnectTimeout(10000);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((responseLine = bufferedReader.readLine()) != null) {
                        resultString += responseLine;
                    }
                    bufferedReader.close();

                    JSONObject jsonObject = new JSONObject(resultString);
                    if(jsonObject.has("status"))
                        returnModel.setStatus(jsonObject.getString("status"));
                    if(jsonObject.has("code"))
                        returnModel.setCode(jsonObject.getString("code"));
                    if(jsonObject.has("message"))
                        returnModel.setMessage(jsonObject.getString("message"));
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                return returnModel;
            }

            @Override
            protected void onPostExecute(Response result) {
                if (result.getCode().equals("201")) {
                    callback.onSuccess(result);
                } else {
                    callback.onError(result);
                }

            }
        }.execute(text);
    }
}

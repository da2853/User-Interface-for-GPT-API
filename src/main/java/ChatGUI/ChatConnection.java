package ChatGUI;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatConnection {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String ROLE_SYSTEM = "system";
    private static final String ROLE_USER = "user";
    private static final String ROLE_ASSISTANT = "assistant";
    private static final String PROMPT = "You are a helpful assistant.";

    private OkHttpClient client;
    private String apiKey;
    private String json;
    private StringBuilder allMessages = new StringBuilder();

    public String formatString(String role, String content){
        return String.format("{\"role\": \"%s\", \"content\":  \"%s\"}", role, content);
    }

    public String[] initialize(String key) {
        this.apiKey = key;
        this.client = new OkHttpClient();
        return sendPost("");
    }

    public String sendToGPT(String message){
        return sendPost(message)[1];
    }

    private String[] sendPost(String cont) {
        if (allMessages.toString().isEmpty()) {
            allMessages.append(formatString(ROLE_SYSTEM, PROMPT));
            json = formatString(MODEL, allMessages.toString());
            System.out.println("JSON: " + json);
        } else if (!cont.isEmpty()) {
            allMessages.append(",").append(formatString(ROLE_USER, cont));
            json = formatString(MODEL, allMessages.toString());
            System.out.print("JSON: " + json);
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                return new String[]{"1", ("Unexpected code " + response)};

            assert response.body() != null;
            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            JSONObject choice = choices.getJSONObject(0);
            JSONObject message = choice.getJSONObject("message");
            String content = message.getString("content");
            allMessages.append(",").append(formatString(ROLE_ASSISTANT, content));
            return new String[]{"0", content + '\n'};
        } catch (IOException e) {
            return new String[]{"1","An IOException Has Occurred: " + e.getMessage()};
        }
    }
}

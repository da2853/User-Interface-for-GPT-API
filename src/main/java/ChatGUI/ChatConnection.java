package ChatGUI;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatConnection {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    protected String MODEL;
    private String ROLE_SYSTEM;
    private String ROLE_USER;
    private String ROLE_ASSISTANT;
    private String PROMPT;
    private String lastUserMessage;
    private String lastAssistantMessage;
    private  String apiKey;
    private StringBuilder allMessages = new StringBuilder();

    protected void defaultSettings(){
        MODEL = "gpt-3.5-turbo";
        ROLE_SYSTEM = "system";
        ROLE_USER = "user";
        ROLE_ASSISTANT = "assistant";
        PROMPT = "You are a helpful assistant.";
        lastUserMessage = null;
        lastAssistantMessage = null;
    }

    public String formatMessage(String role, String content) {
        return String.format("{\"role\": \"%s\", \"content\": \"%s\"}", role, content);
    }

    public String formatRequestBody(String model, String messages) {
        return String.format("{\"model\": \"%s\", \"messages\": [%s]}", model, messages);
    }

    public String[] initialize(String key) {
        defaultSettings();
        this.apiKey = key;
        this.client = new OkHttpClient();
        return sendPost();
    }

    public String sendToGPT(String message) {
        lastUserMessage = message;
        return sendPost()[1];
    }
    private String[] sendPost() {
        StringBuilder messagesToSend = new StringBuilder();

        if (lastUserMessage == null && lastAssistantMessage == null) {
            messagesToSend.append(formatMessage(ROLE_SYSTEM, PROMPT));
        } else {
            if (lastAssistantMessage != null) {
                if (messagesToSend.length() > 0) {
                    messagesToSend.append(",");
                }
                messagesToSend.append(formatMessage(ROLE_ASSISTANT, lastAssistantMessage));
            }
            if (lastUserMessage != null) {
                if (messagesToSend.length() > 0) {
                    messagesToSend.append(",");
                }
                messagesToSend.append(formatMessage(ROLE_USER, lastUserMessage));
            }

        }

        String json = formatRequestBody(MODEL, messagesToSend.toString());
        System.out.println("JSON: " + json);

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

            lastAssistantMessage = content;

            return new String[]{"0", content + '\n'};
        } catch (IOException e) {
            return new String[]{"1", "An IOException Has Occurred: " + e.getMessage()};
        }
    }
}
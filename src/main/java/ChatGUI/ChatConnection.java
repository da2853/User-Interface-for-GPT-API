package ChatGUI;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChatConnection {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final int MAX_MESSAGE_LENGTH = 4070;
    private static final int TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json");

    protected String MODEL;
    private String ROLE_SYSTEM;
    private String ROLE_USER;
    private String ROLE_ASSISTANT;
    private String PROMPT;
    private String lastUserMessage;
    private String lastAssistantMessage;
    private String apiKey;

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
        return sendPost();
    }

    public String sendToGPT(String message) {
        if (message.split(" ").length > MAX_MESSAGE_LENGTH) {
            return "Your message is too long. Please shrink it and try again.";
        }


        lastUserMessage = message;
        String[] response = sendPost();
        if (response[0].equals("1")) {
            return response[1];
        }

        return response[1];
    }
    private String[] sendPost() {
        StringBuilder messagesToSend = new StringBuilder();

        if (lastUserMessage == null && lastAssistantMessage == null) {
            appendMessage(messagesToSend, ROLE_SYSTEM, PROMPT);
        } else {
            appendMessage(messagesToSend, ROLE_ASSISTANT, truncateMessage(lastAssistantMessage, MAX_MESSAGE_LENGTH));
            appendMessage(messagesToSend, ROLE_USER, truncateMessage(lastUserMessage, MAX_MESSAGE_LENGTH));
        }

        String json = formatRequestBody(MODEL, messagesToSend.toString());
        System.out.println("JSON: " + json);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, json);

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
            e.printStackTrace();
            return new String[]{"1", "An IOException Has Occurred: " + e.getMessage()};
        }
    }


    private String truncateMessage(String message, int maxLength) {
        if (message.length() <= maxLength) {
            return message;
        }
        return message.substring(message.length() - maxLength);
    }


    private void appendMessage(StringBuilder messages, String role, String message) {
        if (message != null) {
            if (messages.length() > 0) {
                messages.append(",");
            }
            messages.append(formatMessage(role, message));
        }
    }

    public String[] getSettings() {
        return new String[]{PROMPT, ROLE_ASSISTANT, ROLE_USER};
    }

    public void saveSettings(String prompt, String assistantRole, String userRole) {
        this.PROMPT = prompt;
        this.ROLE_ASSISTANT = assistantRole;
        this.ROLE_USER = userRole;
    }
}

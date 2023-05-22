package ChatGUI;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
        // Check if the new message exceeds the token limit
        if (message.split(" ").length > 4096) {
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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

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

            // Check if the assistant's message exceeds the token limit
            if (content.split(" ").length > 4096) {
                // Truncate the assistant's message to fit the token limit
                content = truncateMessage(content, 4096);
            }

            lastAssistantMessage = content;

            return new String[]{"0", content + '\n'};
        } catch (IOException e) {
            return new String[]{"1", "An IOException Has Occurred: " + e.getMessage()};
        }
    }

    private String truncateMessage(String message, int maxLength) {
        String[] tokens = message.split(" ");
        StringBuilder truncatedMessage = new StringBuilder();
        int count = 0;

        for (String token : tokens) {
            if (count + token.length() > maxLength) {
                break;
            }

            truncatedMessage.append(token).append(" ");
            count += token.length() + 1;
        }

        return truncatedMessage.toString().trim();
    }
}

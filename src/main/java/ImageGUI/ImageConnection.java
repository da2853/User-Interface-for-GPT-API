//package ImageGUI;
//
//import com.google.gson.Gson;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//import okhttp3.*;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//public class ImageConnection {
//    private static final String API_URL = "https://api.openai.com/v1/images/generations";
//    private static final MediaType JSON = MediaType.parse("application/json");
//    private static final int TIMEOUT = 60;
//    private String apiKey;
//
//    @FXML
//    TextField promptTextArea;
//
//    protected void defaultSettings() {
//        // default settings, if any
//    }
//
//    public void initialize(String key) {
//        defaultSettings();
//        this.apiKey = key;
//        sendToGPT();
//    }
//
//    private String formatRequestBody() {
//        int maxImages = 1;
//        String size = "1024x1024";
//        String prompt = promptTextArea.getText();
//
//        ImageGenerationRequest requestBody = new ImageGenerationRequest(prompt, maxImages, size);
//
//        Gson gson = new Gson();
//        return gson.toJson(requestBody);
//    }
//
//    private void sendToGPT() {
//        Platform.runLater(() -> {
//            String prompt = promptTextArea.getText();
//
//            if (prompt != null && !prompt.isEmpty()) {
//                promptTextArea.setText("");
//
//                OkHttpClient client = new OkHttpClient.Builder()
//                        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
//                        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
//                        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
//                        .build();
//
//                String json = formatRequestBody();
//
//                RequestBody body = RequestBody.create(JSON, json);
//
//                Request request = new Request.Builder()
//                        .url(API_URL)
//                        .addHeader("Content-Type", "application/json")
//                        .addHeader("Authorization", "Bearer " + apiKey)
//                        .post(body)
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        // handle request failure
//                        Platform.runLater(() -> {
//                            // Show an error message to the user
//                            // e.g., showAlert("Error: " + e.getMessage());
//                        });
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (!response.isSuccessful()) {
//                            // handle unsuccessful response
//                            Platform.runLater(() -> {
//                                // Show an error message to the user
//                                // e.g., showAlert("Error: " + response.message());
//                            });
//                        } else {
//                            String responseBody = response.body().string();
//                            Gson gson = new Gson();
//                            ImageGenerationResponse imageResponse;
//                            try {
//                                imageResponse = gson.fromJson(responseBody, ImageGenerationResponse.class);
//                                // Then do something with imageResponse
//                                // e.g., displayImage(imageResponse.getGeneratedImages());
//                            } catch (Exception e) {
//                                // Handle error in parsing the response
//                                Platform.runLater(() -> {
//                                    // Show an error message to the user
//                                    // e.g., showAlert("Error: " + e.getMessage());
//                                });
//                            }
//                        }
//                    }
//                });
//            }
//        });
//    }
//}

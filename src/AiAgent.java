import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI Agent
 * 提供与 Ollama 模型交互的 API，支持流式输出
 */
public class AiAgent {
    private static final String OLLAMA_API = "http://localhost:11434/api/generate";
    private static final String MODEL = "deepseek-r1:8b";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * 流式回调接口
     */
    public interface StreamCallback {
        //当收到新的回复内容时调用
        void onResponse(String text);

        //当收到新的思考内容时调用
        void onThinking(String text);

        //当完成时调用
        void onComplete();

        //当发生错误时调用
        void onError(Throwable t);
    }

    //非流式只获取完整回复
    public static String ask(String prompt) throws Exception {
        String json = String.format("""
            {
                "model": "%s",
                "prompt": "%s",
                "stream": false
            }
            """, MODEL, prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_API))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return extractJsonValue(response.body(), "response");
    }

    /**
     * 流式：实时获取 AI 的输出
     * @param prompt 用户问题
     * @param callback 回调接口，用于接收实时内容
     */
    public static void askStream(String prompt, StreamCallback callback) {
        try {
            String json = String.format("""
                {
                    "model": "%s",
                    "prompt": "%s",
                    "stream": true
                }
                """, MODEL, prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_API))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // 发送请求并接收流式响应
            CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                    .thenAccept(response -> {
                        response.body().forEach(line -> {
                            if (line.trim().isEmpty()) {
                                return;
                            }
                            try {
                                // 提取 response 和 thinking
                                String responseText = extractJsonValue(line, "response");
                                String thinkingText = extractJsonValue(line, "thinking");
                                
                                if (responseText != null && !responseText.isEmpty()) {
                                    callback.onResponse(responseText);
                                }
                                if (thinkingText != null && !thinkingText.isEmpty()) {
                                    callback.onThinking(thinkingText);
                                }
                                
                                // 检查是否完成
                                if (line.contains("\"done\":true")) {
                                    callback.onComplete();
                                }
                            } catch (Exception e) {
                                // 忽略解析错误
                            }
                        });
                    })
                    .exceptionally(ex -> {
                        callback.onError(ex);
                        return null;
                    });

        } catch (Exception e) {
            callback.onError(e);
        }
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"(.*?)\"(?=,\"|\\}$|$)";
        Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(json);
        if (matcher.find()) {
            return matcher.group(1)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }
        return null;
    }
    
}

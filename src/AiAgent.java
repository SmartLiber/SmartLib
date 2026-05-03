import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
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
     * 流式数据是 异步 的（数据一点一点来），不能用返回值直接获取，需要通过回调通知调用者。
     * AiAgentUI 实现了这个接口，用于实时显示模型的输出和思考过程
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

            /*
            发送请求并接收流式响应  异步流式响应处理
            sendAsync()异步发送请求
            BodyHandlers.ofLines()逐行处理响应体
            */

            CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                    .thenAccept(response -> {

                        // 当响应成功到达时执行
                        response.body().forEach(line -> {
                            // 处理每一行
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

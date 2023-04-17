package com.catchyan.mrzz;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.catchyan.mrzz.model.CreateCompletionRequest;
import com.catchyan.mrzz.model.CreateCompletionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
public class MrZzApplication {

    public static Map<String, Integer> userMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        SpringApplication.run(MrZzApplication.class, args);
    }

    @Value("${openai.api-key}")
    private String API_KEY;
    @Value("${user.free-ask-count}")
    private Integer FREE_ASK_COUNT;
    @Value("${openai.url}")
    private String URL;
    @Value("${openai.model}")
    private String MODEL;

    @GetMapping("/ask")
    public String ask(String userId, String message){
        if(!userMap.containsKey(userId)){
            userMap.put(userId, FREE_ASK_COUNT - 1);
        }
        if(userMap.get(userId) < 1){
            return "免费提问次数已用完";
        }

        CreateCompletionRequest request = new CreateCompletionRequest();
        request.setModel(MODEL);
        request.setMessages(new ArrayList<CreateCompletionRequest.Message>(){{add(new CreateCompletionRequest.Message("user", message));}});

        String result = HttpRequest.post(URL)
                .header("Authorization", "Bearer " + API_KEY)
//                .setHttpProxy("127.0.0.1", 7890)
                .body(JSONUtil.toJsonStr(request))
                .execute()
                .body();
        List<CreateCompletionResponse.ChoicesItem> choicesItemList = JSONUtil.toBean(result, CreateCompletionResponse.class).getChoices();
        return choicesItemList.stream()
                .map(CreateCompletionResponse.ChoicesItem::getMessage)
                .map(CreateCompletionResponse.Message::getContent)
                .collect(Collectors.joining());
    }
}

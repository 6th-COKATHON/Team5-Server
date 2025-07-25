package cotato.hack.domain.ai.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cotato.hack.domain.ai.dto.ChatGPTRequest;
import cotato.hack.domain.ai.dto.ChatGPTResponse;
import cotato.hack.domain.ai.dto.GPTEmotionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

	@Value("${openai.model}")
	private String model;

	@Value("${openai.api.url}")
	private String apiURL;

	private final RestTemplate template;


	public GPTEmotionResponse chat(String prompt) {
		String processedPrompt = """
			You are an AI that analyzes human emotions in a text. 
			Read the following diary entry carefully and count the occurrences of emotions in the text. 
			The emotions to identify and count are: happiness, sadness, anger, fear, love, disgust, surprise, gratitude, regret, and hope. 
			For each emotion, provide a count of how many times it is expressed in the text. 
			
			Additionally, provide two separate, detailed reviews of the diary entry in Korean:
			1. **천사 리뷰**:
			    - Warm, empathetic, and encouraging tone.
			    - Provide a deep analysis of the positive emotions and the user's actions or decisions.
			    - Highlight strengths, show understanding of struggles, and provide hope or actionable suggestions for the future.
	
			2. **악마 리뷰**:
			    - Harsh, blunt, and critical tone, but with constructive feedback.
			    - Analyze the negative emotions and point out areas where the user could improve or act differently.
			    - Be straightforward and a bit mischievous, but ensure the review is helpful.
	
			
			Here is the diary entry: 
			""" + prompt + """
			
			Respond in the following JSON format:
			{
			  "happiness": <count>,
			  "sadness": <count>,
			  "anger": <count>,
			  "fear": <count>,
			  "love": <count>,
			  "disgust": <count>,
			  "surprise": <count>,
			  "gratitude": <count>,
			  "regret": <count>,
			  "hope": <count>,
			  "devil_review": "<악마 리뷰 내용 (한국어로 작성)>",
			  "angel_review": "<천사 리뷰 내용 (한국어로 작성)>"
			}
			""";

		ChatGPTRequest request = new ChatGPTRequest(model, processedPrompt);

		// GPT 응답을 EmotionResponse로 매핑
		ChatGPTResponse gptResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
		ObjectMapper objectMapper = new ObjectMapper();

		log.info("GPT Response: {}", gptResponse);
		try {
			return objectMapper.readValue(gptResponse.getChoices().get(0).getMessage().getContent(),
				GPTEmotionResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Failed to parse GPT response", e);
		}
	}
}

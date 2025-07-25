package cotato.hack.domain.ai.dto;

import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class ChatGPTRequest {
	private String model;
	private List<AiMessage> messages;

	public ChatGPTRequest(String model, String prompt) {
		this.model = model;
		this.messages =  new ArrayList<>();
		this.messages.add(new AiMessage("user", prompt));
	}
}

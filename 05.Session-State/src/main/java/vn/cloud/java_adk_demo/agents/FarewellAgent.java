package vn.cloud.java_adk_demo.agents;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;

public class FarewellAgent {

	public static BaseAgent FAREWELL_AGENT = LlmAgent.builder()
			.name("FarewellAgent")
			.model("gemini-2.0-flash")
			.description("Handles simple farewells and goodbyes using the 'sayGoodbye' tool.")
			.instruction("""
				You are the Farewell Agent. Your ONLY task is to provide a polite goodbye message. \
				Use the 'say_goodbye' tool when the user indicates they are leaving or ending the conversation \
				(e.g., using words like 'bye', 'goodbye', 'thanks bye', 'see you'). \
				
				Translate the response to {preferenceLanguage}.
				Do not perform any other actions.
				""")
			.tools(FunctionTool.create(FarewellAgent.class, "sayGoodbye"))
			.build();

	@Schema(description = "Provides a simple farewell message to conclude the conversation.")
	public static Map<String, String> sayGoodbye() {
		System.out.println("--- Tool: sayGoodbye called ---");
		return Map.of("status", "success", "report", "Goodbye! Have a great day.");
	}
}

package vn.cloud.java_adk_demo.agents;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;

public class GreetingAgent {

	public static BaseAgent GREETING_AGENT = LlmAgent.builder()
			.name("GreetingAgent")
			.model("gemini-2.0-flash")
			.description("Handles simple greetings and hellos using the 'say_hello' tool.")
			.instruction("""
				You are the Greeting Agent. Your ONLY task is to provide a friendly greeting to the user. \
				Use the 'sayHello' tool to generate the greeting. \
				If the user provides their name, make sure to pass it to the tool. \
				
				Translate the response to {preferenceLanguage}.
				Do not engage in any other conversation or tasks.
				""")
			.tools(FunctionTool.create(GreetingAgent.class, "sayHello"))
			.build();

	@Schema(description = "Provides a simple greeting. If a name is provided, it will be used.")
	public static Map<String, String> sayHello(@Schema(name = "name", description = "The name of the person to greet. Optional parameter.") String name) {
		System.out.printf("--- Tool: sayHello called for name: %s ---%n", name);
		String greeting = name != null && !name.trim().isEmpty()
				? "Hello, %s!".formatted(name) : "Hello there!";
		return Map.of("status", "success", "report", greeting);
	}
}

package vn.cloud.java_adk_demo.agents;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.Callbacks;
import com.google.adk.agents.InvocationContext;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.LlmRequest;
import com.google.adk.models.LlmResponse;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.FunctionTool;
import com.google.adk.tools.ToolContext;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Maybe;

public class WeatherAgent {

	public static BaseAgent ROOT_AGENT = LlmAgent.builder()
			.name("WeatherAgent")
			.model("gemini-2.0-flash")
			.description("Provide weather information using the 'getWeather' tool.")
			.instruction("""
				You are the main Weather Agent. Your primary responsibility is to provide weather information. \
				Use the 'getWeather' tool ONLY for specific weather requests (e.g., 'weather in London'). \
				For anything else, respond appropriately or state you cannot handle it.
				""")
			.tools(FunctionTool.create(WeatherAgent.class, "getWeather"))
			.beforeModelCallback(WeatherAgent::blockKeywordGuardrail)
			.beforeToolCallback(WeatherAgent::blockParisToolGuardrail)
			.build();

	@Schema(description = "Provide weather information for the specified city.")
	public static Map<String, String> getWeather(@Schema(name = "city", description = "The name of the city for which to retrieve the weather report") String city) {
		System.out.printf("--- Tool: getWeather called for city: %s ---%n", city);

		String cityNormalized = city.toLowerCase().replace(" ", "");

		Map<String, Map<String, String>> mockWeatherDb = Map.of(
				"newyork", Map.of("status", "success", "report", "The weather in New York is sunny with a temperature of 25°C."),
				"london", Map.of("status", "success", "report", "It's cloudy in London with a temperature of 15°C."),
				"tokyo", Map.of("status", "success", "report", "Tokyo is experiencing light rain and a temperature of 18°C.")
		);

		if (mockWeatherDb.containsKey(cityNormalized)) {
			return mockWeatherDb.get(cityNormalized);
		} else {
			return Map.of("status", "error", "error_message", "Sorry, I don't have weather information for '" + city + "'.");
		}
	}

	/**
	 * Inspects the latest user message for 'BLOCK'. If found, blocks the LLM call
	 * and returns a predefined response. Otherwise, returns null to proceed.
	 * This is a conceptual implementation of a beforeModelCallback guardrail.
	 */
	private static Maybe<LlmResponse> blockKeywordGuardrail(CallbackContext callbackContext, LlmRequest llmRequest) {
		// Extract the text from the latest user message in the request history
		String lastUserMessageText = "";
		if (llmRequest.contents() != null && !llmRequest.contents().isEmpty()) {
			// Find the most recent message with role 'user'
			for (int i = llmRequest.contents().size() - 1; i >= 0; i--) {
				Content content = llmRequest.contents().get(i);
				if (content.role().isPresent() && "user".equals(content.role().get()) &&
						content.parts().isPresent() && !content.parts().get().isEmpty()) {
					// Assuming text is in the first part for simplicity
					if (content.parts().get().get(0).text().isPresent()) {
						lastUserMessageText = content.parts().get().get(0).text().get();
						break; // Found the last user message text
					}
				}
			}
		}

		// Guardrail Logic
		String keywordToBlock = "BLOCK";
		if (lastUserMessageText.toUpperCase().contains(keywordToBlock)) { // Case-insensitive check
			// Keyword found. Construct and return an LlmResponse to stop the flow and send this back instead
			Content blockedContent = Content.fromParts(
					Part.fromText("I cannot process this request because it contains the blocked keyword '%s'.".formatted(keywordToBlock))
			);
			LlmResponse blockedResponse = LlmResponse.builder()
					.content(blockedContent)
					.build();
			return Maybe.just(blockedResponse);
		} else {
			// Keyword not found, allow the request to proceed to the LLM
			return Maybe.empty(); // Returning empty signals ADK to continue normally
		}
	}

	/**
	 * Checks if 'getWeather' is called for 'Paris'.
	 * If so, blocks the tool execution and returns a specific error dictionary.
	 * Otherwise, allows the tool call to proceed by returning null.
	 */
	private static Maybe<Map<String, Object>> blockParisToolGuardrail(
			InvocationContext invocationContext,
			BaseTool baseTool,
			Map<String, Object> input,
			ToolContext toolContext) {

		// Guardrail Logic
		String targetToolName = "getWeather"; // Match the function name used by FunctionTool
		String blockedCity = "paris";
		String toolName = baseTool.name();

		// Check if it's the correct tool and the city argument matches the blocked city
		if (targetToolName.equals(toolName)) {
			String cityArgument = (String) input.getOrDefault("city", ""); // Safely get the 'city' argument
			if (!cityArgument.isEmpty() && cityArgument.toLowerCase().equals(blockedCity)) {
				// Return a Map matching the tool's expected output format for errors
				// This Map becomes the tool's result, skipping the actual tool run.
				Map<String, Object> errorResult = Map.of(
						"status", "error",
						"error_message", "Policy restriction: Weather checks for '" +
								cityArgument.substring(0, 1).toUpperCase() + cityArgument.substring(1).toLowerCase() +
								"' are currently disabled by a tool guardrail."
				);
				return Maybe.just(errorResult);
			}
		}

		return Maybe.empty(); // Returning empty allows the actual tool function to run
	}

}

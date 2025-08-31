package vn.cloud.java_adk_demo.agents;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.adk.tools.ToolContext;

public class WeatherAgent {

	public static BaseAgent ROOT_AGENT = LlmAgent.builder()
			.name("WeatherAgent")
			.model("gemini-2.0-flash")
			.description("Provide weather information using the 'getWeather' tool.")
			.instruction("""
				You are the polite Weather Agent.
				
				For every request, you always do these steps sequentially:
				1. (Required) Call 'initiateUserPreference' tool for setting the user preference's language, you can pass null if you don't find any related information.
				2. (Required) Greeting user with 'GreetingAgent' tool.
				3. (Optional) Use the 'getWeather' tool for specific weather requests (e.g., 'weather in London').
				4. (Required) Saying goodbye user with 'FarewellAgent' tool.
				5. (Required) Combine all results from step 2,3,4 to return the final response to user.
				
				For anything else, respond appropriately or state you cannot handle it.
				""")
			.tools(
					FunctionTool.create(WeatherAgent.class, "getWeather"),
					AgentTool.create(GreetingAgent.GREETING_AGENT), // GreetingAgent tool
					AgentTool.create(FarewellAgent.FAREWELL_AGENT), // FarewellAgent tool
					FunctionTool.create(WeatherAgent.class, "initiateUserPreference")
			)
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

	@Schema(description = "initiate the user preferences ")
	public static Map<String, String> initiateUserPreference(
			@Schema(name = "language", description = "user language preference") String language,
			@Schema(name = "toolContext") ToolContext toolContext) {

		System.out.printf("--- Tool: initiateUserPreference called, language is set to: %s ---%n", language);

		if (toolContext.state().containsKey("preferenceLanguage")) {
			System.out.println("preferenceLanguage is already set");
			return Map.of("status", "success", "report", "The preference language is already set");
		}

		String preferenceLanguage = language == null || language.isEmpty() ? "English" : language;
		toolContext.state().put("preferenceLanguage", language);
		System.out.printf("preferenceLanguage is set to %s%n", preferenceLanguage);

		return Map.of("status", "success", "report", "The preference language is set to " + language);
	}

}

package vn.cloud.java_adk_demo.agents;

import java.util.Map;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;

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
			.subAgents(GreetingAgent.GREETING_AGENT, FarewellAgent.FAREWELL_AGENT)
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

}

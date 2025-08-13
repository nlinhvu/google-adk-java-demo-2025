package vn.cloud.java_adk_demo.agents;

import com.google.adk.agents.LlmAgent;

public class TravelPlanningAgent {

	public static LlmAgent ROOT_AGENT = LlmAgent.builder()
			.name("TravelPlanningAgent")
			// Only Anthropic Claude Family and Gemini Family are supported in the Java Version in 0.2.0
			.model("gemini-2.0-flash")
			// A concise summary of the agent's overall purpose. This becomes crucial later
			// when other agents need to decide whether to delegate tasks to this agent.
			.description("A helpful assistant to brainstorm and plan travel itineraries.")
			// Detailed guidance for the LLM on how to behave, its persona, its goals,
			// and specifically how and when to utilize its assigned `tools`.
			.instruction("""
				You are a friendly and enthusiastic travel planning assistant.
				Your goal is to help users by providing travel suggestions, and creating potential itineraries.
				Engage with the user to understand their interests and preferences for their trip.
				Provide informative and inspiring suggestions for destinations, activities, and local cuisine.
				""")
			.build();
}

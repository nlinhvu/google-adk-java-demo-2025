package vn.cloud.java_adk_demo.agents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;

public class GoogleSearchAgent {

	public static BaseAgent ROOT_AGENT =
			LlmAgent.builder()
					.name("GoogleSearchAgent")
					.model("gemini-2.0-flash") // Ensure to use a Gemini 2.0 model for Google Search Tool
					.description("Agent to answer questions using Google Search.")
					.instruction("I can answer your questions by searching the internet. Just ask me anything!")
					.tools(new GoogleSearchTool())
					.build();
}

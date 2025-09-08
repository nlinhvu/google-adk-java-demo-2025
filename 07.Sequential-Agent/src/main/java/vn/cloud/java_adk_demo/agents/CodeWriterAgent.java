package vn.cloud.java_adk_demo.agents;

import com.google.adk.agents.LlmAgent;

public class CodeWriterAgent {

	public static LlmAgent CODE_WRITER_AGENT = LlmAgent.builder()
			.model("gemini-2.0-flash")
			.name("CodeWriterAgent")
			.description("Writes initial Java code based on a specification.")
			.instruction(
					"""
					You are a Java Code Generator.
					Based *only* on the user's request, write Java code that fulfills the requirement.
					Output *only* the complete Java code block, enclosed in triple backticks (```java ... ```).
					Do not add any other text before or after the code block.
					""")
			// --> OUTPUT KEY: the response of this agent will be saved to "generated_code" attribute in the state of the current session
			.outputKey("generated_code")
			.build();
}

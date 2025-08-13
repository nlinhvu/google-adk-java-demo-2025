package vn.cloud.java_adk_demo.agents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;

public class AlarmAgent {

	public static LlmAgent ROOT_AGENT = initRootAgent();

	public static LlmAgent initRootAgent() {
		AlarmTools alarmTools = new AlarmTools();

		return LlmAgent.builder()
				.name("AlarmAgent")
				.model("gemini-2.0-flash")
				.description("An agent that helps users set up alarms by getting the current time and setting alarms at specified future times")
				.instruction("""
					You are the Alarm Agent responsible for setting up alarms for users. \
					Your primary responsibility is to help users set alarms at specific times.
					
					You have 2 specialized tools:
					1. 'getCurrentTime': Use this tool to get the current date and time when users request relative time alarms (e.g., 'set alarm in 15 minutes').
					2. 'setAlarm': Use this tool to actually set the alarm with the calculated target time in ISO format.
					
					Analyze the user's query. If they request an alarm with relative time, first use 'getCurrentTime', \
					calculate the target time by adding the requested duration, then use 'setAlarm'.
					If they provide absolute time, use 'setAlarm' directly.
					For anything else, respond appropriately or state you cannot handle it.
					""")
				.tools(
						FunctionTool.create(alarmTools, "getCurrentTime"),
						FunctionTool.create(alarmTools, "setAlarm")
				)
				.build();
	}
}

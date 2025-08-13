package vn.cloud.java_adk_demo.agents;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.google.adk.tools.Annotations.Schema;

public class AlarmTools {

	@Schema(description = "Gets the current date and time in ISO format")
	public Map<String, String> getCurrentTime() {
		return Map.of("status", "success", "report", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
	}

	@Schema(description = "Sets an alarm for the specified time")
	public Map<String, String> setAlarm(@Schema(name = "time", description = "The target time to set the alarm for in ISO datetime format (e.g., 2024-01-15T14:30:00)") String time) {
		return Map.of("status", "success", "report", "Your alarm is set for " + time);
	}
}

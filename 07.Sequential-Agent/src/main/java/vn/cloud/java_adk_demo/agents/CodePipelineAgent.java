package vn.cloud.java_adk_demo.agents;

import com.google.adk.agents.SequentialAgent;

public class CodePipelineAgent {

	public static SequentialAgent ROOT_AGENT =
			SequentialAgent.builder()
					.name("CodePipelineAgent")
					.description("Executes a sequence of code writing, reviewing, and refactoring.")
					// The agents will run in the order provided: Writer -> Reviewer -> Refactorer
					.subAgents(
							CodeWriterAgent.CODE_WRITER_AGENT,
							CodeReviewerAgent.CODE_REVIEWER_AGENT,
							CodeRefactorerAgent.CODE_REFACTORER_AGENT)
					.build();

}

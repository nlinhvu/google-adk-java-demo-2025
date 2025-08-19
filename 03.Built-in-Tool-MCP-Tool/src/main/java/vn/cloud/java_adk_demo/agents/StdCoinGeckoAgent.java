package vn.cloud.java_adk_demo.agents;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.adk.tools.mcp.McpTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.reactivex.rxjava3.core.Flowable;

public class StdCoinGeckoAgent {

	public static void main(String[] args) {
		// There are 2 ways to kick off a STDIO MCP SERVER in CoinGecko
		// 1. Remote Server (Public, Keyless)
		ServerParameters serverParameters1 = ServerParameters.builder("npx")
				.args(
						"mcp-remote",
						"https://mcp.api.coingecko.com/sse"
				)
				.build();
		// 2. Local Server (API Key Required)
		ServerParameters serverParameters2 = ServerParameters.builder("npx")
				.args(
						"-y",
						"@coingecko/coingecko-mcp"
				)
				.addEnvVar("COINGECKO_DEMO_API_KEY", "your_api_key")
				.addEnvVar("COINGECKO_ENVIRONMENT", "demo")
				.build();

		try (McpToolset mcpToolset = new McpToolset(serverParameters1)) {
			CompletableFuture<List<McpTool>> cfMcpTools = mcpToolset.loadTools();
			List<McpTool> mcpTools = cfMcpTools.get(1, TimeUnit.MINUTES);

			LlmAgent llmAgent = LlmAgent.builder()
					.name("StdCoinGeckoAgent")
					.model("gemini-2.5-pro")
					.description("An agent that provides cryptocurrency market data, prices, and analytics by interfacing with CoinGecko's comprehensive crypto API")
					.instruction("""
					You are the CoinGecko Agent responsible for providing cryptocurrency market data and insights to users.
					Your primary responsibility is to deliver real-time price and trend information for various cryptocurrencies.
		
					You have 2 specialized tools:
					1. 'get_simple_price': Use this tool to get the current price of one or more specific cryptocurrencies.
					2. 'get_search_trending': Use this tool to discover the cryptocurrencies that are currently trending based on user searches on CoinGecko.
		
					Analyze the user's query. If they ask for the price of a specific cryptocurrency by name or symbol, use 'get_simple_price'.
					If they ask a general question about popular or trending coins, use 'get_search_trending'.
					For anything else, respond appropriately or state you cannot handle it.
					""")
					.tools(mcpTools)
					.build();

			String appName = "CryptoADKAgent";
			String userId = "SpiderMan";
			InMemorySessionService sessionService = new InMemorySessionService();
			Session session = sessionService.createSession(appName, userId).blockingGet();

			Runner runner = new Runner(llmAgent, appName, null, sessionService);

			Content userMsg = Content.fromParts(Part.fromText("What is the current price of Bitcoin in USD?"));
			Flowable<Event> events = runner.runAsync(userId, session.id(), userMsg);
			events.blockingForEach(event -> System.out.println(event.stringifyContent()));
		}
		catch (ExecutionException | InterruptedException | TimeoutException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}

	}
}

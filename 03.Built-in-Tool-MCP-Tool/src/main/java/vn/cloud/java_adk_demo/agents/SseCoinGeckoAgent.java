package vn.cloud.java_adk_demo.agents;

import java.time.Duration;
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
import com.google.adk.tools.mcp.SseServerParameters;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.reactivex.rxjava3.core.Flowable;

public class SseCoinGeckoAgent {

	public static void main(String[] args) {

		SseServerParameters sseServerParameters = SseServerParameters.builder()
				.url("https://mcp.api.coingecko.com/sse")
//				.headers(Map.of(
//						"Authorization", "Bearer your-mcp-api-key" // header
//				))
				.timeout(Duration.ofSeconds(10L)) // request timeout to connect to sse server
				.sseReadTimeout(Duration.ofSeconds(30L)) // read timeout to tool call
				.build();

		try (McpToolset mcpToolset = new McpToolset(sseServerParameters)) {
			CompletableFuture<List<McpTool>> cfMcpTools = mcpToolset.loadTools();
			List<McpTool> mcpTools = cfMcpTools.get(1, TimeUnit.MINUTES);

			LlmAgent llmAgent = LlmAgent.builder()
					.name("SseCoinGeckoAgent")
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

			Content userMsg = Content.fromParts(Part.fromText("What are the top 3 trending coins on CoinGecko right now?"));
			Flowable<Event> events = runner.runAsync(userId, session.id(), userMsg);
			events.blockingForEach(event -> System.out.println(event.stringifyContent()));
		}
		catch (ExecutionException | InterruptedException | TimeoutException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}

	}
}

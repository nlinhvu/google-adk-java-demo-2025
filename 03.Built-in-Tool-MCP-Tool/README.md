# Google ADK for Java: Built-in Tool and MCP Tool

This project demonstrates how to use Google's Agent Development Kit (ADK) for Java to create intelligent agents with `built-in tools` and `mcp tools`.

## How to run

1. Clone this repository
2. Ensure Java 21+ is installed
3. Ensure Google API Key is available as an Environment Variable
   ```properties
   GOOGLE_API_KEY=your_key_here
   GOOGLE_GENAI_USE_VERTEXAI=false
   ```
4. Clean and build the project:
   ```bash
   ./gradlew clean build
   ```
5. Run the development server using the Gradle task:
   ```bash
   ./gradlew runAdkWebServer
   ```
6. Open your browser and navigate to `http://localhost:8080`
7. Test the `GoogleSearchAgent` through the provided UI
8. Test `StdCoinGeckoAgent` by running:
   ```bash
   ./gradlew runStdCoinGeckoAgent
   ```
9. Test `SseCoinGeckoAgent` by running:
   ```bash
   ./gradlew runSseCoinGeckoAgent
   ```

# Google ADK for Java: Built-in Tool and MCP Tool

## 1. Initiate a Project from Scratch 
* Create Empty Java Project
* Add `Google ADK for Java` dependencies
* Provide `Google API Key` as `environment variables` 

## 2. Built-in Tool
> **Note:** Java only supports `Google Search` and `Code Execution` tools currently.

### Limitation: 
1. A `built-in tool` along with `other tools` within a single agent is **NOT** currently supported:
```java
 LlmAgent searchAgent =
    LlmAgent.builder()
        .model(MODEL_ID)
        .name("SearchAgent")
        .instruction("You're a specialist in Google Search")
        .tools(new GoogleSearchTool(), new YourCustomTool()) // <-- not supported
        .build();
```
2. `Built-in tools` **CANNOT** be used within a `sub-agent`:
```java
LlmAgent searchAgent =
    LlmAgent.builder()
        ...
        .tools(new GoogleSearchTool())
        .build();

LlmAgent codingAgent =
    LlmAgent.builder()
        ...
        .tools(new BuiltInCodeExecutionTool())
        .build();


LlmAgent rootAgent =
    LlmAgent.builder()
        ...
        .subAgents(searchAgent, codingAgent) // Not supported, as the sub agents use built in tools.
        .build();
```

* Create a **GoogleSearchAgent** `LLMAgent` that leverages `Google Search` tool
* Test **GoogleSearchAgent** with `DEV UI Server`

## 3. MCP Tool

> **Note:** This feature is **NOT** stable yet as we're using `Google ADK for Java` **_v0.2.0_**, there are still lots of things need to be improved.
> Most of the time, it won't work with `Web UI Server` **_v0.2.0_**, that's why we will test our `Agents` in the `main method`.

### 3.1. STDIO MCP Server
* Create a **StdCoinGeckoAgent** `LLMAgent` that leverages `MCP Tool` tool to connect to the `STDIO MCP Server`
* Test **StdCoinGeckoAgent** in the main method

### 3.1. SSE MCP Server
* Create a **SseCoinGeckoAgent** `LLMAgent` that leverages `MCP Tool` tool to connect to the `SSE MCP Server`
* Test **SseCoinGeckoAgent** in the main method

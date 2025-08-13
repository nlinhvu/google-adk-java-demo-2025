# Google ADK for Java: Function Tool

## 1. Explain Terminologies
What is Tool Calling? Function Calling, Tool Use

## 2. Create an empty Java Maven/Gradle Project
I’ll start with `Spring Initializer`, then remove all of `Spring Dependencies`, \
you can simply start with a simple `Maven/Gradle project`, I choose `Spring Initializer`, \
because it helps me create **the project structure**, providing **.gitingore**, **.gitattributes** files, \
and **maven or gradle wrapper** in advance

## 3. Build a simple LLMAgent with tools and start DEV UI Server to test it
* Create an **AlarmAgent** `LLMAgent`
* Create 2 functions `getCurrentTime` and turn them into `FunctionTool`s
* Test **AlarmAgent** with DEV UI Server

# Google ADK for Java: Getting Started

## 1. Create an empty Java Maven/Gradle Project
I’ll start with `Spring Initializer`, then remove all of `Spring Dependencies`, \
you can simply start with a simple `Maven/Gradle project`, I choose `Spring Initializer`, \
because it helps me create **the project structure**, providing **.gitingore**, **.gitattributes** files, \
and **maven or gradle wrapper** in advance

## 2. Build a simple LLMAgent and run it, all in the main method
* Create an `LLMAgent`
* Create a `Session` that represents the `User`
* Create a `Runner` to run a `User's message` with `his corresponding Session`

## 3. Run DEV UI Server to test our Agent
We also have to follow some rules to make our Agents available to the DEV UI Server

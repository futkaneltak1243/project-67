# Voice Command Parser Specification

## Objective
Define how to convert natural language text from voice input into structured, executable application commands.

## Input
A raw text string (e.g., from a speech-to-text engine).

## Output
A structured data object representing a specific command. This could be implemented using a Kotlin sealed class hierarchy, where each subclass represents a distinct command type with its specific parameters.

## Example Commands and Structured Output
Below are examples of natural language voice commands and their corresponding structured output representation.

### File Creation
- **Voice Command:** `create class MyService`
- **Structured Output:** `CreateFile(name="MyService", type=CLASS)`

### Function Addition
- **Voice Command:** `add function getUser returning User`
- **Structured Output:** `AddFunction(name="getUser", returnType="User", params=[])`

### Navigation
- **Voice Command:** `go to file UserRepository`
- **Structured Output:** `NavigateToFile(fileName="UserRepository")`

### Git Operations
- **Voice Command:** `commit with message "feat: add user login"`
- **Structured Output:** `GitCommit(message="feat: add user login")`

## Initial Parsing Strategy

An initial parsing strategy could involve a multi-step approach:

1.  **Keyword Identification:** Identify specific keywords or phrases at the beginning of the input string that strongly indicate a command type (e.g., 'create class', 'add function', 'go to file', 'commit with message').
2.  **Regular Expressions:** Once a command type is identified, use regular expressions tailored to that command to extract specific parameters (e.g., file names, function names, return types, commit messages).
3.  **Parameter Validation:** Validate extracted parameters against expected types or known entities (e.g., ensuring a file name is valid, checking if a return type is a known class).
4.  **Structured Object Creation:** Construct the appropriate structured command object (e.g., `CreateFile`, `AddFunction`) with the extracted and validated parameters.
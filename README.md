# Engineering Challenge - Dataico
Presented by Juan Camilo Rojas - 2023

This repository contains my answers to the Clojure Challenge described in the `problem_description.md` file. Here are some important points regarding my solutions:

1. **Source Code Files**: You will find all the source code files in the `src` directory, including `invoice.json` and `invoice_item.clj`. These files were provided as part of the challenge and are used in my solutions.

2. **Problem 2**: In `problem2.clj`, I had to copy the specs into the same source code file. I attempted to import them from the original file provided in the challenge (`invoice_spec.clj`), but the compiler didn't recognize the specs. By pasting them into the same file, I was able to successfully run the program with recognized specs.

3. **Problem 2 Invoice**: It's important to note that the given invoice for problem 2 (`invoice.json`) does not meet the specified requirements of the given spec. So, I read the JSON file and construct a map that pass the specs, doing the necessary changes to the original map extracted from the JSON. 

4. **Problem 3 Tests**: I have written tests for `problem3.clj`, but I encountered an error every time I tried to execute the program. The error message was: "Could not locate `clojure/test'__init.class`, `clojure/test'.clj`, or `clojure/test'.cljc` on the classpath." This issue is unusual because I could locate the `clojure/test.clj` file in the project packages, and the compiler recognized it. However, during execution, the program encountered this error. I did not find a documented solution for this issue in the available documentation or forums. Nevertheless, I have written the tests and gained a good understanding of Clojure's testing features.

Please feel free to use the revised text for your README. If you have any further questions or need more assistance, please don't hesitate to ask.

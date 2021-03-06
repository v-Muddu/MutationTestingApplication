**Launcher Application**
_Java bytecode Mutation_

Applications and their repositories:
Instrumenting Application(ast-instrumentation) - https://github.com/v-Muddu/InstrumentationApp
Instrumented Application (Apache Commons Lang) - https://github.com/apache/commons-lang)

The instrumented application is the input to the current project "launcher"

**Overview**
In this assignment, we implement bytecode manipulation during runtime using javassist.

As we introduce the concept of mutation testing. Our launcher takes in the test classes and the unmutated bytecode from the previous homework as inputs to run. We’ve performed mutation on the bytecode that covers atleast two mutation operators from each category.
We inject a few of the mutators and manipulate the bytecode and this is all done in the runtime of our launcher.
We then run the test cases and find the trace of the original class file and mutated class file happening concurrently using the executor service framework. We execute the entire task in threads, We then compare the trace of each mutated file with the original file’s trace and collects all the traces in a log file. Later, using String comparision, original log file is compared against the log file that contains the traces of the mutated code. A boolean is returned in the console for each comparision stating whether the traces match or not, if they do then it returns true and false if they don’t.

**Project Setup**
Once the application is downloaded, follow these steps to setup the project:

1.  Set the following properties in the config.properties file according to your file system
    
    //path to the bytecode of your input classes
    sourceClassPath= <Add the path to the source files in instrumented application>
    
    //path to the bytecode of the test classes.
    testClassPath=<Add the path to the test files in instrumented application>
    
    //path to the directory storing mutated output
    mutationClassPath=<add the path to the mutation output directory mutatedFiles. Its inside the project structure itself>
    
    //path to the trace file
    traceFilePath=D:\\application.log
    
    //path to the output csv file. Used to view the results of tests against mutated classes
    csvFilePath=D:\\output.csv

**How to run the Launcher application?**
Make changes to the config file.
If you’re a mac user then please replace `\\` with `\` or else the execution will end with exceptions. Also do this inside
project code wherever applicable.
Once the setup is finished in the IDE, open Main.java and run.

**Implemention of the Junit test cases details:**
Execute the launcher applications test cases using:
`gradle test`

**To build the Launcher application, execute to following command on the IDE’s terminal/command prompt**
`gradle build`


**Explanation of the implementation**
Concurrency:
We are using the ExecutorService framework to run multiple threads concurrently

Mutation Operators:
The operators to be applied are inside the operators package. We have implemented the following 8 operators:
1. Access Modifier Operator
2. Default Constructor Deletion Operator
3. Hiding Variable Deletion Operator
4. Java Static Modifier Deletion Operator
5. Java Static Modifier Insertion Operator
6. Overloading Method Deletion Operator
7. Overriding Method Deletion Operator
8. Parent Member Declaration Operator

Each operator is implemented as a thread and passed to the executor service framework for execution

Each operator class performs its own mutation and stores the mutated class files in the output directory.
Next we use two Class loader instances:
1. First class loader loads the original class files and executes test cases.
2. Second class loader loads the mutated class files and executes test cases.

Finally the trace generated from the above two executions is compared and result is obtained in boolean form.
Above step is done for each class under each mutation operator. The output is stored into the csv file in the following format:

Operator Name | Class Name | Mutation Applied (Y/N) | Mutation Detected (Y/N)

Currently the mutations are applied to all classes, however the execution and trace generation is taking a long time.
Thus, we have limited the execution part to only 10 classes(for which mutation has been applied) under each operator. 

If the execution takes too long, please go to Main class and pass lesser number of operators to the ExecutorService object.

The output can be seen in the csv file and console too.


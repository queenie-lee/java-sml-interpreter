# Simple (Small) Machine Language (SML)

### A coursework examining reflection API and dependency injection

+ This assignment is to be completed **individually**, and without any AI tools or assistants. 
+ The sample code mentioned in the text can be found in this repository.

<img src="sml.jpg" alt="sml"/>

The aim of this assignment is to give you practice with 

+ subclasses, 
+ modifying existing code, 
+ testing,
+ the use of reflection,
+ and dependency injection,

amongst other skills.

## Details

**Name**: Queenie Lee

**Your userid**: qlee02

Please do not forget to complete this part as too often we have "orphaned" repositories,
which can require months to sort out.

## The problem

In this assignment you will write an interpreter for a simple machine language — `SML`. 

The machine is similar to the Java Virtual Machine: it has no registers or flags and is based on a stack.
Recall that a stack is a last in, first out (`LIFO`) data structure that has two operations — push and pop.
The stack of our machine consists of *frames*. Each method invocation creates a new frame, which is used to
store 

- values of method arguments and local variables, which are accessed by referring to their name,
- operands of the instruction in the so-called *operand stack* 
  (so instructions can pop their arguments and push the result back on the operand stack) and
- the value of the *program counter*, which indicates which instruction of the method will be executed next.

We refer to the frame of the currently executed method as the *current frame* and its operand stack 
as the *current operand stack*. When the current method returns, its frame (including the operand stack) 
is simply destroyed, and the frame that had invoked the returning method becomes the current frame.
Most instruction read operands from the current operand stack and push the results back onto the
current operand stack. This `SML` supports only one data type, Java's `int`. 


The general form of an `SML` instruction is:

```
	[label:] opcode parameter-list
```
where

* `label` — is the optional label for the line. It is a sequence of non-whitespace characters.  
	Other instructions might “jump” to that label. 
* `opcode` — is the actual instruction name (operation code).
	In `SML`, there are instructions, for example, for adding and comparing integers, for storing and reading them from the method arguments and local variables, 
    and for conditionally branching to other labels  (like an `if` statement).
* `parameter-list` — is the comma-separated list of parameters for the instruction. Parameters can be
  - integer numbers (`I` in the table below), 
  - variable names (`V` in the table below),
  - labels for branching instructions (`L` in the table below),
  - method names for method invocation (`M` in the table below).

SML has the following types of instructions:

| Instruction  | Interpretation                                                                                                                                                                                                                                                                                                                                                                                                                                                |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `load V`     | load variable: push the value of the method argument or local variable `V` onto the current operand stack                                                                                                                                                                                                                                                                                                                                                     |
| `store V`    | store variable: pop a value from the current operand stack and store it in the method argument or local variable `V`                                                                                                                                                                                                                                                                                                                                          |
| `push I`     | push a constant: push the value `I` onto the current operand stack                                                                                                                                                                                                                                                                                                                                                                                            |
| `add`        | add: two integers, `value1` and `value2`, are popped from the current operand stack, and the result, `value1 + value2`, is pushed back onto the current operand stack                                                                                                                                                                                                                                                                                         |
| `sub`        | subtract: two integers, `value1` and `value2`, are popped from the current operand stack, and the result, `value1 - value2`, is pushed back onto the current operand stack                                                                                                                                                                                                                                                                                    |
| `mul`        | multiply: two integers, `value1` and `value2`, are popped from the current operand stack, and the lower 32 bits part of the result, `value1 * value2`, is pushed back onto the current operand stack                                                                                                                                                                                                                                                          |
| `div`        | divide: two integers, `value1` and `value2`, are popped from the current operand stack, and the result, `value1 / value2`, is pushed back onto the current operand stack                                                                                                                                                                                                                                                                                      |
| `goto L`     | goto: execution proceeds from the specified label `L` in the current method                                                                                                                                                                                                                                                                                                                                                                                   |
| `print`      | print: an integer, `value`, is popped from the current operand stack and printed on screen                                                                                                                                                                                                                                                                                                                                                                    |  
| `if_cmpgt L` | branch: two integers, `value1` and `value2`, are popped from the current operand stack and compared; if `value1 > value2`, then execution proceeds from the specified label `L` in the current method; otherwise, the next instruction is executed                                                                                                                                                                                                            |
| `if_cmpeq L` | branch: two integers, `value1` and `value2`, are popped from the current operand stack and compared; if `value1 = value2`, then execution proceeds from the specified label `L` in the current method; otherwise, the next instruction is executed                                                                                                                                                                                                            |
| `invoke M`   | invoke a method with `k` arguments: `k` values, `value1`, ..., `value_k`, are popped from the current operand stack; a new frame is created for method `M`; these values are stored in the arguments in the new frame: `value1` is stored in the first argument in the new frame, and so on; the local variables in the new frame are initialised to `0`; the new frame is then made current, and execution proceeds from the first instruction of method `M` |
| `return`     | return int from method: an integer, `value`, is popped from the current operand stack and pushed onto the invoker's operand stack; the current frame is destroyed, and execution continues from the next instruction of the invoker                                                                                                                                                                                                                           |



Here is an example of an `SML` program to compute the `n`th Fibonacci number using a simple recursive algorithm (see `test1.sml` in the `resources` folder):

```
@fib: n 
    load n
    push 1
    if_cmpgt L7
    push 1
    return
L7: load n
    push 1
    sub
    invoke @fib
    load n
    push 2
    sub
    invoke @fib
    add
    return
```

Note that labels end with a colon (but the colon is not part of the label's name),
and that label, opcode and parameters are separated by whitespace, 
with commas between parameters (if needed).

A program is a collection of methods (each method declaration begins with an `@` label). 
A method declaration also specifies its arguments in 
a comma-separated list after the method name.
In our example method `@fib`, we have one argument, `n`.
In the `test2.sml` example, we have method `@fib2`, also with one argument, `n`. Note that
variable names `fm2`, `fm1`, `i` and `f` used in its instructions are the local variables of `@fib2`.

Execution of a program starts with method `main`.
The instructions of a method are executed in order (starting with the first one), 
unless the order is changed by execution of a "jump" instruction such as `goto` or `if_cmpgt`. 
Execution of a method terminates when it reaches a `return` instruction. Note that a 
program is incorrect if it contains a method that does not end with a `return` instruction.

Your interpreter will:

1. Read the name of a file that contains the program from the command line 
(via `String[] args` and the `RunSml` class).
2. Read the program from the file and translate it into an internal representation (classes `Machine`, `Method`, `Instruction` and others).
3. Execute the program and print its output.

This looks like a tall order, but have no fear; 
we provide you with some of the code, so you can concentrate on the interesting 
use of subclasses, dependency injection and reflection. 

Completing the worksheets really helps as preparation for this assignment.

## Design of the program

We provide some of the classes, specifications for a few, and leave a few others 
for you to write/complete. The code we provide does some of the dirty work of reading 
in a program and translating it to an internal representation; you can concentrate on the 
code that executes the program. 

We suggest that you examine the `Machine` class first, as it is the heart 
of the program (you can use the `main` method in the `RunSml` class to guide you as well).

## Studying the program

You are provided with some skeleton code which is on this repository.

Look at the fields of the `Machine`, `Method`, `Instruction`, `SymbolTable`, `Label` and `Variable` classes, 
which contain exactly what is needed to execute an `SML` program.
Next examine the method `Machine.execute` and classes `Frame`, `InvokeInstruction` and `ReturnInstruction`, 
which describe execution of the program. 
It is a typical *fetch-decode-execute* cycle that all machines have in some form. 

At each iteration, the instruction to execute is fetched, the instruction is executed and 
the program counter is updated. In most cases, the program counter is simply incremented 
to move to the next instruction in the program (see method `advance` in `Frame`); 
some instructions (e.g., `if_cmpgt`) can change the order of execution by jumping to a specific label 
(see method `jumpTo` in `Frame`).

The `Translator` class contains the methods that read in the program and translate 
it into an internal representation; be warned, very little error checking goes on here. 

Finally, study the `main` method of the `RunSml` class (if you think it will help you).

## The `Instruction` class and its subclasses

All the programming that you do has to do with the `Instruction` class and its subclasses. 
The specification of the class `Instruction` has been given to you — open the file

```
	Instruction.java
```
and examine it. This class is *abstract*, because it should not be instantiated.  
The method `execute` is also abstract, forcing every concrete subclass to implement it. 
Every instruction has an optional *label* and an *operation code* — that is exactly 
what is common to every instruction.  Therefore, these properties (fields) are maintained 
in the base class of all instructions.

## Tasks

There are two components to this coursework assignment.

##### Part I

1. Complete the methods in the `Instruction` class — this may require you to add some fields, 
which should be *protected*, so that they are accessible in all subclasses.

2. Now create a subclass of `Instruction` for each kind of `SML` instruction and fix 
the method `Translator.instruction` so that it properly translates that kind of instruction.

   *Recommended*: write one instruction at a time and test it out thoroughly, before proceeding to the next!

3. Start with the inspection of the `print` instruction, 
because the implementation of this `Instruction` subclass and 
the code for translating it is already there — in method `Translator.getInstruction`.  

4. For each instruction, the subclass needs appropriate fields, a constructor, 
and methods `execute` and `toString`; these should override 
the same methods in the `Instruction` class, with appropriate annotations.

5. As you do this, you will see that each successive class can be written by 
duplicating a previous one and modifying it. 
Introduce auxiliary abstract classes where that can help avoid code duplication. 

6. Write a test class for each of the `Instruction` subclasses.

7. After you finish writing a subclass for an `SML` instruction, 
   you will have to add code to the method `Translator.getInstruction` to translate 
   that instruction. The existing code for translating `print`, `goto`, `invoke` and `return` 
   should help you with this.

8. There are also a few places in the code with `TODO:` labels — follow the instructions to
   improve the provided code (or implement missing methods as required). 
   Use the Java Stream API whenever possible instead of loops.

##### Part II

1. Next, take the `switch` statement in `Translator.java` that decides which type of instruction is created 
   and modify the code so that it uses *reflection* to create the instances, i.e., 
   remove the explicit calls to the subclasses and the `switch` statement. 
   This will allow the `SML` language to be extended without having to modify the original code.
   Remember that your reflection code should not mention any instruction subclass names. It should also be 
   fairly general to accommodate new types of instructions, for example, bitwise operations on integers that work similarly to `add`
   or branching on "less than" or "not equal" and so on.

2. Modify the source code to use *dependency injection*, the *singleton* design pattern, 
   and *factory* classes where you deem appropriate. (You are allowed to use other 
   design patterns if you consider them necessary.) Be careful not to introduce, for example, singletons
   where the class is expected to have multiple instances (for example, `Machine` is not meant to be a singleton).

3. Apart from the specific code mentioned above you should not modify other classes.

All of these parts of the coursework should be fully tested (you do not need to provide 
tests for the original codebase).

## Submission

Your repository will be *cloned* at the appropriate due date and time.

------

###### Individual Coursework 2024-25

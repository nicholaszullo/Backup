# CS 1632 Midterm 2 Exam Study Guide - Spring 2021

The exam will be posted on GradeScope just like before and you will have a 3
hour span to complete the exam at a time of your own choosing.  Remember that
while the exam is open book, you are forbidden from getting help from anybody,
as usual.  Also, during the exam period, you will not be able to ask me any
questions about the exam except clarification questions.

The midterm will cover everything we have covered since Midterm 1, starting
from Automated System Testing.  I recommend you review the slides and the
textbook (See syllabus.md for which chapters are required reading.  The reading
is also on the last slide of each lecture).  You are also expected to have
completed all exercises since then be able to answer related questions.

The exam will consist of these types of questions:
  * Multiple choice questions
  * Fill in the blank questions
  * Short answer questions (explain a concept, discuss pros/cons, etc.)
  * Code tracing questions

Here are the key topics to study in preparation for the test.

_Note: the items in italic are learning goals that require application of your
knowledge.  Either you have to apply your knowledge to a piece of code, or you
need to apply an algorithm to a specific problem._

## AUTOMATED (WEB) SYSTEMS TESTING
* Be able to explain why testing a GUI web application involves a lot of text
  testing, just like text UI applications.
  - Website contain a lot of text, and functionality like links are able to be checked with text
* Be able to discuss reasons why testing an HTML verbatim against an expected
  HTML page is not desirable and how Selenium solves those problems.
  - Is not able to check dynamic behavior from JavaScript. Selenium runs the page live like a user would interact with.
* Be able to explain why in Selenium, there is an option to select
  different locator strings for the same target element.
  - XPath allows for more complicated operations than a simple css selector.

## PERFORMANCE TESTING
* Be able to compare different definitions of speed: throughput,
  responsiveness, and utilization
  - Throughput: Number of users that can use a resource in a given amount of time
  - Responsiveness: time taken for resources to become usable to the user, average response time
  - Utilization: How much of the system resources a given resource needs to use to be functional
* Be able to apply appropriate definition of speed according to context (type
  of application).
  - Web browser: Speed means response time to user application
  - Web Server: How quickly responds to request, throughput
* Be able to define performance indicator, KPIs, performance targets,
  performance thresholds.
  - Performance indicators: Quantitative measures of performance of a system under test
  - KPIs: Key Performance Indicators
  - Performance targets: Set by user/buyer to reach
  - Performance thresholds: What it actually is
* Be able to explain different categories and subcategories of performance
  indicators: service-oriented indicators / efficiency-oriented indications.
  Also, be able to explain the reasoning behind the categorization.
  - Service oriented: Quality of Service, how a typical end user experiences: Response time (quickly processes, how long to load), availability (days per year website runs)
  - Efficiency: How well it uses system resources, computational and energy efficiency. Utilization and throughput
* Be able to discuss reasons why statistical reasoning is mandatory when
  measuring response time.  Be able to list examples of factors that can cause
  variability when measuring response time.
  - Need to account for variability, quickest response time doesn't help, need average. Other processes, data is different, network traffic. Long tail of latency.
* Be able to define real time (wall clock time), user time, system time, and
  total time accurately.  Be able to explain why certain types of time may be
  important when measuring certain performance indicators.
  - Real Time: The actual time in the world to complete the task
  - User Time: time spent running user code on the cpu, important for optimize your code
  - System time: Time spent running system code on the cpu, important for use different resources
  - total time: User + System 
  - Real = total + idle / CPUs
* Be able to explain what the "nines" notation means for availability.
  - 1 9, 90%. 2 9, 99%, 3 9, 99.9%
* Be able to explain why availability is hard to measure directly.
  - Not feasible to run a few "test years" to see how long it was available during the year
* Be able to explain how baseline, soak (stability), and stress load testing
  can help model availability.
  - Soak: Typical, extended period use
  - Stress: Short period, high load
* Be able to explain what value efficiency-oriented indicators provide beyond
  service-oriented indicators, which already measure user experience.
  - Need to know for usage costs, optimizations to help increase throughput and increase user experience
* Be able to differentiate between general purpose and program-specific
  utilization measurement tools.

## STOCHASTIC / PROPERTY-BASED TESTING
* Be able to define accurately what stochastic testing is.
  - Random input testing
* Be able to define accurately what property-based testing is.
  - Test properties of the output, not 1 to 1 match
* Be able to explain why property-based testing is necessary for stochastic
  testing.
  - Impossible to write a correct output for stochastic test, test oracle problem.
* Be able to define what an invariant is.
  - Assertion that must be true at that point of the program. Property of output that is the same for all in that category.
* Be able to discuss the advantages / disadvantages of property-based testing.
  - Write once, all outputs can use it. Does not guarentee correct output.
* Be able to explain why "shrinking" in quickcheck is useful for debugging.
  - Finds the easiest possible occurance of the defect to be able to debug better.
* Be able to define what fuzz testing is.
  - Stochastic testing with byte stream inputs, randomize bytes 
* Be able to explain why complete random input generation when fuzz testing is
  ineffective, using an example.
  - Random strings to test strings will not work, too many characters. Limit to A-Z characters. 
  - HTML needs an html template to generate from, random garbage won't be valid html
* _Be able to come up with invariants of your own given a piece of (pseudo)
  code._

## STATIC TESTING PART 1
* Be able to discuss the pros / cons of static testing compared to dynamic
  testing.
  - Easier, no test cases. Pinpoints defect exactly. Finds some that dynamic miss.
  - Finds false positives, false negatives
* Be able to discuss why choice of language is important for compiler static analysis.
  - (?) Need to know syntax of that language to check for compiler (?)
* Be able to explain the differences between different types of code coverage.
  - MEthod coverage: How many methods are called
  - Statement coverage: How many statements are executed. Usually what is meant by code coverage.
  - Branch coverage: How many if/else branches visited
  - State Coverage: If program is FSM, how many visited. Best but not practical.
* Be able to explain why 100% code coverage necessarily does not mean defect
  free.
  - Only means that each statement is run, can't check if logic is correct
* Be able explain similarities between linters and bug finders (pattern
  matching) and the differences (usage).
  - Both look for patterns in the code of known bugs, or of language style.
  - Linters are used to fix style, bug finders find bugs

## STATIC TESTING PART 2
* Be able to define what formal verification is.
  - Use mathematical proofs to show a program will output correct output
* Be able to explain how theorem proving seeks to achieve formal verification.
  - By showing a mathematical proof, you can prove that the output will be correct.
* Be able to explain how model checking seeks to achieve formal verification.
  - Checks all the states, ensure invariants are met at each
* Be able to discuss pros / cons of theorem proving compared to model checking.
 - Model checking: Easier, faster, might state explode
 - Theorem proving: Hard, time consuming, accurate
* Be able to compared the similarities and differences of model checking
  compared to property-based testing.
  - Checks properties along the way, property-based only can do outputs.
* Be able to explain how backtracking and state matching both help in make
  state space exploration efficient for model checkers.
  - prevents duplicate states from being created, waste of time and space.
* Be able to explain what the state explosion problem is.
  - Duplicate states continue to be created, unnecessary states are checked and considered. 
* _Given code, be able identify parts of it that leads to state space explosion._
* _Given a piece of code, be able to create a state transition diagram._
* _Given a state transition diagram, be able to draw backtracking arrows._

## STATIC TESTING PART 3
* Be able to explain how symbolic execution can drastically reduce the size of
  the state space.
  - Each possibility for a number can be reduced to 1 state using a symbolic representation, or a few states based on properties.
* Be able to explain what role a constraint solver plays in symbolic execution.
  - Solves symbolic expressions to ensure a constraint is met
* Be able to explain the deficiencies of symbolic execution and why it cannot be easily applied to all programs.
  - Rigid system based on numbers, can't work in situations where complex behavior occurs
* _Given code, be able to trace through the code, creating a symbolic
execution tree in the process.  Each statement in the tree should have an
associated path condition.  Also, if the statement is an assignment, a symbolic
expression should be assigned to the variable instread of a concrete value._

## PAIRWISE / COMBINATORIAL TESTING
* Be able to interpret the results of the NIST study on the frequency of
  defects for various numbers of variable interactions.
  - All defects involve under 6 total interactions between variables
* Be able to define what pairwise testing is and what combinatorial testing is.
  - Testing interaction and behavior of a program when pairs or combinations of variables are active
* Be able to define what a covering array is.
  - Table showing true/false for different variable interactions
* Given that the size of the covering array is O(v^t * logk), be able to
  explain its implications; i.e. why it is possible to do combinatorial testing
  with good defect coverage even for large programs in terms of v, t, k.
  - NP-Hard to find the optimatl coveraging arrray, but most extra work doesn't add asymptotically more time.
  - Almost all defects involve under 6 variable interactions, testing 6 combinations isn't too long
* _Be able to create a covering array for the given list of parameters and the
  given number of interactions (t)._

## SMOKE / EXPLORATORY TESTING
* Be able to explain why smoke testing can help the QA team to work more efficiently.
  - Prep system for serious testing, avoid wasting time
* Be able to tell other names for smoke testing.
  - Confidence testing, sanity testing, build verification testing
* Be able to explain how build verification testing is different the unit
  testing during TDD and what value it adds.
  - Test the entire system in all aspects, failure means full stop of deployment pipeline.
* Be able to discuss the choice you have on when BVT is triggered and the trade-offs.
  - Every commit: Expensive, time consuming, ensures best system reliability
  - Periodically: Cheaper, quicker, verify regression hasn't happened.
* Be able to explain what happens on a BVT pass or fail.
  - On a pass, ready for next steps. On a fail, revert to a safe version, patch bug, try again
* Be able to explain clearly why BVT has to be fast (around 10 minutes).
  - On a fail, revert all changes while it was run, don't want to waste developer time.
  - No commit can happen until another pass is achieved.
* Be able to explain the situation where exploratory testing may be needed.
  - When a new feature hasn't been fully hashed out yet
  - Some requirements are still subjective/implicit, detailed tests not in place
* Be able to compare the pros/cons of exploratory testing.
  - Learn more about the system, influence development of system
  - Unregulated, unrepeatable, unknown coverage

## SECURITY TESTING
* Be able to list the Infosec (CIA) triad
  - Confidentiality: no unauth can read data
  - Integrity: no unauth can write data
  - Availability: System is availabe for read/write
* Be able to tell which of the triad a particular attacks
  tries to compromise (confidentiality, integrity, availability).
  - Confidentiality attack: Eacesdropping, keylogging, phishing.
  - Integrity: Modification/Fabrication, digital signature forgery
  - Availability: Interruption, DoS Attack
* Be familiar with the terminology of vulnerability, exploit, attack.
  - Vulnerability: Weakness of the system
  - Exploit: Mechanism for compromising a system
  - Attack: Actual compromising of system
* Be able to tell the differences between the numerous types of malicious code
  and be familiar with the terminology.
  - Malware: General term for malicious code
  - Spyware: monitors your actions
  - Adware: shows more ads when browsing
  - Randomware: theatens to publish data or block data for money
  - Bacteria: program that consumes system resources, fork bomb
  - DoS: Flood server to shut down
* Be able to explain how each of the 8 common types of attacks are perpetrated
  and possible protections.
  - Logic bomb: hidden code that executes when logic is true
  - Trapdoor: secret code that has undocumented access to system
  - Trojan horse: Pretends to be normal code
  - Virus: self replicates with human intervention
  - Worm: replicates without human intervention
  - Zombie: program ran by unauth controller
  - Bot network: collection of zombies
* Be able to explain what Single Origin Policy (SOP) is in web security.
  - Code must come from same url origin
  - Website frames can only access code/data from that frame, not rest of site.
* Be able to explain how cross-site scripting circumvents SOP.
  - If your website allows execution of arbitrary code, it is vunerable. Place code in <script> tag that gets executed when loaded
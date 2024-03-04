# Cron Expression Evaluator
This command-line application parses multiple cron expressions and expands each field to show the times at which it will run.

# Usage
**1. Clone the repo using ssh command:**  
git clone https://github.com/akshayvadicharla/CronExpressionEvaluator.git

**2. Open Intellij and open Cloned Repository.**

**3. Run the main method.**

**4. Change input cron expression and re run to get the results.**


# Follow the prompts:

The application will automatically evaluate the provided cron expressions.
Output for each expression will be displayed on the console.

**Sample Input and Output**

Input Expression is : 

*/15 0 1,15 * 1-5 /usr/bin/find

Output Expression is :

minute         0 15 30 45
hour           0
day of month   1 15
month          1 2 3 4 5 6 7 8 9 10 11 12
day of week    1 2 3 4 5
command        /usr/bin/find

# Cron Expression Format
The cron expression consists of five time fields followed by a command:

Minute (0-59)
Hour (0-23)
Day of Month (1-31)
Month (1-12)
Day of Week (1-7)
Command can be any string

Each field can be a single value, a range, a list of values separated by commas, or a step value.

# Language Used
Java 

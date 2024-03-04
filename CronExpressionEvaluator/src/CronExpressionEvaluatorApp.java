import java.util.ArrayList;
import java.util.List;

public class CronExpressionEvaluatorApp {
    public static void main(String[] args) {
        try {

            String[] inputCronExpressions = new String[] {
                    "*/15 0 1,15 * 1-5 /usr/bin/find",
                    "* * * * * /usr/bin/find",
                    "3/15 0 1-15 * 1-7 /usr/bin/find",
                    "1-10 2/23 1,15 * 1-5 /usr/bin/find",
                    "0/15 0-10 1/15 * 1-7 /usr/bin/find"
            };
            int size = inputCronExpressions.length;
            int i = 0;
            while (i < size) {
                System.out.println("Input Expression is : " + inputCronExpressions[i]);
                System.out.println("Output Expression is : ");
                evaluateCronExpression(inputCronExpressions[i]);
                i++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void evaluateCronExpression(String cronExpression) {

        // fields orders in the cron expression
        String[] fields = {
                "minute", "hour", "day of month", "month", "day of week"
        };

        // separating each field expression using space
        String[] cronValues = cronExpression.split(" ");

        // last expression is command
        String command = cronValues[cronValues.length - 1];

        // for storing result size = 6  --> 5 for cron expression + 1 command expression
        String[] result = new String[6];
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String value = cronValues[i];

            // extracting expanded values for current cron value and field
            String[] expandedValues = extractExpandedValuesFromCurrrentCronValueAndField(value, field);

            // updating result after formatting expanded values
            result[i] = formatOutput(field, expandedValues);
        }

        // adding command to the output
        result[5] = formatOutput("command", new String[]{command});


        // printing the result as per requirement
        for (int i =0;i<6;i++) {
            System.out.println(result[i]);
        }
    }

    private static String[] extractExpandedValuesFromCurrrentCronValueAndField(String cronValue, String field) {
        if (cronValue.equals("*")) {
            // find array from range start to end for corresponding field.
            return rangeToArray(getRangeStart(field), getRangeEnd(field));

        } else if (cronValue.contains(",")) {
            // fetches all comma separated values with validation of data
            return expandCommaSeparatedValuesWithDataValidation(cronValue, field);

        } else if (cronValue.contains("-")) {
            // fetches expanded range of Cron Value with data validation
            return expandRangeCronValueWithDataValidation(cronValue, field);
        } else if (cronValue.contains("/")) {
            // fetches all the steps data of Cron Value with data validation and starting value
            return expandStepDataOfCronValueWithDataValidation(cronValue, field);
        } else {
            return new String[]{cronValue};
        }
    }

    private static String[] expandCommaSeparatedValuesWithDataValidation(String cronValue, String fieldType) {
        String[] subfields = cronValue.split(",");
        List<String> expandedValues = new ArrayList<>();
        for(String subfield : subfields) {
            // validating fields
            validateFieldValueWithFieldType(Integer.parseInt(subfield), fieldType);
            expandedValues.add(subfield);
        }
        return expandedValues.toArray(new String[0]);
    }
    private static String[] expandRangeCronValueWithDataValidation(String cronValue,String fieldType) {
        String[] range = cronValue.split("-");
        int start = Integer.parseInt(range[0]);
        int end = Integer.parseInt(range[1]);
        validateFieldValueWithFieldType(start, fieldType);
        validateFieldValueWithFieldType(end, fieldType);
        return rangeToArray(start, end);
    }

    private static String[] expandStepDataOfCronValueWithDataValidation(String cronValue, String field) {
        String[] parts = cronValue.split("/");
        int step = Integer.parseInt(parts[1]);
        // validation step value limits
        validateFieldValueWithFieldType(step, field);
        int start = getRangeStart(field);
        int end = getRangeEnd(field);
        if (!parts[0].equals("*")) {
            start = Integer.parseInt(parts[0]);
        }
        // validation start value limits
        validateFieldValueWithFieldType(start, field);
        return rangeToArrayWithStepWiseIncrement(start, end, step);
    }

    private static void validateFieldValueWithFieldType(Integer fieldValue, String fieldType) {
        switch(fieldType) {
            case "minute":
                if (fieldValue > 59) {
                    throw new RuntimeException(fieldType + " value in the input cron expression exceeds maximum " + fieldType+ " value");
                }
                if (fieldValue < 0) {
                    throw new RuntimeException(fieldType + " value in the input cron expression is less than minimum "+ fieldType + " value");
                }
                break;
            case "hour":
                if (fieldValue > 23) {
                    throw new RuntimeException(fieldType + " value in the input cron expression exceeds maximum " + fieldType+ " value");
                }
                if (fieldValue < 0) {
                    throw new RuntimeException(fieldType + " value in the input cron expression is less than minimum "+ fieldType + " value");
                }
                break;
            case "day of month":
                if (fieldValue > 31) {
                    throw new RuntimeException(fieldType + " value in the input cron expression exceeds maximum " + fieldType+ " value");
                }
                if (fieldValue < 0) {
                    throw new RuntimeException(fieldType + " value in the input cron expression is less than minimum "+ fieldType + " value");
                }
                break;
            case "month":
                if (fieldValue > 12) {
                    throw new RuntimeException(fieldType + " value in the input cron expression exceeds maximum " + fieldType+ " value");
                }
                if (fieldValue < 0) {
                    throw new RuntimeException(fieldType + " value in the input cron expression is less than minimum "+ fieldType + " value");
                }
                break;
            case "day of week":
                if (fieldValue > 7) {
                    throw new RuntimeException(fieldType + " value in the input cron expression exceeds maximum " + fieldType+ " value");
                }
                if (fieldValue < 1) {
                    throw new RuntimeException(fieldType + " value in the input cron expression is less than minimum "+ fieldType + " value");
                }
                break;
            default:
                return ;
        }
    }

    private static String[] rangeToArray(int start, int end) {
        List<String> array = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            array.add(String.valueOf(i));
        }
        return array.toArray(new String[0]);
    }

    private static String[] rangeToArrayWithStepWiseIncrement(int start, int end, int step) {
        List<String> array = new ArrayList<>();
        for (int i = start; i <= end; i += step) {
            array.add(String.valueOf(i));
        }
        return array.toArray(new String[0]);
    }

    private static int getRangeStart(String field) {
        return field.equals("minute") || field.equals("hour") ? 0 : 1;
    }

    private static int getRangeEnd(String field) {
        switch (field) {
            case "minute":
            case "hour":
                return field.equals("minute") ? 59 : 23;
            case "day of month":
                return 31;
            case "month":
                return 12;
            case "day of week":
                return 7;
            default:
                return -1;
        }
    }

    private static String formatOutput(String field, String[] values) {
        return String.format("%-14s %s", field, String.join(" ", values));
    }
}

package io.github.lunarwatcher.jvmi18n

import java.util.regex.Matcher
import java.util.regex.Pattern

public abstract class Formatter (var translation: Translation){

    /**
     * How the formatter should format given input. This is abstract to allow for overriding
     * and use with custom formatters. See [DefaultFormatter] for the default implementation.
     * Custom implementation features can include
     */
    abstract fun formatInput(line: String, formatData: Array<Any?>) : String;

}

public class DefaultFormatter(translation: Translation) : Formatter(translation){
    var replacable: Pattern;
    var config: Pattern;
    init{
        replacable = Pattern.compile("(?i)\\$\\[.*?\\]");
        config = Pattern.compile("\\[.*?\\]")

    }

    override fun formatInput(line: String, formatData: Array<Any?>) : String {
        var line = line;
        for(o in formatData){
            if(o == null){
                System.out.println("Null arguments not supported.")
                return line;
            }
        }
        var matcher: Matcher = replacable.matcher(line);
        var refs = scanLine(line);
        if(refs == 0){
            println("Cannot format a line with 0 formattable items")
            return line;
        }
        var groups = mutableListOf<String>();
        while(matcher.find()){
            groups.add(matcher.group())
        }

        for(i in 0 until refs) {
            var argument = parseArgs(groups[i])

            if(argument == ARGUMENT_STRING){
                matcher.find(0);
                line = matcher.replaceFirst(formatData[i] as String)
                matcher = replacable.matcher(line)
            }else if(argument.contains(ARGUMENT_INTEGER)
                    || argument.contains(ARGUMENT_DOUBLE)
                    || argument.contains(ARGUMENT_FLOAT)
                    || argument.contains(ARGUMENT_NUMBER)){
                matcher.find(0);
                var split = argument.split(",");
                //val FLAG: String = if(argument.contains(ARGUMENT_DOUBLE)) ARGUMENT_DOUBLE
                //    else if(argument.contains(ARGUMENT_FLOAT)) ARGUMENT_FLOAT
                //    else if(argument.contains(ARGUMENT_INTEGER))  ARGUMENT_INTEGER
                //    else ARGUMENT_NUMBER;
                var possibilities = mutableListOf<Data>()
                for(inp: String in split){
                    var inp = inp.toLowerCase()
                    if(inp == ARGUMENT_DOUBLE || inp == ARGUMENT_FLOAT || inp == ARGUMENT_INTEGER || inp == ARGUMENT_NUMBER)
                        continue;

                    var s = inp.split(" ", ignoreCase=true, limit=2)
                    var case: String = s[1].replace("\"", "")
                    var op: String = s[0];
                    var num = op;
                    num = num.replace(">", "")
                    num = num.replace("<", "")
                    num = num.replace("=", "")
                    op = op.replace(num, "");

                    if(parseNum(op, num, formatData[i] ?: continue)){
                        var flag = "-d";
                        if(case.contains("-e ")){
                            flag = "-e";
                            case = case.replace("-e ", "");
                        }
                        possibilities.add(Data(op, num, case, flag))
                    }
                }

                if(possibilities.size == 0){
                    //The number only if no matches were found
                    line = matcher.replaceFirst(formatData[i].toString())
                    matcher = replacable.matcher(line);
                }else {
                    var picked: Data? = null;

                    //Check = separately
                    for (data in possibilities) {
                        if (data.operator == "=" && data.num == formatData[i].toString()) {
                            picked = data;
                            println("=")
                            break;
                        }
                    }
                    if (picked == null) {
                        for (data in possibilities) {
                            if (data.operator == ">=" && formatData[i].toString().toDouble() >= data.num.toDouble()) {
                                picked = data;
                                println(">=")
                                break;
                            } else if (data.operator == "<=" && formatData[i].toString().toDouble() <= data.num.toDouble()) {
                                picked = data;
                                System.out.println("<=")
                                break;
                            }

                        }
                    }
                    if (picked == null) {
                        // If the option hasn't been picked so far, go with the highest
                        // available reference available. It can't be empty, and it has to
                        // be higher than the others.
                        picked = possibilities[possibilities.size - 1];
                    }
                    if (picked.flag == "-e") {
                        /**
                         * The excluding flag excludes the number itself and adds a specific hard-coded text
                         * for it. For an instance with the number 0, with this flag it can be rendered as
                         * "no occurrences". This isn't necessary in all cases, but that is how the flag works
                         */
                        line = matcher.replaceFirst(picked.case);
                    } else if (picked.flag == "-d") {
                        /**
                         * The default flag has default behavior (SHOCKER!)
                         * Adds the ending to a given input defined through the formatter
                         */
                        line = matcher.replaceFirst(formatData[i].toString() + " " + picked.case);
                    }
                    matcher = replacable.matcher(line)
                }
            }else{
                //Default to String if the type isn't supported by custom conversion
                matcher.find(0);
                line = matcher.replaceFirst(formatData[i] as String)
                matcher = replacable.matcher(line)
            }

        }

        return line;
    }

    fun parseNum(operator: String, referenceNumber: String, expectedNumber: Any): Boolean{

        val ref = referenceNumber.toDouble();
        val expectedNum = expectedNumber.toString().toDouble();
        return handleNum(ref, expectedNum, operator);
    }

    public fun handleNum(ref: Double, expected: Double, operator: String) : Boolean{
        when(operator){
            ">" ->{
                return expected > ref;
            }
            "<" ->{
                return expected < ref;
            }
            ">=" ->{
                return expected >= ref;
            }
            "<=" -> {
                return expected <= ref;
            }
            "=" ->{
                return expected == ref;
            }

        }
        System.out.println("That (\"" + operator + "\") isn't a valid operator...")
        throw RuntimeException();
    }

    public fun parseArgs(group: String) : String{
        var g = group.replace("$", "");
        g = g.replace("[", "");
        return g.replace("]", "")

    }

    companion object {
        val ARGUMENT_STRING = "string"
        val ARGUMENT_INTEGER = "int"
        val ARGUMENT_FLOAT = "float"
        val ARGUMENT_DOUBLE = "double"
        val ARGUMENT_NUMBER = "number"

        val GREATER_EQUAL = 0;
        val SMALLER_EQUAL = 1;
        val EQUAL = 2;
        val GREATER = 3;
        val SMALLER = 4;
    }

    /**
     * Scans a given line and returns the amount of formattable items it has
     */
    fun scanLine(line: String) : Int{
        var matcher: Matcher = replacable.matcher(line);
        var match = matcher.find();
        var groups = mutableListOf<String>();
        while(match){
            groups.add(matcher.group())
            match = matcher.find()
        }

        return groups.size;
    }

    /**
     * @param operator the operator used in the translation
     * @param num The connected number
     */
    class Data(var operator: String, var num: String, var case: String, var flag: String = "-d");
}
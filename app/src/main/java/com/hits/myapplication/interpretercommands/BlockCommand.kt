package com.hits.myapplication.interpretercommands

import java.util.Stack
import kotlin.math.pow

interface BlockCommand {
    fun runCommand()

    private val operatorPriority: HashMap<String, Int>
        get() = hashMapOf(
                "==" to 1,
                "!=" to 1,
                ">" to 1,
                "<" to 1,
                ">=" to 1,
                "<=" to 1,
                "+" to 2,
                "-" to 2,
                "*" to 3,
                "/" to 3,
                "%" to 3,
                "~" to 4,
                "^" to 5
        )

    private fun simpleOperation(left: String, right: String, operation: String): String {
        return when(operation) {
            "+" -> {
                if(Interpreter.getType(left) == "double" || Interpreter.getType(right) == "double") {
                    (left.toDouble() + right.toDouble()).toString()
                }
                else if(Interpreter.getType(left) == "bool" && Interpreter.getType(right) == "bool") {
                    (left == "true" || right == "true").toString()
                }
                else if(Interpreter.getType(left) != "str" && Interpreter.getType(right) != "str") {
                    (left.toInt() + right.toInt()).toString()
                }
                else if(Interpreter.getType(left) == "str" && Interpreter.getType(right) == "str") {
                    left + right
                }
                else null
            }
            "-" -> {
                if(Interpreter.getType(left) == "double" || Interpreter.getType(right) == "double") {
                    (left.toDouble() - right.toDouble()).toString()
                }
                else if(Interpreter.getType(left) == "int" && Interpreter.getType(right) == "int") {
                    (left.toInt() - right.toInt()).toString()
                }
                else null
            }
            "*" -> {
                if(Interpreter.getType(left) == "double" || Interpreter.getType(right) == "double") {
                    (left.toDouble() * right.toDouble()).toString()
                }
                else if(Interpreter.getType(left) == "int" && Interpreter.getType(right) == "int") {
                    (left.toInt() * right.toInt()).toString()
                }
                else if(Interpreter.getType(left) == "str" && Interpreter.getType(right) == "int") {
                    left.repeat(right.toInt())
                }
                else if(Interpreter.getType(left) == "bool" && Interpreter.getType(right) == "bool") {
                    (left == "true" && right == "true").toString()
                }
                else null
            }
            "/" -> {
                if(Interpreter.getType(left) == "double" || Interpreter.getType(right) == "double") {
                    (left.toDouble() / right.toDouble()).toString()
                }
                else if(Interpreter.getType(left) == "int" && Interpreter.getType(right) == "int") {
                    (left.toInt() / right.toInt()).toString()
                }
                else null
            }
            "%" -> (left.toInt() % right.toInt()).toString()
            "^" -> {
                if(Interpreter.getType(left) == "double" || Interpreter.getType(right) == "double") {
                    left.toDouble().pow(right.toDouble()).toString()
                }
                else if(Interpreter.getType(left) == "int" && Interpreter.getType(right) == "int") {
                    left.toDouble().pow(right.toDouble()).toInt().toString()
                }
                else null
            }
            "==" -> (left == right).toString()
            "!=" -> (left != right).toString()
            ">" -> ((Interpreter.convertVariable(left) as Comparable<Any>) > (Interpreter.convertVariable(right) as Comparable<Any>)).toString()
            "<" -> ((Interpreter.convertVariable(left) as Comparable<Any>) < (Interpreter.convertVariable(right) as Comparable<Any>)).toString()
            "<=" -> ((Interpreter.convertVariable(left) as Comparable<Any>) <= (Interpreter.convertVariable(right) as Comparable<Any>)).toString()
            ">=" -> ((Interpreter.convertVariable(left) as Comparable<Any>) >= (Interpreter.convertVariable(right) as Comparable<Any>)).toString()
            else -> "0"
        }!!
    }

    private fun parseExpression(expression: String): List<String> {
        var charList = expression.toCharArray().map { it.toString() }.toMutableList()
        var strFlag = false
        var tempStr = ""
        for (i in charList.indices) {
            if(charList[i] == "\"") {
                //charList[i] = tempStr
                //tempStr = ""
                strFlag = !strFlag
            }
            if(strFlag) {
                //tempStr += charList[i]
                //charList[i] = ""
                continue
            }
            if (charList[i] == "-" && (charList[i + 1][0].isLetterOrDigit() || charList[i + 1] == "(")) {
                charList[i] = "~ "
            } else if (charList[i] == "(") {
                charList[i] += " "
            } else if (charList[i] == ")") {
                charList[i] = " " + charList[i]
            }
        }
        return charList.joinToString("").split(" (?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex()).map { if (Interpreter.getVar(it) != null) Interpreter.getVar(it) else it.removeSurrounding("\"") }.toMutableList() as MutableList<String>
    }

    private fun shuntingYard(parsedExpression: List<String>) : List<String> {
        val result = mutableListOf<String>()
        val stack = Stack<String>()
        parsedExpression.forEach {token ->
            if(token == "(") {
                stack.push(token)
            }
            else if(token == ")") {
                while(stack.peek() != "(") {
                    result.add(stack.pop())
                }
                stack.pop()
            }
            else if(operatorPriority.keys.contains(token)) {
                var peek = if(stack.isNotEmpty()) stack.peek() else null
                while(operatorPriority.keys.contains(peek) && operatorPriority[peek]!! >= operatorPriority[token]!!) {
                    result.add(stack.pop())
                    peek = if(stack.isNotEmpty()) stack.peek() else null
                }
                stack.push(token)
            }
            else {
                result.add(token)
            }
        }
        while(stack.isNotEmpty()) {
            result.add(stack.pop())
        }
        return result
    }

    fun calculate(expression: String): String {
        val stack = Stack<String>()
        var right: String
        var left: String
        shuntingYard(parseExpression(expression)).forEach{token ->
            if(!operatorPriority.contains(token)) stack.push(token)
            else if(token == "~") {
                if(Interpreter.getType(stack.peek()) == "bool") {
                    stack.push((!stack.pop().toBoolean()).toString())
                }
                else stack.push(simpleOperation("0", stack.pop(), "-"))
            }
            else {
                right = stack.pop()
                left = stack.pop()
                stack.push(simpleOperation(left, right, token))
            }
        }
        //return parseExpression(expression).toString() + "\n" + shuntingYard(parseExpression(expression)).toString() + "\n" + stack.pop()
        return stack.pop()
    }
}
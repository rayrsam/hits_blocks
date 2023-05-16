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
                    '"' + left.trim('"') + right.trim('"') + '"'
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
                    '"' + left.trim('"').repeat(right.toInt()) + '"'
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
        val pattern = """(\".*?\")|([\d]+(?:\.\d+)?)|([+\-*\/^~%]|[<>]=?|==|!=)|([\w\[\]]+)""".toRegex()
        val result = pattern.findAll(expression).map { it.value }.toMutableList()
        for(i in result.indices) {
            if(Interpreter.getList(result[i]) != null) {
                val list = Interpreter.getList(result[i])
                result[i] = (list!![0] as MutableList<String>)[(list[2] as String).toInt()]
            }
            if(Interpreter.getVar(result[i]) != null) result[i] = Interpreter.getVar(result[i]).toString()
            else if(result[i] == "-" && (i == 0 || !result[i - 1].last().isLetterOrDigit())) result[i] = "~"
        }
        return result
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
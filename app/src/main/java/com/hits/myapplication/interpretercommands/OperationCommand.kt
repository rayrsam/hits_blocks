package com.hits.myapplication.interpretercommands

import com.hits.myapplication.Block
import com.hits.myapplication.OperBlock
import java.util.Stack
import kotlin.math.pow

class OperationCommand(val name: String, val expression: String) : BlockCommand {
    companion object Factory : BlockCommandFactory() {
        override fun buildBlockCommand(block: Block) = OperationCommand((block as OperBlock).left.toString(), block.right.toString())
    }

    override fun runCommand() {
        Interpreter.assignVar("int", name, calculate(expression))
    }

    private val operatorPriority = hashMapOf(
        "+" to 1,
        "-" to 1,
        "*" to 2,
        "/" to 2,
        "%" to 2,
        "~" to 3,
        "^" to 4
    )

    private fun simpleOperation(left: String, right: String, operation: String): String {
        when(operation) {
            "+" -> return (left.toInt() + right.toInt()).toString()
            "-" -> return (left.toInt() - right.toInt()).toString()
            "*" -> return (left.toInt() * right.toInt()).toString()
            "/" -> return (left.toInt() / right.toInt()).toString()
            "%" -> return (left.toInt() % right.toInt()).toString()
            "^" -> return left.toDouble().pow(right.toDouble()).toInt().toString()
            else -> return "0"
        }
    }

    private fun parseExpression(expression: String) : List<String> {
        val result = mutableListOf<String>()
        var temp = ""
        var last = 'ඞ'
        for (char in expression) {
            when (char) {
                '+', '-', '*', '/', '^', '%', '(', ')' -> {
                    if (temp.isNotEmpty()) result.add(temp)
                    else if (char == '-' && !last.isDigit() && last != ')') {
                        result.add("~")
                        continue
                    }
                    result.add(char.toString())
                    temp = ""
                }
                else -> {
                    temp += char
                }
            }
            last = char
        }
        if(temp.isNotEmpty()) result.add(temp)
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
            else if(token == "~") stack.push(simpleOperation("0", stack.pop(), "-"))
            else {
                right = stack.pop()
                left = stack.pop()
                stack.push(simpleOperation(left, right, token))
            }
        }
        return parseExpression(expression).toString() + "\n" + shuntingYard(parseExpression(expression)).toString() + "\n" + stack.pop()
        //return stack.pop()
    }
}
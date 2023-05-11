package com.hits.myapplication.interpretercommands

open class ConditionCommand(val condition: String): ContainerCommand(queue = mutableListOf()) {
    override fun runCommand() {
        TODO("Not yet implemented")
    }
}
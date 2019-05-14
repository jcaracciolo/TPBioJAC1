package commands

interface CommandType {
    fun execute()
    fun isExit(): Boolean
}

class InvalidCommandArgument(message: String): Exception(message)

class ExitCommand: CommandType {

    override fun execute() = throw NotImplementedError()

    override fun isExit(): Boolean = true
}

class UnknownCommand : CommandType {

    override fun execute() {
        println("Unknown Command")
        println("Secuence Processing: 1 (inputFile, outputFile, [startAtStartCodon], [stopAtStopCodon])")
        println("Blast Processing: 2 (inputFile, outputFile)")
    }

    override fun isExit(): Boolean = false
}

class EmptyCommand : CommandType {

    override fun execute() { }

    override fun isExit(): Boolean = false
}

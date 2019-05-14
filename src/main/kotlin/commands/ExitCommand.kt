package commands

class ExitCommand: CommandType {

    override fun execute() = throw NotImplementedError()

    override fun isExit(): Boolean = true
}
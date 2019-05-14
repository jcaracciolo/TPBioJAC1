package commands

class UnknownCommand : CommandType {

    override fun execute() {
        println("Unknown Command")
    }

    override fun isExit(): Boolean = false
}

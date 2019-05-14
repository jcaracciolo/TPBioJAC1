package commands

interface CommandType {
    fun execute()
    fun isExit(): Boolean
}

package commands

import utils.isValidPath

class SecuenceProcessing(val args: List<String>): CommandType {

    init {
        println(args)
    }

    fun validateArguments(): Boolean {
        if(args.size != 2) {
            println("Secuence Processing expects 2 arguments (inputFile and outputFile)")
            return false
        }

        if(args.map { it.isValidPath() }.reduce { acc, b -> acc && b }) {
            return false
        }

        return true
    }

    override fun execute() {
        if(!validateArguments()) return

        println("Success")
    }

    override fun isExit(): Boolean = false

}


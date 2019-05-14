package commands

import utils.fileExists
import java.io.File

class SecuenceProcessing(val args: List<String>): CommandType {

    val inputFile: File
    val outputFile: File

    init {
        if(args.size != 2) {
            throw InvalidCommandArgument("Secuence Processing expects 2 arguments (inputFile and outputFile)")
        }

        inputFile = File(args[0])
        if(!inputFile.exists()) {
            throw InvalidCommandArgument("${args[0]} file does not exists")
        }

        outputFile = File(args[1])
        if(!outputFile.createNewFile()) {
            throw InvalidCommandArgument("${args[1]} file could not be created")
        }
    }

    override fun execute() {
        println("Success")
    }

    override fun isExit(): Boolean = false

}


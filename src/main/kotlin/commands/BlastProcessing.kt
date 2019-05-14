package commands

import java.io.File

class BlastProcessing(args: List<String>): CommandType {
    val inputFile: File
    val outputFile: File


    init {
        if( args.size != 2)
            throw InvalidCommandArgument("Blast processing recieves two arguments")

        inputFile = File(args[0])
        outputFile = File(args[1])

        if(!inputFile.exists())
            throw InvalidCommandArgument("${args[0]} does not exist")

        if(!outputFile.createNewFile())
            throw InvalidCommandArgument("${args[1]} could not be created")
    }

    override fun execute() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExit(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
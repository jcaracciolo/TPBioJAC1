package TPBioJac1

import commands.*

class BioConsole {

    companion object {

        fun getLine(): CommandType {
            val line = readLine()
            if(line != null && line.length > 0) {
                val parts = line.split(" ")

                try {
                    return when (parts[0]) {
                        "1" -> SecuenceProcessing(parts.takeLast(parts.size - 1))
                        "2" -> BlastProcessing(parts.takeLast(parts.size - 1))
                        "exit" -> ExitCommand()
                        else -> UnknownCommand()
                    }
                }catch (e: InvalidCommandArgument) {
                    println(e.message)
                    return EmptyCommand()
                }

            } else {
                return UnknownCommand()
            }
        }
    }

}

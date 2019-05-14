package TPBioJac1

import commands.CommandType
import commands.ExitCommand
import commands.SecuenceProcessing
import commands.UnknownCommand

class BioConsole {

    companion object {

        fun getLine(): CommandType {
            val line = readLine()
            if(line != null && line.length > 0) {
                val parts = line.split(" ")

                return when(parts[0]) {
                    "1" -> SecuenceProcessing(parts.takeLast(parts.size-1))
                    "exit" -> ExitCommand()
                    else -> UnknownCommand()
                }

            } else {
                return UnknownCommand()
            }
        }
    }

}

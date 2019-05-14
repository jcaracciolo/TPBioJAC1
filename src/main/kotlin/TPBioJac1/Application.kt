package TPBioJac1

open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            var exit = false
            do {
                val command = BioConsole.getLine()
                if(command.isExit()) {
                    exit = true
                } else {
                    command.execute()
                }
            } while(!exit)
        }
    }
}
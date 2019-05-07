package TPBioJac1

import org.biojava.nbio.core.util.FileDownloadUtils

open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            println("Hello World")
            println("Testing BioJava is included as dependecy: ")
            println(FileDownloadUtils())
            println("Done")
        }
    }
}
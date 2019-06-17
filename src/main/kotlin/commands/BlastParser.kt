package commands

import BlastJAXParser.BlastOutput
import java.io.File
import java.io.IOException
import javax.xml.bind.JAXBContext
import javax.xml.transform.stream.StreamSource
import javax.xml.stream.XMLStreamReader
import javax.xml.stream.XMLInputFactory




class BlastParser(args: List<String>): CommandType {
    val inputFile: File
    val pattern: String

    init {
        if (args.size != 2) {
            throw InvalidCommandArgument("Secuence Processing expects 2 arguments (inputFile, pattern)")
        }

        inputFile = File(args[0])
        if (!inputFile.exists()) {
            throw InvalidCommandArgument("${args[0]} file does not exists")
        }


        pattern = args[1]
    }

    override fun execute() {

        try {
            val parser = JAXBContext.newInstance(BlastOutput::class.java)
            val unm = parser.createUnmarshaller()
            val xif = XMLInputFactory.newFactory()
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false)
            val xsr = xif.createXMLStreamReader(inputFile.inputStream())

            val output = (unm.unmarshal(xsr)) as BlastOutput

            val filtered = output.blastOutputIterations.iteration.map {
                Pair(it.iterationQueryDef,
                     it.iterationHits.hit.filter { it.hitDef.toLowerCase().contains(pattern) }
                )
            }

            filtered.forEach {
                println("${it.first} -> ${it.second.size} matches found:")
                it.second.forEach {
                    //TODO retrieve accesion from NCBI
                    println("Accession: " + it.hitAccession)
                    println("Def: " + it.hitDef)
                }
            }

        } catch (e : IOException) {
            e.printStackTrace()
            print(e.message)
        }
    }

    override fun isExit() = false

}
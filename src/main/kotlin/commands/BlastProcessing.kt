package commands

import org.biojava.nbio.core.sequence.DNASequence
import org.biojava.nbio.core.sequence.ProteinSequence
import org.biojava.nbio.core.sequence.compound.*
import org.biojava.nbio.core.sequence.io.*
import org.biojava.nbio.core.sequence.io.template.SequenceHeaderParserInterface
import org.biojava.nbio.ws.alignment.RemotePairwiseAlignmentProperties
import org.biojava.nbio.ws.alignment.qblast.*
import java.io.File
import java.lang.Exception

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
        //TODO Make it so it can use DNA sequences
        //TODO Make it so it works local
        println("Making request to Blas from file $inputFile saving to $outputFile")

        val proxy = FastaReader(
            inputFile,
            PlainFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
            ProteinSequenceCreator(AminoAcidCompoundSet())
        )

        val dnaSequences = proxy.process()

        dnaSequences.entries.forEach { e ->
            val name = e.key
            val seq = e.value

            try {
                val blast = NCBIQBlastService()
                val properties = NCBIQBlastAlignmentProperties()

                properties.blastProgram = BlastProgramEnum.blastp
                properties.blastDatabase = "swissprot"

                println("Sending Sequence request for $name")

                val id = blast.sendAlignmentRequest(seq.toString(), properties)
                while (!blast.isReady(id)) {
                    println("Waiting for results for $name..")
                    Thread.sleep(5000)
                }

                val res = blast.getAlignmentResults(id, NCBIQBlastOutputProperties())
                File(outputFile.path + "-$name").outputStream().use { res.copyTo(it) }

                println("Done")
            }catch (e: Exception) {
                println("Failed to retrieve Blast for $name")
            }

            outputFile.delete()
        }
    }

    override fun isExit(): Boolean = false

}
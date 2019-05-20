package commands

import org.biojava.nbio.core.sequence.DNASequence
import org.biojava.nbio.core.sequence.MultipleSequenceAlignment
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
    val blastType: BlastType

    enum class BlastType(val program: BlastProgramEnum, val db: String) {
        AMINOACID(BlastProgramEnum.blastp, "swissprot"),
        DNA(BlastProgramEnum.blastn, "nr");
    }


    init {
        if( args.size != 2)
            throw InvalidCommandArgument("Blast processing recieves at least two arguments")

        if(args.size >= 3) {
            if(args[3].toLowerCase() == "dna") {
                blastType = BlastType.DNA
            } else if (args[3].toLowerCase() == "aminoacid") {
                blastType = BlastType.AMINOACID
            } else {
                throw InvalidCommandArgument("${args[3]} is not a valid blast type (dna or aminoacid)")
            }
        } else {
            blastType = BlastType.AMINOACID
        }

        inputFile = File(args[0])
        outputFile = File(args[1])

        if(!inputFile.exists())
            throw InvalidCommandArgument("${args[0]} does not exist")

        if(!outputFile.createNewFile())
            throw InvalidCommandArgument("${args[1]} could not be created")
    }

    override fun execute() {
        //TODO Test DNA works
        //TODO Make it so it works local
        println("Making request to Blas from file $inputFile saving to $outputFile")

        val proxy = if(blastType == BlastType.AMINOACID) {
            FastaReader(
                inputFile,
                PlainFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
                ProteinSequenceCreator(AminoAcidCompoundSet())
            )
        } else {
            FastaReader(
                inputFile,
                PlainFastaHeaderParser<DNASequence, NucleotideCompound>(),
                DNASequenceCreator(DNACompoundSet())
            )
        }

        val dnaSequences = proxy.process()

        dnaSequences.entries.forEach { e ->
            val name = e.key
            val seq = e.value

            try {
                val blast = NCBIQBlastService()
                val properties = NCBIQBlastAlignmentProperties()

                properties.blastProgram = blastType.program
                properties.blastDatabase = blastType.db

                println("Sending Sequence request for $name")

                val id = blast.sendAlignmentRequest(seq.toString(), properties)
                while (!blast.isReady(id)) {
                    println("Waiting for results for $name..")
                    Thread.sleep(5000)
                }

                val res = blast.getAlignmentResults(id, NCBIQBlastOutputProperties())
                File(outputFile.path + "-$name").outputStream().use { res.copyTo(it) }

                println("Done for $name..")
            }catch (e: Exception) {
                println("Failed to retrieve Blast for $name")
            }

            outputFile.delete()
        }
    }

    override fun isExit(): Boolean = false

}
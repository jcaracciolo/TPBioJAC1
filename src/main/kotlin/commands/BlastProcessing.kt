package commands

import org.biojava.nbio.core.sequence.DNASequence
import org.biojava.nbio.core.sequence.ProteinSequence
import org.biojava.nbio.core.sequence.compound.*
import org.biojava.nbio.core.sequence.io.*
import org.biojava.nbio.core.sequence.template.AbstractCompound
import org.biojava.nbio.core.sequence.template.AbstractSequence
import org.biojava.nbio.ws.alignment.qblast.*
import java.io.File
import java.lang.Exception
import java.io.InputStreamReader
import java.io.BufferedReader



class BlastProcessing(args: List<String>): CommandType {
    private val inputFile: File
    private val outputFile: File
    private val remoteRequest: Boolean
    private val blastType: BlastType
    private val localBlastDB: String

    enum class BlastType(val program: BlastProgramEnum, val programName: String,  val db: String) {
        AMINOACID(BlastProgramEnum.blastp,"blastp", "swissprot"),
        DNA(BlastProgramEnum.blastn, "blastn","nr");
    }


    init {
        if( args.size < 2)
            throw InvalidCommandArgument("Blast processing recieves at least two arguments")

        remoteRequest = if(args.size >=3) {
            when(args[2].toLowerCase()) {
                "local", "l" -> false
                "remote", "r" -> true
                else -> throw InvalidCommandArgument("${args[2]} is not a valid location type (r|remote|l|local)")
            }
        } else { true }

        localBlastDB = if(!remoteRequest) {
            if(args.size <=3) {
                throw InvalidCommandArgument("Please specify a local DB (inputFile, outputFile, (l|local), localDB)")
            } else {
                args[3]
            }
        } else {
            ""
        }

        val blastTypeIndex = if(remoteRequest) 4 else 5

        blastType = if(args.size >= blastTypeIndex) {
            when (args[blastTypeIndex - 1].toLowerCase()) {
                "dna" -> BlastType.DNA
                "aminoacid" -> BlastType.AMINOACID
                else -> throw InvalidCommandArgument("${args[blastTypeIndex - 1]} is not a valid blast type (dna or aminoacid)")
            }
        } else {
            BlastType.AMINOACID
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
        //TODO Test it works local
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

        val dnaSequences = proxy.process().entries.map { it.key to it.value }


        if(remoteRequest) {
            executeRemote(dnaSequences)
        } else {
            executeLocal(dnaSequences)
        }

        outputFile.delete()
    }

    private fun executeRemote(sequences: List<Pair<String, AbstractSequence<out AbstractCompound>>>) {
        sequences.forEach { (name, seq) ->
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
                println("Failed to retrieve Blast for $name - ${e.message}")
            }
        }
    }

    private fun executeLocal(sequences: List<Pair<String, AbstractSequence<out AbstractCompound>>>) {
        sequences.forEach { (name, seq) ->
            try {
                val pb = ProcessBuilder(
                    localBlastDB,
                    "-db", blastType.db,
                    "-outfmt", "5",
                    "-query", inputFile.absolutePath,
                    "-out", File(outputFile.path + "-$name").absolutePath
                )

                val p = pb.start()

                val stdError = BufferedReader(InputStreamReader(p.errorStream))

                stdError.lines().forEach {
                    println("Error when blasting $name locally - $it")
                }

                println("Done for $name..")
            }catch (e: Exception) {
                println("Failed to retrieve Blast for $name - ${e.message}")
            }
        }
    }

    override fun isExit(): Boolean = false

}
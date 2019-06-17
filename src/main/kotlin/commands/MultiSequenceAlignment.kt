package commands

import java.io.File
import java.io.IOException
import java.net.URL
import java.util.ArrayList
import java.util.LinkedHashMap

import org.biojava.nbio.alignment.Alignments
import org.biojava.nbio.alignment.template.Profile
import org.biojava.nbio.core.sequence.ProteinSequence
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound
import org.biojava.nbio.core.sequence.io.FastaReaderHelper
import org.biojava.nbio.core.util.ConcurrencyTools

internal class MultiSequenceAlignment(args: List<String>) : CommandType {

    val inputFile: File
    val outputFile: File

    init {
        if (args.size < 2) {
            throw InvalidCommandArgument("Secuence Processing expects at least 2 arguments (inputFile, outputFile)")
        }

        inputFile = File(args[0])
        if (!inputFile.exists()) {
            throw InvalidCommandArgument("${args[0]} file does not exists")
        }

        outputFile = File(args[1])
        if (!outputFile.createNewFile()) {
            throw InvalidCommandArgument("${args[1]} file could not be created")
        }
    }
    override fun execute() {

        try {
            val sequencesMap = FastaReaderHelper.readFastaProteinSequence(inputFile)
            val profile = Alignments.getMultipleSequenceAlignment(sequencesMap.values.toList())
            outputFile.writeText(profile.toString())
            ConcurrencyTools.shutdown()
        } catch (e : IOException) {
            e.printStackTrace()
            print(e.message)
        }
    }

    override fun isExit(): Boolean = false
}
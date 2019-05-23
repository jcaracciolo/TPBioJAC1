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

        val sequencesMap: LinkedHashMap<String, ProteinSequence>
        val sequences =  ArrayList<ProteinSequence>();

        try {
            sequencesMap = FastaReaderHelper.readFastaProteinSequence(inputFile)
            for ((key, value) in sequencesMap) {
                sequences.add(value)
            }

        } catch (e : IOException) {
            e.printStackTrace();
        }
        val profile = Alignments.getMultipleSequenceAlignment(sequences);
        println("Alignment:%n%s%n".format(profile));
        ConcurrencyTools.shutdown();
    }

    override fun isExit(): Boolean = false
}
package commands

import org.biojava.nbio.core.sequence.DNASequence
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet
import org.biojava.nbio.core.sequence.compound.AmbiguityRNACompoundSet
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound
import org.biojava.nbio.core.sequence.compound.NucleotideCompound
import org.biojava.nbio.core.sequence.io.DNASequenceCreator
import org.biojava.nbio.core.sequence.io.FastaWriter
import org.biojava.nbio.core.sequence.io.GenbankReader
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser
import org.biojava.nbio.core.sequence.transcription.Frame
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine
import utils.OurAminoacidSequence
import java.io.File


class SecuenceProcessing(args: List<String>): CommandType {

    val inputFile: File
    val outputFile: File
    var startAtStartCodon: Boolean = true
    var stopAtStopCodon: Boolean = true

    init {
        if(args.size < 2) {
            throw InvalidCommandArgument("Secuence Processing expects at least 2 arguments (inputFile, outputFile, [startAtStartCodon], [stopAtStopCodon])")
        }

        inputFile = File(args[0])
        if(!inputFile.exists()) {
            throw InvalidCommandArgument("${args[0]} file does not exists")
        }

        if(args.size > 2) {
            startAtStartCodon = when(args[2].toLowerCase()) {
                "true" -> true
                "false" -> false
                else -> throw InvalidCommandArgument("Expected boolean, got ${args[2]}")
            }
        }

        if(args.size > 3) {
            stopAtStopCodon = when(args[3].toLowerCase()) {
                "true" -> true
                "false" -> false
                else -> throw InvalidCommandArgument("Expected boolean, got ${args[3]}")
            }
        }

        outputFile = File(args[1])
        if(!outputFile.createNewFile()) {
            throw InvalidCommandArgument("${args[1]} file could not be created")
        }
    }

    override fun execute() {

        println("Converting $inputFile to Fasta in $outputFile with params (startAtStartCodon: $startAtStartCodon, stopAtStopCodon: $stopAtStopCodon)")

        val ambiguityDNACompoundSet = AmbiguityDNACompoundSet.getDNACompoundSet()
        val nucleotideCompoundSet = AmbiguityRNACompoundSet.getRNACompoundSet()

        val proxy = GenbankReader(
                inputFile,
                GenericGenbankHeaderParser<DNASequence, NucleotideCompound>(),
                DNASequenceCreator(ambiguityDNACompoundSet)
        )

        val dnaSequences = proxy.process()

        val engine = TranscriptionEngine.Builder()
            .dnaCompounds(ambiguityDNACompoundSet)
            .rnaCompounds(nucleotideCompoundSet)
            .waitForStartCodon(startAtStartCodon)
            .stopAtStopCodons(stopAtStopCodon)
            .build()

        val sixFrames = Frame.getAllFrames()

        val finalSequences = dnaSequences.values.map { dna ->
            engine.multipleFrameTranslation(dna, *sixFrames).mapValues {
                OurAminoacidSequence(it.value, "${dna.accession} - ${it.key}")
            }.values
        }.flatten()


        FastaWriter<OurAminoacidSequence, AminoAcidCompound>(outputFile.outputStream(), finalSequences) {
            return@FastaWriter it.name
        }.process()
    }

    override fun isExit(): Boolean = false

}


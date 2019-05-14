package utils

import org.biojava.nbio.core.sequence.AccessionID
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound
import org.biojava.nbio.core.sequence.template.CompoundSet
import org.biojava.nbio.core.sequence.template.Sequence
import org.biojava.nbio.core.sequence.template.SequenceView

class OurAminoacidSequence(val original: Sequence<AminoAcidCompound>, val name: String): Sequence<AminoAcidCompound> {
    override fun getLength(): Int = original.length

    override fun getCompoundSet(): CompoundSet<AminoAcidCompound> = original.compoundSet

    override fun getIndexOf(p0: AminoAcidCompound?): Int = original.getIndexOf(p0)

    override fun getInverse(): SequenceView<AminoAcidCompound> = original.inverse

    override fun getSubSequence(p0: Int?, p1: Int?): SequenceView<AminoAcidCompound> = original.getSubSequence(p0, p1)

    override fun getSequenceAsString(): String = original.sequenceAsString

    override fun getAccession(): AccessionID = AccessionID(name)

    override fun countCompounds(vararg p0: AminoAcidCompound?): Int = original.countCompounds(*p0)

    override fun getCompoundAt(p0: Int): AminoAcidCompound = original.getCompoundAt(p0)

    override fun iterator(): MutableIterator<AminoAcidCompound> = original.iterator()

    override fun getLastIndexOf(p0: AminoAcidCompound?): Int = original.getLastIndexOf(p0)

    override fun getAsList(): MutableList<AminoAcidCompound> = original.asList
}
package commands;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.alignment.template.Profile;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;

public class MultiSequenceAlignment {
    public static void main(String[] args) {
        align();

    }



    public static void align() {
        LinkedHashMap<String, ProteinSequence> sequencesMap;
        List<ProteinSequence> sequences = new ArrayList<ProteinSequence>();

        try {
            File file = new File("msaTest.fasta");
            sequencesMap = FastaReaderHelper.readFastaProteinSequence(file);
            for (Map.Entry<String, ProteinSequence> entry : sequencesMap.entrySet()) {
                sequences.add(entry.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Profile<ProteinSequence, AminoAcidCompound> profile = Alignments.getMultipleSequenceAlignment(sequences);
        System.out.printf("Alignment:%n%s%n", profile);
        ConcurrencyTools.shutdown();
    }
}

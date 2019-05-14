package commands

import utils.fileExists
import java.io.File
import java.io.ByteArrayInputStream
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound
import org.biojava.nbio.core.sequence.DNASequence
import org.biojava.nbio.core.sequence.transcription.Frame.getAllFrames
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine
import org.biojava.nbio.core.sequence.io.embl.EmblReader.process
import java.util.LinkedHashMap
import org.biojava.nbio.core.sequence.io.DNASequenceCreator
import org.biojava.nbio.core.sequence.compound.NucleotideCompound
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser
import org.biojava.nbio.core.sequence.io.FastaReader
import org.biojava.nbio.core.sequence.compound.AmbiguityRNACompoundSet
import org.biojava.nbio.core.sequence.template.CompoundSet
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet
import org.biojava.nbio.core.sequence.transcription.Frame


class SecuenceProcessing(args: List<String>): CommandType {

//    val inputFile: File
//    val outputFile: File

    init {
//        if(args.size != 2) {
//            throw InvalidCommandArgument("Secuence Processing expects 2 arguments (inputFile and outputFile)")
//        }

//        inputFile = File(args[0])
//        if(!inputFile.exists()) {
//            throw InvalidCommandArgument("${args[0]} file does not exists")
//        }

//        outputFile = File(args[1])
//        if(!outputFile.createNewFile()) {
//            throw InvalidCommandArgument("${args[1]} file could not be created")
//        }
    }

    override fun execute() {
        val baseFile = "LOCUS       NM_000207                469 bp    mRNA    linear   PRI 02-MAY-2019\n" +
                "DEFINITION  Homo sapiens insulin (INS), transcript variant 1, mRNA.\n" +
                "ACCESSION   NM_000207\n" +
                "VERSION     NM_000207.2\n" +
                "KEYWORDS    RefSeq; RefSeq Select.\n" +
                "SOURCE      Homo sapiens (human)\n" +
                "  ORGANISM  Homo sapiens\n" +
                "            Eukaryota; Metazoa; Chordata; Craniata; Vertebrata; Euteleostomi;\n" +
                "            Mammalia; Eutheria; Euarchontoglires; Primates; Haplorrhini;\n" +
                "            Catarrhini; Hominidae; Homo.\n" +
                "REFERENCE   1  (bases 1 to 469)\n" +
                "  AUTHORS   Balboa D, Saarimaki-Vire J, Borshagovski D, Survila M, Lindholm P,\n" +
                "            Galli E, Eurola S, Ustinov J, Grym H, Huopio H, Partanen J,\n" +
                "            Wartiovaara K and Otonkoski T.\n" +
                "  TITLE     Insulin mutations impair beta-cell development in a patient-derived\n" +
                "            iPSC model of neonatal diabetes\n" +
                "  JOURNAL   Elife 7, e38519 (2018)\n" +
                "   PUBMED   30412052\n" +
                "  REMARK    GeneRIF: Here we show that misfolded proinsulin impairs developing\n" +
                "            beta-cell proliferation without increasing apoptosis. We generated\n" +
                "            induced pluripotent stem cells (iPSCs) from people carrying insulin\n" +
                "            (INS) mutations, engineered isogenic CRISPR-Cas9 mutation-corrected\n" +
                "            lines and differentiated them to beta-like cells\n" +
                "            Publication Status: Online-Only\n" +
                "REFERENCE   2  (bases 1 to 469)\n" +
                "  AUTHORS   Andrade RLM, Gigante DP, de Oliveira IO and Horta BL.\n" +
                "  TITLE     C-Peptide and cardiovascular risk factors among young adults in a\n" +
                "            southern Brazilian cohort\n" +
                "  JOURNAL   BMC Endocr Disord 18 (1), 80 (2018)\n" +
                "   PUBMED   30400868\n" +
                "  REMARK    GeneRIF: In a southern Brazilian cohort, C-peptide was associated\n" +
                "            with obesity indicators, both waist circumference and BMI.\n" +
                "            Publication Status: Online-Only\n" +
                "REFERENCE   3  (bases 1 to 469)\n" +
                "  AUTHORS   Kralovicova J and Vorechovsky I.\n" +
                "  TITLE     Allele-specific recognition of the 3' splice site of INS intron 1\n" +
                "  JOURNAL   Hum. Genet. 128 (4), 383-400 (2010)\n" +
                "   PUBMED   20628762\n" +
                "  REMARK    GeneRIF: Findings reveal critical interactions underlying the\n" +
                "            allele-dependent INS expression and INS-mediated risk of T1D\n" +
                "REFERENCE   4  (bases 1 to 469)\n" +
                "  AUTHORS   Stead JD, Hurles ME and Jeffreys AJ.\n" +
                "  TITLE     Global haplotype diversity in the human insulin gene region\n" +
                "  JOURNAL   Genome Res. 13 (9), 2101-2111 (2003)\n" +
                "   PUBMED   12952878\n" +
                "REFERENCE   5  (bases 1 to 469)\n" +
                "  AUTHORS   Naylor,R., Knight Johnson,A. and del Gaudio,D.\n" +
                "  TITLE     Maturity-Onset Diabetes of the Young Overview\n" +
                "  JOURNAL   (in) Adam MP, Ardinger HH, Pagon RA, Wallace SE, Bean LJH, Stephens\n" +
                "            K and Amemiya A (Eds.);\n" +
                "            GENEREVIEWS((R));\n" +
                "            (1993)\n" +
                "   PUBMED   29792621\n" +
                "REFERENCE   6  (bases 1 to 469)\n" +
                "  AUTHORS   De Leon,D.D. and Stanley,C.A.\n" +
                "  TITLE     Permanent Neonatal Diabetes Mellitus\n" +
                "  JOURNAL   (in) Adam MP, Ardinger HH, Pagon RA, Wallace SE, Bean LJH, Stephens\n" +
                "            K and Amemiya A (Eds.);\n" +
                "            GENEREVIEWS((R));\n" +
                "            (1993)\n" +
                "   PUBMED   20301620\n" +
                "REFERENCE   7  (bases 1 to 469)\n" +
                "  AUTHORS   Bell,G.I., Swain,W.F., Pictet,R., Cordell,B., Goodman,H.M. and\n" +
                "            Rutter,W.J.\n" +
                "  TITLE     Nucleotide sequence of a cDNA clone encoding human preproinsulin\n" +
                "  JOURNAL   Nature 282 (5738), 525-527 (1979)\n" +
                "   PUBMED   503234\n" +
                "REFERENCE   8  (bases 1 to 469)\n" +
                "  AUTHORS   Tager,H., Given,B., Baldwin,D., Mako,M., Markese,J., Rubenstein,A.,\n" +
                "            Olefsky,J., Kobayashi,M., Kolterman,O. and Poucher,R.\n" +
                "  TITLE     A structurally abnormal insulin causing human diabetes\n" +
                "  JOURNAL   Nature 281 (5727), 122-125 (1979)\n" +
                "   PUBMED   381941\n" +
                "REFERENCE   9  (bases 1 to 469)\n" +
                "  AUTHORS   Flynn,T.J., Deshmukh,D.S. and Pieringer,R.A.\n" +
                "  TITLE     Effects of altered thyroid function on galactosyl diacylglycerol\n" +
                "            metabolism in myelinating rat brain\n" +
                "  JOURNAL   J. Biol. Chem. 252 (16), 5864-5870 (1977)\n" +
                "   PUBMED   195962\n" +
                "REFERENCE   10 (bases 1 to 469)\n" +
                "  AUTHORS   SANGER,F.\n" +
                "  TITLE     Chemistry of insulin; determination of the structure of insulin\n" +
                "            opens the way to greater understanding of life processes\n" +
                "  JOURNAL   Science 129 (3359), 1340-1344 (1959)\n" +
                "   PUBMED   13658959\n" +
                "COMMENT     REVIEWED REFSEQ: This record has been curated by NCBI staff. The\n" +
                "            reference sequence was derived from BC005255.1 and BM510748.1.\n" +
                "            This sequence is a reference standard in the RefSeqGene project.\n" +
                "            On Jun 15, 2006 this sequence version replaced NM_000207.1.\n" +
                "            \n" +
                "            Summary: This gene encodes insulin, a peptide hormone that plays a\n" +
                "            vital role in the regulation of carbohydrate and lipid metabolism.\n" +
                "            After removal of the precursor signal peptide, proinsulin is\n" +
                "            post-translationally cleaved into three peptides: the B chain and A\n" +
                "            chain peptides, which are covalently linked via two disulfide bonds\n" +
                "            to form insulin, and C-peptide. Binding of insulin to the insulin\n" +
                "            receptor (INSR) stimulates glucose uptake. A multitude of mutant\n" +
                "            alleles with phenotypic effects have been identified. There is a\n" +
                "            read-through gene, INS-IGF2, which overlaps with this gene at the\n" +
                "            5' region and with the IGF2 gene at the 3' region. Alternative\n" +
                "            splicing results in multiple transcript variants. [provided by\n" +
                "            RefSeq, Jan 2019].\n" +
                "            \n" +
                "            Transcript Variant: This variant (1) represents the shortest\n" +
                "            variant. All variants encode the same protein.\n" +
                "            \n" +
                "            Publication Note:  This RefSeq record includes a subset of the\n" +
                "            publications that are available for this gene. Please see the Gene\n" +
                "            record to access additional publications.\n" +
                "            \n" +
                "            ##Evidence-Data-START##\n" +
                "            Transcript exon combination :: BC005255.1, BU075674.1 [ECO:0000332]\n" +
                "            RNAseq introns              :: single sample supports all introns\n" +
                "                                           SAMEA2151405, SAMEA2153347\n" +
                "                                           [ECO:0000348]\n" +
                "            ##Evidence-Data-END##\n" +
                "            \n" +
                "            ##RefSeq-Attributes-START##\n" +
                "            RefSeq Select criteria :: based on conservation, expression,\n" +
                "                                      longest protein\n" +
                "            ##RefSeq-Attributes-END##\n" +
                "            COMPLETENESS: complete on the 3' end.\n" +
                "PRIMARY     REFSEQ_SPAN         PRIMARY_IDENTIFIER PRIMARY_SPAN        COMP\n" +
                "            1-413               BC005255.1         1-413\n" +
                "            414-469             BM510748.1         418-473\n" +
                "FEATURES             Location/Qualifiers\n" +
                "     source          1..469\n" +
                "                     /organism=\"Homo sapiens\"\n" +
                "                     /mol_type=\"mRNA\"\n" +
                "                     /db_xref=\"taxon:9606\"\n" +
                "                     /chromosome=\"11\"\n" +
                "                     /map=\"11p15.5\"\n" +
                "     gene            1..469\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /note=\"insulin\"\n" +
                "                     /db_xref=\"GeneID:3630\"\n" +
                "                     /db_xref=\"HGNC:HGNC:6081\"\n" +
                "                     /db_xref=\"MIM:176730\"\n" +
                "     exon            1..42\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /inference=\"alignment:Splign:2.1.0\"\n" +
                "     STS             16..186\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"GDB:181496\"\n" +
                "                     /db_xref=\"UniSTS:155248\"\n" +
                "     exon            43..246\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /inference=\"alignment:Splign:2.1.0\"\n" +
                "     CDS             60..392\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /note=\"proinsulin; preproinsulin\"\n" +
                "                     /codon_start=1\n" +
                "                     /product=\"insulin preproprotein\"\n" +
                "                     /protein_id=\"NP_000198.1\"\n" +
                "                     /db_xref=\"CCDS:CCDS7729.1\"\n" +
                "                     /db_xref=\"GeneID:3630\"\n" +
                "                     /db_xref=\"HGNC:HGNC:6081\"\n" +
                "                     /db_xref=\"MIM:176730\"\n" +
                "                     /translation=\"MALWMRLLPLLALLALWGPDPAAAFVNQHLCGSHLVEALYLVCG\n" +
                "                     ERGFFYTPKTRREAEDLQVGQVELGGGPGAGSLQPLALEGSLQKRGIVEQCCTSICSL\n" +
                "                     YQLENYCN\"\n" +
                "     sig_peptide     60..131\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /inference=\"COORDINATES: ab initio prediction:SignalP:4.0\"\n" +
                "     proprotein      132..389\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /product=\"proinsulin\"\n" +
                "     mat_peptide     132..221\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /product=\"insulin B chain\"\n" +
                "     mat_peptide     228..320\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /product=\"C-peptide\"\n" +
                "     mat_peptide     327..389\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /product=\"insulin A chain\"\n" +
                "     STS             60..391\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"PMC123023P3\"\n" +
                "                     /db_xref=\"UniSTS:270424\"\n" +
                "     STS             68..368\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"PMC21231P1\"\n" +
                "                     /db_xref=\"UniSTS:272021\"\n" +
                "     STS             72..371\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"Ins2\"\n" +
                "                     /db_xref=\"UniSTS:259556\"\n" +
                "     STS             79..390\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"Ins1\"\n" +
                "                     /db_xref=\"UniSTS:256685\"\n" +
                "     STS             198..385\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"PMC24644P6\"\n" +
                "                     /db_xref=\"UniSTS:265494\"\n" +
                "     exon            247..465\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /inference=\"alignment:Splign:2.1.0\"\n" +
                "     STS             247..465\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "                     /standard_name=\"GDB:179433\"\n" +
                "                     /db_xref=\"UniSTS:155046\"\n" +
                "     regulatory      446..451\n" +
                "                     /regulatory_class=\"polyA_signal_sequence\"\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "     polyA_site      465\n" +
                "                     /gene=\"INS\"\n" +
                "                     /gene_synonym=\"IDDM; IDDM1; IDDM2; ILPR; IRDN; MODY10\"\n" +
                "ORIGIN      \n" +
                "        1 agccctccag gacaggctgc atcagaagag gccatcaagc agatcactgt ccttctgcca\n" +
                "       61 tggccctgtg gatgcgcctc ctgcccctgc tggcgctgct ggccctctgg ggacctgacc\n" +
                "      121 cagccgcagc ctttgtgaac caacacctgt gcggctcaca cctggtggaa gctctctacc\n" +
                "      181 tagtgtgcgg ggaacgaggc ttcttctaca cacccaagac ccgccgggag gcagaggacc\n" +
                "      241 tgcaggtggg gcaggtggag ctgggcgggg gccctggtgc aggcagcctg cagcccttgg\n" +
                "      301 ccctggaggg gtccctgcag aagcgtggca ttgtggaaca atgctgtacc agcatctgct\n" +
                "      361 ccctctacca gctggagaac tactgcaact agacgcagcc cgcaggcagc cccacacccg\n" +
                "      421 ccgcctcctg caccgagaga gatggaataa agcccttgaa ccagcaaaa\n" +
                "//\n"



        val parsedFile = baseFile.substringAfter("ORIGIN ")
                .toLowerCase().replace(Regex("[^actg]"), "")

        println(parsedFile)

        val stream = ">1\n$parsedFile".byteInputStream()

        val ambiguityDNACompoundSet = AmbiguityDNACompoundSet.getDNACompoundSet()
        val nucleotideCompoundSet = AmbiguityRNACompoundSet.getRNACompoundSet()

        val proxy = FastaReader(
                stream,
                GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
                DNASequenceCreator(ambiguityDNACompoundSet)
        )

        // has only one entry in this example, but could be easily extended to parse a FASTA file with multiple sequences
        val dnaSequences = proxy.process()

        // Initialize the Transcription Engine
        val engine = TranscriptionEngine.Builder().dnaCompounds(ambiguityDNACompoundSet).rnaCompounds(nucleotideCompoundSet).build()

        val sixFrames = Frame.getAllFrames()

        for (dna in dnaSequences.values) {

            val results = engine.multipleFrameTranslation(dna, *sixFrames)

            for (frame in sixFrames) {
                println("Translated Frame:" + frame + " : " + results[frame])
            }

        }

        println("Done")
    }

    override fun isExit(): Boolean = false

}


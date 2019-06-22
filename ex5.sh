#dada una secuencia de nucleotidos en formato fasta, obtener los marcos de lectura
# abiertos(orf), traducirla a la cadena de aminoacidos
# y aplicamos cualquier otro comando a los aminoacidos
# needs to be executed as root user or with sudo for prosextract

if [ $# -ne 1 ]
then
#validate args cuantity
	echo -e "Error:Wrong format\n\nUssage:\n\t./bio.sh fastaFile.fasta"
else
echo -e "Traduction will be in: traduction.fasta\norf will be in: orf.fasta";
echo -e "Domain analysis will be in: domainsOutput.txt\n";

#usar getorf para marcos de lectura abierto
	echo -e "orf.fasta" | getorf $1 2>> /dev/null;

# generates traduction in traduction.fasta
	transeq $1 traduction.fasta 2>> /dev/null;

# prosextract need to make sure that prosite is the relative path to prosite directory
echo -e "prosite" | sudo prosextract 2>> /dev/null;

#obtain domains
echo -e "traduction.fasta\ndomainsOutput\n" | patmatmotifs 2>> /dev/null;
fi
#dada una secuencia de nucleotidos en formato fasta, obtener los marcos de lectura
# abiertos(orf), traducirla a la cadena de aminoacidos
# y aplicamos cualquier otro comando a los aminoacidos

if [ $# -ne 1 ]
then
#validate args cuantity
	echo -e "Error:Wrong format\n\nUssage:\n\t./bio.sh fastaFile.fasta"
else
echo -e "Traduction will be in: traduction.fasta\n";

#usar getorf para marcos de lectura abierto
	echo -e "orf.fasta" | getorf $1 2>> /dev/null;

# generates traduction in traduction.fasta
	transeq $1 traduction.fasta 2>> /dev/null;

fi
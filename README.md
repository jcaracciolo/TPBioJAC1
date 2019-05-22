# TP Introducción a la Bioinformática: Parte I
### Configuración
`sudo apt-get install mvn`

`mvn clean compile assembly:single`

`java -jar target/TPBioJac1-1.0-SNAPSHOT-jar-with-dependencies.jar`

### Parámetros de ejecución
Para la traducción de secuencias de nucleótidos en GBK a secuencias de aminoácidos en FASTA ejecutar:

`1 input_file output_file start_at_start_codon stop_at_stop_codon`

Siendo:
* `input_file` el nombre del archivo donde se encuentra la secuencia de nucleótidos a taducir;
* `output_file` el nombre del archivo donde se desea guardar la secuencia de aminoácidos traducida;
* `start_at_start_codon` un boolean que indica si se quiere empezar a traducir únicamente desde el codón de inicio;
* `stop_at_stop_codon` un boolean que indica si se quiere parar de traducir al leer un codón de término.

Para la realización del BLAST a partir de un archivo FASTA ejecutar:

`2 input_file output_file running_configuration blast_type`

Siendo:
* `input_file` el nombre del archivo FASTA donde se encuentra la secuencia;
* `output_file` el nombre del archivo donde se desea guardar el output del BLAST;
* `running_configuration` determina el modo de ejecución del BLAST. Puede ser: `local` o `remote`;
* `blast_type` determina el tipo de búsqueda de BLAST a realizar. Puede ser: `nucleotide` o `protein`.

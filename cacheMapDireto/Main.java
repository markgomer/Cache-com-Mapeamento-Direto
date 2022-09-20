/**
 * PSCF
 * Cache com mapeamento direto
 * Eduardo Klein Nakatani
 * Henrique Levandoski Richa
 * Marco Aurélio Silva de Souza Júnior
 * Rafael Bauer Sampaio
 */

package cacheMapDireto;

public class Main {

    public static void main(String[] args) {
        System.out.println("* Arquitetura von Neumann Básica - PSCF\n");
        try {
            // cria componentes da arquitetura
            IO io = new IO(System.out);
            RAM ram = new RAM(8_388_608); // 8M - 131.072 blocos de 64
            Cache cache = new Cache(4096, 64, ram);
            CPU cpu = new CPU(io, cache);
            // carrega "programa" na memória
            final int inicio = 0b00001111;
            ram.Write(inicio, 118);
            ram.Write(inicio+1, 130);
            // executa programa
            cpu.Run(inicio);
        } catch (EnderecoInvalido e) {
            System.err.println("Erro: " + e);
        }
    }
}

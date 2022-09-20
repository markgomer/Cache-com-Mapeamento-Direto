//
// Arquitetura von Neumann Básica
// PSCF
// Prof. Luiz Lima Jr.
//
// IO <- CPU <-> RAM
//

package cacheMapDireto;

public class Main {

    public static void main(String[] args) {
        System.out.println("* Arquitetura von Neumann Básica - PSCF\n");
        
        // cria componentes da arquitetura
        IO io = new IO(System.out);
        RAM ram = new RAM(8_388_608); // 8M
        Cache cache = new Cache(4096, 64, ram);
        CPU cpu = new CPU(io, cache);
        
        try {
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

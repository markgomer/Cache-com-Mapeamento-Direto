package cacheMapDireto;

public class Cache extends Memoria {
    private RAM ram;

    //private int inicio;
    /* int[r][w+1] , int[r][0] = t */
    private int[][] dados;
    private boolean modificada = false;

    public Cache(int capacidade, int tamCacheLine, RAM ram) throws EnderecoInvalido {
        super(capacidade);
        // a primeira posicao da cacheline corresponde à tag 't'
        this.dados = new int[capacidade][tamCacheLine+1];
        this.ram = ram;
        copyLineFromRAM(0, 0);
    }

    @Override
    public int Read(int enderecoBit) throws EnderecoInvalido {
        System.out.println("READCACHE");
        int resp;
        int x = enderecoBit;
        System.out.println("x="+Integer.toBinaryString(x));
        int t, r, s, w;

        // extrair t, r e w
        // se cache line = 2^6 palavras, w possui 6 bits 
        w = 0b11_1111 & x;
        System.out.println("w="+Integer.toBinaryString(w));
        // se cache possui 2^6 cache lines, r possui 6 bits (capacidade memoria principal) 
        r = (x >> 6) & 0b11_1111;
        System.out.println("r="+Integer.toBinaryString(r));
        // capacidade = 8M = 2^23, x é de 23 bits, t possui 11 bits 
        t = (x >> 12) & 0b111_1111_1111;
        System.out.println("t="+Integer.toBinaryString(t));
        // tag da posicao r da cache
        int t_ = (dados[r][0] >> 18) & 0b111_1111_1111;
        System.out.println("t_="+Integer.toBinaryString(t_));
        // s = concatenação t+r
        s = t_ | r | 0b00_0000;
        System.out.println("s="+Integer.toBinaryString(s));

        // comparar t com t'(cacheline)
        boolean cacheHit = t==t_;
        if(cacheHit) {
            // cache hit: utiliza-se w para retornar à CPU a palavra na posição w
            resp = dados[r][w+1]; /* +1 pois a posicao 0 eh da tag */
        }
        else { // cache miss:
            System.out.println("CacheMiss - End: " + x);
            // cache line r corresponde a um outro bloco de memória
            if(modificada) {
                // Se esta cache line tiver sido alterada, ela é copiada para a memória principal
                // a partir do endereço s
                copyLineToRAM(s, r);
                modificada = false;
            }
            copyLineFromRAM(s, r);
            // o bloco s da memória principal é trazido para a cache line
            resp = dados[r][w+1]; /* +1 pois a posicao 0 eh da tag */
        }

        // a palavra no endereço solicitado é retornada à CPU
        System.out.println("resp="+resp);
        return resp;
    }


    @Override
    public void Write(int enderecoBit, int valor) throws EnderecoInvalido {
        int x = enderecoBit;
        System.out.println("x="+Integer.toBinaryString(x));
        int t, r, s, w;
        
        System.out.println("WRITE");
        
        // extrair t, r e w
        // se cache line = 2^6 palavras, w possui 6 bits 
        w = 0b111_1111 & x;
        System.out.println("w="+Integer.toBinaryString(w));
        // se cache possui 2^12 cache lines, r possui 12 bits
        r = (x >> 6) & 0b1111_1111_1111;
        System.out.println("r="+Integer.toBinaryString(r));
        // capacidade = 8M = 2^23, x é de 23 bits, t possui 11 bits 
        t = (x >> 18) & 0b1_1111;
        System.out.println("t="+Integer.toBinaryString(t));
        // tag da posicao r da cache
        int t_ = (dados[r][0] >> 18) & 0b1_1111;
        System.out.println("t_="+Integer.toBinaryString(t_));
        // s = concatenação t+r
        s = t_ | r | 0b00_0000;
        System.out.println("s="+Integer.toBinaryString(s));

        // comparar t com t'(cacheline)
        boolean cacheHit = t==t_;
        if(cacheHit) {
            // cache hit: utiliza-se w para retornar à CPU a palavra na posição w
            System.out.println("CACHEHIT");
        }
        else {
            // cache miss:
            System.out.println("CacheMiss - End: " + x);
            // cache line r corresponde a um outro bloco de memória
            if(modificada) {
                // Se esta cache line tiver sido alterada, ela é copiada para a memória principal
                // a partir do endereço s
                copyLineToRAM(s, r);
                modificada = false;
            }
            // o bloco s da memória principal é trazido para a cache line
            dados[r][w+1] = valor; /* +1 pois a posicao 0 eh da tag */
            //resp = dados[r][w+1]; /* +1 pois a posicao 0 eh da tag */
        }

    }



    private void copyLineToRAM(int s, int r) throws EnderecoInvalido {
        int i = 0;

        try {
            while (i < capacidade) {
                ram.Write(s+i, dados[r][i]);
                i++;
            }
        } catch (EnderecoInvalido e) {
            while(i < capacidade) {
                ram.Write(s+i , dados[r][i]);
                i++;
            }
        }
    }


    private void copyLineFromRAM(int s, int r) throws EnderecoInvalido {
        int i = 0;

        // combinar t com r (s) e buscar na mem RAM

        try {
            while (i < 64) { // r tem 6 bits
                dados[r][i] = ram.Read(s << 6 | i); // s concatenado com i
                i++;
            }
        } catch (EnderecoInvalido e) {
            while(i < capacidade) {
                dados[r][i] = ram.Read(s << 6 | i);
                i++;
            }
        }
    }
    
}

package cacheMapDireto;

public class Cache extends Memoria {
    private RAM ram;

    private int inicio;
    private int[][] dados;
    private boolean modificada = false;

    public Cache(int capacidade, int tamCacheLine, RAM ram) {
        super(capacidade);
        // a primeira posicao da cacheline corresponde à tag 't'
        this.dados = new int[capacidade][tamCacheLine+1];
        this.ram = ram;
    }

    @Override
    public int Read(int enderecoBit) {
        int resp;
        int x = enderecoBit;
        int t, r, s, w;

        // extrair t, r e w
        // se cache line = 2^6 palavras, w possui 6 bits 
        w = 0b111_1111 & x;
        // se cache possui 2^12 cache lines, r possui 12 bits
        r = (x >> 6) & 0b1111_1111_1111;
        // capacidade = 8M, x é de 23 bits, t possui 5 bits 
        t = (x >> 18) & 0b1_1111;
        // tag da posicao r da cache
        int t_ = (dados[r][0] >> 18) & 0b1_1111;
        // s = concatenação t+r
        s = t_ | r | 0b00_0000;

        // comparar t com t'(cacheline)
        boolean cacheHit = t==t_;
        if(cacheHit) {
            // cache hit: utiliza-se w para retornar à CPU a palavra na posição w
            resp = dados[r][w+1]; /* +1 pois a posicao 0 eh da tag */
        }
        else {
            // cache miss:
            // cache line r corresponde a um outro bloco de memória
            if(modificada) {
                // Se esta cache line tiver sido alterada, ela é copiada para a memória principal
                // a partir do endereço s
                //todo: copiar bloco para RAM
                
                modificada = false;
            }
            // o bloco s da memória principal é trazido para a cache line
            resp = dados[r][w+1]; /* +1 pois a posicao 0 eh da tag */
        }

        // a palavra no endereço solicitado é retornada à CPU
        return resp;
    }


    @Override
    public void Write(int endereco, int valor) {

    }





















    /************************ 
     * CACHE SIMPLES
    */

    /**
     * 
     * /
    @Override
    public int Read(int endereco) throws EnderecoInvalido {
        // recebe endereco de leitura X da CPU
        int endNaCache = endereco - inicio;
        // bloco contendo X esta na cache?
        if( !cacheHit(endereco) ) {
            // nao: copia cache (se foi modificada) para a sua devida posicao na RAM
            if(modificada) writeBlockToRam(endereco);
            // copia bloco da RAM a partir do endereco X para a cache
            readBlockFromRam(endereco);
            inicio = endereco;
            endNaCache = 0;
        }
        this.VerificaEndereco(endNaCache);
        System.out.println("endcache: " + endNaCache);
        return dados[endNaCache];
    }
    
    
    @Override
    public void Write(int endereco, int valor) throws EnderecoInvalido {
        // recebe endereco de leitura X da CPU
        int endNaCache = endereco - inicio;
        // bloco contendo X esta na cache?
        if( !cacheHit(endereco) ) {
            // nao: copia cache (se foi modificada) para a sua devida posicao na RAM
            if(modificada) writeBlockToRam(endereco);
            // copia bloco da RAM a partir do endereco X para a cache
            readBlockFromRam(endereco);
            inicio = endereco;
            endNaCache = 0;
        }
        this.VerificaEndereco(endNaCache);
        // sim: escreve P em X na cache e marca a cache como modificada
        dados[endNaCache] = valor;
        modificada = true;
        System.out.println("Write: endNaCache:" + endNaCache + " valor:" + valor);
    }


    /**************
     * PRIVATES
    ***************/



    /**
     * 
     * @param endereco
     * @return
     * /
    private boolean cacheHit(int endereco) {
        boolean resp = endereco >= inicio && endereco <= inicio + capacidade;
        System.out.println(resp? "CacheHit!" : "CacheMiss!");
        return resp;
    }
    
    private void writeBlockToRam(int endereco) throws EnderecoInvalido {
        int i = 0;
        try {
            while (i < capacidade) {
                ram.Write(endereco + i, dados[i]);
                System.out.println("CopyToRam -> end:" + (endereco+i) + "content:" + dados[i]);
                i++;
            }
        } catch (EnderecoInvalido e) {
            int count = 1;
            while(i < capacidade) {
                ram.Write(endereco - count , dados[i]);
                System.out.println("CopyToRam -> end:" + (endereco-count) + "content:" + dados[i]);
                i++;
                count++;
            }
        }
    }

    private void readBlockFromRam(int endereco) throws EnderecoInvalido {
        int i = 0;
        try {
            while (i < capacidade) {
                dados[i] = ram.Read(endereco + i);
                System.out.println("ReadFromRam -> end:" + (endereco+i) + "content:" + dados[i]);
                i++;
            }
        } catch (EnderecoInvalido e) {
            int count = 1;
            while(i < capacidade) {
                dados[i] = ram.Read(endereco - count );
                System.out.println("ReadFromRam -> end:" + (endereco-count) + "content:" + dados[i]);
                i++;
                count++;
            }
        }
    }
    /** */

    
}

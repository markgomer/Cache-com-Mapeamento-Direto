## Descrição

A próxima etapa do projeto consiste em **implementar o modelo de memória cache por Mapeamento Direto** (cf. *Stallings, 10ª ed, seção 4.3* – disponível na biblioteca virtual da PUCPR – leitura obrigatória!).

A memória cache é formada por `m` "blocos" (chamados "*cache lines*") de `K` palavras de memória, cada bloco precedido por uma "etiqueta" (*tag*) que identifica o bloco de memória principal sendo atualmente armazenado naquela *cache line* e uma flag para indicar se foi modificada ou não (veja figura abaixo).

![modelo_cache.png](C:\Users\aurel\Documents\3%20-%20Estudo\02%20PUC\6o%20semestre\Perf%20Sis%20Ciber%20Fis\220917-trab-cache-mapeamento-direto\modelo_cache.png)



Neste contexto, a CPU solicita o conteúdo da memória em um determinado endereço `x` que é enviado, não mais diretamente à memória principal, mas sim à cache. Ao receber esta requisição de acesso a um endereço, o sistema deve verificar se o conteúdo do endereço solicitado está ou não em alguma *cache line* e tomar as medidas necessárias em caso negativo (*cache miss*), buscando o bloco contendo o endereço solicitado da memória principal (RAM) conforme ilustrado na figura abaixo.

![direct_mapping2.png](C:\Users\aurel\Documents\3%20-%20Estudo\02%20PUC\6o%20semestre\Perf%20Sis%20Ciber%20Fis\220917-trab-cache-mapeamento-direto\direct_mapping2.png)



## Especificações

- Capacidade da *memória principal*: **8M** palavras*

- Capacidade total da *memória cache*: **4096** palavras*

- Tamanho da *cache line*: **64** palavras* (isto é, `K` = 64)
* 1 palavra de memória = 1 número inteiro

## Funcionamento Básico da Memória Cache (por Mapeamento Direto)

Endereços `x` gerados pela CPU são divididos internamente nos seguintes blocos de *bits*:

- **`w`** — especifica uma das `K` palavras de uma *cache line* ou bloco de memória;
- **`r`** — especifica o índice do *cache line*;
- **`t`** — é a etiqueta (*tag*) que corresponde aos *bits* restantes do endereço e serve para identificar qual o bloco que memória principal que se encontra atualmente na *cache line*;
- **`s`** — corresponde à concatenação dos bits de `t` e `r` e representa o número do bloco de memória principal onde está a palavra à qual se deseja acessar.

### Algoritmo

Ao receber a solicitação de uma palavra de memória localizada em um endereço `x`, extrai-se `r` e `t` de `x` e faz-se então a verificação se `t` é igual ao *tag* `t'` contido na linha `r` da *cache line* (passo 2 na figura acima). Se forem iguais, houve um **"cache hit"** (isto é, a palavra solicitada está na cache) e então utiliza-se `w` para retornar à CPU a palavra na posição `w` dos dados da *cache line* (passo 3a na figura).

Se houver um **"*cache miss*"**, a *cache line* `r` corresponde a um outro bloco de memória. Se esta *cache line* tiver sido alterada, ela é copiada para a memória principal (a partir do endereço formado pela concatenação de `t' | r | 000000`) (passo 3b na figura). Em todo caso, o bloco `s` da memória principal é trazido para a *cache line* e a palavra no endereço solicitado é retornada à CPU (passo 4 na figura). Assim, a cache é atualizada e próximos acessos a endereços próximos produzirão um *cache hit*.

## Considerações de Implementação

Operações úteis em Java:

![java-bitwise-ops.png](C:\Users\aurel\Documents\3%20-%20Estudo\02%20PUC\6o%20semestre\Perf%20Sis%20Ciber%20Fis\220917-trab-cache-mapeamento-direto\java-bitwise-ops.png)



- Para testes, faça o seu programa **imprimir na tela** quando houver *cache miss* e qual endereço que o causou.
- Crie situações para mostrar que a cache desenvolvida está funcionando corretamente (pense em situações que causam *cache misses*).

## Envio

- Este trabalho será desenvolvido por grupos de **até 5 estudantes**.
- Você deve enviar **apenas os arquivos `.java`** do seu projeto.

## Apresentação

- Para a apresentação/defesa, um estudante do grupo será sorteado e responderá pelo grupo. Assim, é importante que todos tenham conhecimento de todas as partes do programa.
- Todos os participantes do grupo devem enviar os arquivos .java (coloque comentário no arquivo que contém o programa principal – main – a lista dos membros do grupo).

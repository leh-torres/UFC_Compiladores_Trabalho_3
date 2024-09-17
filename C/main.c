/******************************************************************************

                            Online C Compiler.
                Code, Compile, Run and Debug C program online.
Write your code in this editor and press "Run" button to compile and execute it.

*******************************************************************************/

# include <stdio.h>
# include <stdlib.h>
# include <math.h>
#include <time.h>
# define MAX 999999

typedef struct 
{
    int coordX;
    int coordY;

} Cidade;

int n_cidades;
Cidade mapa[100];

int grafo[100][100];
int visitados[100], caminho[100];
int custo_total = 0;
int min = MAX;

void decideNumeroDeCidades();
void criarCidades();
void mostrarCidades();
double calcularDistanciaEuclidiana(Cidade cid1, Cidade cid2);
void criarGrafo();
int encontraProxCidade(int vertice);
void caxeiroViajante(int cidade);

int main (void){
    
    clock_t inicio, fim;
    double tempo;
    
    inicio = clock();
    decideNumeroDeCidades();
    criarCidades();
    mostrarCidades();
    criarGrafo();
    
    for(int i = 0; i < n_cidades; i++){
        visitados[i] = 0;
    }
    
    caxeiroViajante(0);
    fim = clock();
    
    printf("\nDistancia total - %d\n km", custo_total);
    printf("Caminho: ");
    for(int b = 0; b < n_cidades; b++){
        printf("%d ", caminho[b]);
    }
    
    tempo = ((double) (fim-inicio)) / CLOCKS_PER_SEC;
    printf("\nTempo:  %f seg", tempo);
    return 0;
}

void decideNumeroDeCidades(){
    

    printf("----------------------------\n");
    printf("Digite o número de cidades desejadas. Este deverá ser maior que 5 e menor que 105.\n ");
    scanf("%d", &n_cidades);

    if(n_cidades >= 0 && n_cidades < 5){
        
        printf("\n----------------------------\n");
        printf("      VALOR INVÁLIDO !!!      \n");
        printf("----------------------------\n");
        printf("Digite o número de cidades desejadas. Este deverá ser maior que 5 e menor que 105.");
        scanf("%d", &n_cidades);
    }  else{
        printf("\n----------------------------\n");
        printf("      VALOR REGISTRADO - %d      \n", n_cidades);
        printf("----------------------------\n");
    }
}

void criarCidades(){

    for (int i = 0; i < n_cidades; i++)
    {
        mapa[i].coordX = rand() % 100;
        mapa[i].coordY = rand() % 100;
    }
    
    printf("\n----------------------\n");
    printf("   Cidades criadas.");
    printf("\n----------------------\n");
}

void mostrarCidades(){

    printf("\n---------CIDADES----------\n");
    for (int i = 0; i < n_cidades; i++)
    {
        printf("Cidade %d - (%d, %d)\n" , i, mapa[i].coordX, mapa[i].coordY);
    }
    printf("--------------------------\n");
}

double calcularDistanciaEuclidiana(Cidade cid1, Cidade cid2){

    int diferencaX = cid1.coordX - cid2.coordX;
    int diferencaY = cid1.coordY - cid2.coordY;
    int somaDasDiferencas = pow(diferencaX, 2) + pow(diferencaY, 2);

    double dist = sqrt(somaDasDiferencas);

    return dist;
}

void criarGrafo(){
    for(int i = 0; i < n_cidades; i++){
        for(int j = 0; j < n_cidades; j++){
            grafo[i][j] = calcularDistanciaEuclidiana(mapa[i], mapa[j]);
        }
    }
    
    printf("\n--------------------------------\n");
    for(int i = 0; i < n_cidades; i++){
        for(int j = 0; j < n_cidades; j++){
            printf("%d ", grafo[i][j]);
        }
        printf("\n");
    }
    printf("--------------------------------\n");
}

int encontraProxCidade(int vertice){
    int cidade_adj = -1;
    int menor_dist = MAX;
    
    for(int i = 0; i < n_cidades; i++){
        if(!visitados[i] && grafo[vertice][i] < menor_dist){
            cidade_adj = i;
            menor_dist = grafo[vertice][i];
        }
    }
    
    return cidade_adj;
}

void caxeiroViajante(int cidade){
    int cidade_atual = cidade;
    visitados[cidade] = 1;
    caminho[0] = cidade;
    
    for(int i = 1; i < n_cidades; i++){
       int prox_cidade = encontraProxCidade(cidade);
       caminho[i] = prox_cidade;
       custo_total = custo_total + grafo[cidade_atual][prox_cidade];
       visitados[prox_cidade] = 1;
       cidade_atual = cidade;
    }
    
    caminho[n_cidades] = cidade;
    custo_total = custo_total + grafo[cidade_atual][cidade];
}

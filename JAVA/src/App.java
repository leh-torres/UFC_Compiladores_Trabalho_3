import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math;
import java.time.Duration;
import java.time.Instant;

class Cidade {
    int coordX;
    int coordY;

    Cidade(int x, int y) {
        this.coordX = x;
        this.coordY = y;
    }
}

public class App extends JPanel {
    static final int MAX = 999999;
    static int n_cidades;
    static ArrayList<Cidade> mapa = new ArrayList<>();
    static int[][] grafo = new int[100][100];
    static boolean[] visitados = new boolean[100];
    static int[] caminho = new int[100];
    static int custo_total = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Instant inicio, fim;
        long tempo;

        inicio = Instant.now();
        decideNumeroDeCidades(scanner);
        criarCidades();
        mostrarCidades();
        criarGrafo();

        for (int i = 0; i < n_cidades; i++) {
            visitados[i] = false;
        }

        caxeiroViajante(0);
        fim = Instant.now();

        System.out.println("\nDistancia total: " + custo_total + " km");
        System.out.print("Caminho: ");
        for (int b = 0; b < n_cidades; b++) {
            System.out.print(caminho[b] + " ");
        }

        tempo = Duration.between(inicio, fim).toMillis();
        System.out.println("\nTempo: " + (tempo / 1000.0) + " seg");

        // Inicializando a visualização gráfica
        JFrame frame = new JFrame("Caixeiro Viajante - Visualização com Plano Cartesiano");
        App visualizacao = new App();
        frame.add(visualizacao);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void decideNumeroDeCidades(Scanner scanner) {
        System.out.println("----------------------------");
        System.out.println("Digite o número de cidades desejadas. Este deverá ser maior que 5 e menor que 105.");
        n_cidades = scanner.nextInt();

        if (n_cidades >= 0 && n_cidades < 5) {
            System.out.println("\n----------------------------");
            System.out.println("      VALOR INVÁLIDO !!!      ");
            System.out.println("----------------------------");
            System.out.println("Digite o número de cidades desejadas. Este deverá ser maior que 5 e menor que 105.");
            n_cidades = scanner.nextInt();
        } else {
            System.out.println("\n----------------------------");
            System.out.println("      VALOR REGISTRADO - " + n_cidades + "      ");
            System.out.println("----------------------------");
        }
    }

    public static void criarCidades() {
        Random rand = new Random();

        for (int i = 0; i < n_cidades; i++) {
            int x = rand.nextInt(100);
            int y = rand.nextInt(100);
            mapa.add(new Cidade(x, y));
        }

        System.out.println("\n----------------------");
        System.out.println("   Cidades criadas.");
        System.out.println("----------------------");
    }

    public static void mostrarCidades() {
        System.out.println("\n---------CIDADES----------");
        for (int i = 0; i < n_cidades; i++) {
            Cidade cidade = mapa.get(i);
            System.out.println("Cidade " + i + " - (" + cidade.coordX + ", " + cidade.coordY + ")");
        }
        System.out.println("--------------------------");
    }

    public static double calcularDistanciaEuclidiana(Cidade cid1, Cidade cid2) {
        int diferencaX = cid1.coordX - cid2.coordX;
        int diferencaY = cid1.coordY - cid2.coordY;
        return Math.sqrt(Math.pow(diferencaX, 2) + Math.pow(diferencaY, 2));
    }

    public static void criarGrafo() {
        for (int i = 0; i < n_cidades; i++) {
            for (int j = 0; j < n_cidades; j++) {
                grafo[i][j] = (int) calcularDistanciaEuclidiana(mapa.get(i), mapa.get(j));
            }
        }

        System.out.println("\n--------------------------------");
        for (int i = 0; i < n_cidades; i++) {
            for (int j = 0; j < n_cidades; j++) {
                System.out.print(grafo[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }

    public static int encontraProxCidade(int vertice) {
        int cidade_adj = -1;
        int menor_dist = MAX;

        for (int i = 0; i < n_cidades; i++) {
            if (!visitados[i] && grafo[vertice][i] < menor_dist) {
                cidade_adj = i;
                menor_dist = grafo[vertice][i];
            }
        }

        return cidade_adj;
    }

    public static void caxeiroViajante(int cidade) {
        int cidade_atual = cidade;
        visitados[cidade] = true;
        caminho[0] = cidade;

        for (int i = 1; i < n_cidades; i++) {
            int prox_cidade = encontraProxCidade(cidade);
            caminho[i] = prox_cidade;
            custo_total += grafo[cidade_atual][prox_cidade];
            visitados[prox_cidade] = true;
            cidade_atual = prox_cidade;
        }

        caminho[n_cidades] = cidade;
        custo_total += grafo[cidade_atual][cidade];
    }

    // Método para desenhar as cidades, o caminho e o plano cartesiano no painel gráfico
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        // Convertendo para Graphics2D para ajustar o traço
        Graphics2D g2d = (Graphics2D) g;
    
        // Configurações da escala e dimensões da janela
        int largura = getWidth();
        int altura = getHeight();
        int escala = 5; // Escala para ajustar as coordenadas ao tamanho da tela
    
        // Desenhar o plano cartesiano (somente no quadrante positivo)
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, altura, largura, altura); // Eixo X (na parte inferior)
        g2d.drawLine(0, 0, 0, altura); // Eixo Y (na parte esquerda)
    
        // Aumentar a grossura do traço para desenhar as arestas
        g2d.setStroke(new BasicStroke(2)); // Aumentando a grossura do traço para 2 pixels
    
        // Desenhar todas as arestas com seus pesos
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < n_cidades; i++) {
            for (int j = i + 1; j < n_cidades; j++) { // Desenha apenas uma vez cada aresta
                Cidade cidade1 = mapa.get(i);
                Cidade cidade2 = mapa.get(j);
                int x1 = (cidade1.coordX) * escala;
                int y1 = altura - (cidade1.coordY * escala); // Invertendo Y para coincidir com o quadrante positivo
                int x2 = (cidade2.coordX) * escala;
                int y2 = altura - (cidade2.coordY * escala); // Invertendo Y para coincidir com o quadrante positivo
                g2d.drawLine(x1, y1, x2, y2); // Desenha uma linha entre as cidades
    
                // Desenhar o peso da aresta no meio da linha
                int peso = grafo[i][j];
                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;
                g2d.drawString(Integer.toString(peso), midX, midY);
            }
        }
    
        // Aumentar mais a grossura do traço para destacar o caminho mínimo
        g2d.setStroke(new BasicStroke(3)); // Aumentando a grossura do traço para 3 pixels
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < n_cidades - 1; i++) {
            Cidade cidadeAtual = mapa.get(caminho[i]);
            Cidade proxCidade = mapa.get(caminho[i + 1]);
            int x1 = (cidadeAtual.coordX) * escala;
            int y1 = altura - (cidadeAtual.coordY * escala);
            int x2 = (proxCidade.coordX) * escala;
            int y2 = altura - (proxCidade.coordY * escala);
            g2d.drawLine(x1, y1, x2, y2); // Desenha uma linha entre as cidades
        }
    
        // Fechar o ciclo do caminho mínimo (última cidade para a primeira)
        Cidade ultimaCidade = mapa.get(caminho[n_cidades - 1]);
        Cidade primeiraCidade = mapa.get(caminho[0]);
        int x1 = (ultimaCidade.coordX) * escala;
        int y1 = altura - (ultimaCidade.coordY * escala);
        int x2 = (primeiraCidade.coordX) * escala;
        int y2 = altura - (primeiraCidade.coordY * escala);
        g2d.drawLine(x1, y1, x2, y2); // Desenha a última aresta
    
        // Aumentar o tamanho das bolinhas que representam as cidades
        g2d.setColor(Color.RED);
        int tamanhoBolinhas = 15; // Aumentar o tamanho da bolinha
        for (int i = 0; i < n_cidades; i++) {
            Cidade cidade = mapa.get(i);
            int x = (cidade.coordX) * escala; // Ajustando para quadrante positivo
            int y = altura - (cidade.coordY * escala); // Invertendo Y para coincidir com o quadrante positivo
            g2d.fillOval(x - tamanhoBolinhas / 2, y - tamanhoBolinhas / 2, tamanhoBolinhas, tamanhoBolinhas); // Desenha a cidade como um círculo maior
        }
    }
    

}

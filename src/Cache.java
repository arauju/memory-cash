import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Cache {

	int[] bitVallidade;
	int[] tag;
	int[][] aCash;
	int miss;
	int hit;

	// metodo construtor responsavel pela alocação de espaço das estruturas de
	// dados usados
	public Cache(int tamanhoDaCash, int tamanhoDoBloco) {

		bitVallidade = new int[tamanhoDaCash / tamanhoDoBloco];
		tag = new int[tamanhoDaCash / tamanhoDoBloco];
		aCash = new int[tamanhoDaCash / tamanhoDoBloco][tamanhoDoBloco];
		miss = 0;
		hit = 0;
		for (int i = 0; i < (tamanhoDaCash / tamanhoDoBloco); i++) {
			bitVallidade[i] = 0;
		}
	}

	// metodo responsavel por contabilizar os acertos e erro na cachê
	public static void contadorMissHit(int adress, int tagOfAdress, int newAdress, int offSet, Cache cache,
			int tamanhoDoBloco) {
		if (cache.bitVallidade[newAdress] == 0) {
			cache.miss++;
			cache.bitVallidade[newAdress] = 1;
			cache.tag[newAdress] = tagOfAdress;
			for (int i = 0; i < tamanhoDoBloco; i++) {
				cache.aCash[newAdress][i] = newAdress;
			}
		} else {
			if (cache.tag[newAdress] == tagOfAdress && cache.aCash[newAdress][offSet] == newAdress) {
				cache.hit++;

			} else {
				cache.miss++;
				cache.bitVallidade[newAdress] = 1;
				cache.tag[newAdress] = tagOfAdress;
				for (int i = 0; i < tamanhoDoBloco; i++) {
					cache.aCash[newAdress][i] = newAdress;
				}

			}

		}
	}

	// (calula o tamanho do shift necessario para cada tamanho de offset
	public static int valorOffset(int valor) {
		switch (valor) {
		case 1:
			return 0;
		case 2:
			return 1;
		case 4:
			return 2;
		case 8:
			return 3;
		default:
			return -1;
		}
	}

	// retorna o tamanho da cachê, responsavel pela analise do teste de forma
	// combinatória, executa todas as possiblidades de teste.
	public static int conversorTamanhoCashe(int valor) {
		switch (valor) {
		case 0:
			return 32;
		case 1:
			return 64;
		case 2:
			return 128;
		case 3:
			return 256;
		default:
			return -1;
		}
	}

	public static int conversorTamanhoBloco(int valor) {
		switch (valor) {
		case 0:
			return 1;
		case 1:
			return 2;
		case 2:
			return 4;
		case 3:
			return 8;
		default:
			return -1;
		}
	}

	// metodo responsavel apenas por executar os metodos acima, e realizar a
	// escrita do arquivo
	public static void executarTpDaCache(ArrayList<String> leitura, ArrayList<String> tokens) throws IOException {
		int tamanhoDaCash, tamanhoDoBloco;
		FileWriter arq = new FileWriter("resultadoDaCacheDoTharles.txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		gravarArq.printf("Resultados da Cache Do Tharles %n");
		System.out.println("                         Bem vindo A Cash Do Tharles");

		for (int j = 0; j < 4; j++) {
			tamanhoDaCash = conversorTamanhoCashe(j);
			for (int k = 0; k < 4; k++) {
				tamanhoDoBloco = conversorTamanhoBloco(k);
				Cache cashe = new Cache(tamanhoDaCash, tamanhoDoBloco); // alocação
																		// de
																		// acordo
																		// com
																		// os
																		// parametros
																		// da
																		// cachê

				for (int i = 2; i < tokens.size(); i += 3) {
					String aux = tokens.get(i);
					int adress = Integer.parseInt(aux);
					int tagOfAdress = (int) adress / tamanhoDaCash;
					int newAdress = adress % tamanhoDaCash;
					int offSet = newAdress % tamanhoDoBloco;
					newAdress = newAdress >> (valorOffset(tamanhoDoBloco));
					contadorMissHit(adress, tagOfAdress, newAdress, offSet, cashe, tamanhoDoBloco);

				}

				gravarArq.printf("para uma configuracao de %d blocos, e para cada bloco %d palavra(s): %n",
						conversorTamanhoCashe(j), conversorTamanhoBloco(k));
				gravarArq.printf("miss: %d %n", cashe.miss);
				gravarArq.printf("hit: %d %n", cashe.hit);
				gravarArq.printf("=====================================");
				gravarArq.printf("%n%n");
			}
		}
		gravarArq.close();
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> leitura = new ArrayList<String>();
		ArrayList<String> tokens = new ArrayList<String>();

		try {
			FileReader arq = new FileReader("trace.txt");
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine();
			leitura.add(linha);
			while (linha != null) {
				linha = lerArq.readLine();
				leitura.add(linha);
			}
			leitura.remove(null);
			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		for (String s : leitura) {
			StringTokenizer st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				tokens.add(st.nextToken());
			}
		}

		executarTpDaCache(leitura, tokens);
		System.out.println(
				"Arquivo com os resultados gerado, o arquivo pode ser encontrado na pasta referente ao projeto");

	}
}

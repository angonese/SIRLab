
import java.io.IOException;

public class voa {

	static Forma forma;

	public static void main(String args[]) throws IOException {

		//***** planejamento da missão **********
		// modulo de planejamento da formação
		
		// definir formação do ponto central do GOL

//		//Formação dos Robos
		int vants = 2;
		double[][] posform = new double[vants][2];
		posform[0][0] = 0;
		posform[0][1] = 0;
		posform[1][0] = 1;
		posform[1][1] = 0;
		//posform[2][0] = -1;
		//posform[2][1] = 0;

		forma = new Forma(vants, posform);

		// modulo planejamento da trajetoria
		// substituir para x y da posiçao da bola (gerada aleatoriamente)
		int wtp = 1;
		double[][] wayp = new double[wtp][2];
		
		wayp[0][0] = 5; //bola x
		wayp[0][1] = 5; //bola y

		forma.setWaypoinst(wtp, wayp);
		//forma.detWaypointsExp();	
		//forma.detDistWaypFinal();
		//forma.SalvaArquivoWaypt();
		//***** fim do planejamento da missão **********
		
		//*****        inicio do voo          **********
		forma.setVANTsInitialPosition();
		forma.SalvaArquivoVP();
		forma.SalvaArquivoBP();
		int p = 1;
		do {
			//forma.calculaVelProp();
			//forma.setWaypoinst(wtp, wayp);
			forma.CPAtrativo();
			forma.CPrepulsivo();
			forma.CPTotal();
			forma.DeterminaVelocidades();
			forma.DeterminaHeading();
			forma.AtualizaPosicao();
			forma.AtualizaPosicaoBola();
			forma.SalvaArquivoVP();
			forma.SalvaArquivoBP();
			forma.verificaDistancia();
			p++;
			System.out.println(" Passo "+ p);
		} while (p<=50); //!forma.verificaMissao()

		//*****        fim do voo          **********
	}

}

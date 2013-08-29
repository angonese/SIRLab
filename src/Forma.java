import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Forma {

	public Coordenadas getCentroide() {
		return centroide;
	}

	public void setCentroide() {
		double SomaX = 0, SomaY = 0;
		for (int nv = 0; nv < this.numvants; nv++) {
			SomaX = SomaX + this.VANTs.get(nv).xform;
			SomaY = SomaY + this.VANTs.get(nv).yform;
		}
		this.centroide.X = SomaX / this.numvants;
		this.centroide.Y = SomaY / this.numvants;
	}

	public Coordenadas getWaypoints(int i) {
		return waypoints.get(i);
	}

	public void setWaypoinst(int nwayp, double[][] wayps) {
		this.numwayps = nwayp;
		
		this.bola.X = wayps[0][0];
		this.bola.Y = wayps[0][1];
		
//		for (int i = 0; i < nwayp; i++) {
//			this.waypoints.add(new Coordenadas());
//			this.waypoints.get(i).X = wayps[i][0];
//			this.waypoints.get(i).Y = wayps[i][1];
	//	}
		// for (int j = 0; j < this.numvants; j++) {
		// this.VANTs.get(j).waypI = new Coordenadas[nwayp];
		// }
	}

	public void detWaypointsExp() {
		double ang = 0.0;
		for (int i = 0; i < this.numwayps; i++) {
			if (i < this.numwayps - 1)
				ang = Math
						.atan2((this.waypoints.get(i + 1).X - this.waypoints
								.get(i).X),
								(this.waypoints.get(i + 1).Y - this.waypoints
										.get(i).Y));
			for (int j = 0; j < this.numvants; j++) {
				this.VANTs.get(j).waypI.add(new Coordenadas());
				this.VANTs.get(j).waypI.get(i).X = this.waypoints.get(i).X
						+ (this.VANTs.get(j).dxcentro * Math.cos(ang) + this.VANTs
								.get(j).dycentro * Math.sin(ang));
				this.VANTs.get(j).waypI.get(i).Y = this.waypoints.get(i).Y
						+ (-this.VANTs.get(j).dxcentro * Math.sin(ang) + this.VANTs
								.get(j).dycentro * Math.cos(ang));
			}
		}
	}

	public void detDistWaypFinal() {
		double difcordX = 0.0;
		double difcordY = 0.0;
		double distancia = 0.0;
		for (int j = 0; j < this.numvants; j++) {
			for (int i = 0; i < this.numwayps; i++) {
				this.VANTs.get(j).distWaypFinal.add(new DDouble());
				distancia = 0.0;
				for (int k = i; k < this.numwayps; k++) {

					difcordX = this.VANTs.get(j).waypI.get(i).X
							- this.VANTs.get(j).waypI.get(k).X;
					difcordY = this.VANTs.get(j).waypI.get(i).Y
							- this.VANTs.get(j).waypI.get(k).Y;

					distancia += Math.sqrt(difcordX * difcordX)
							+ (difcordY * difcordY);
				}
				this.VANTs.get(j).distWaypFinal.get(i).v = distancia;
			}
		}
		for (int i = 0; i < this.numwayps; i++) {
			System.out.println("Distancia " + i + " "
					+ this.VANTs.get(0).distWaypFinal.get(i).v);
		}
	}

	public Aero getVANTs(int i) {
		return VANTs.get(i);
	}

	public void setVANTs(double[][] vANTs) {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).xform = vANTs[i][0];
			this.VANTs.get(i).yform = vANTs[i][1];
		}
	}

	public void CalculaDistanPCentroide() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).dxcentro = this.centroide.X
					- this.VANTs.get(i).xform;
			this.VANTs.get(i).dycentro = this.centroide.Y
					- this.VANTs.get(i).yform;
		}
	}

	public void setVANTsInitialPosition() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).VP.X = this.VANTs.get(i).xform;
			this.VANTs.get(i).VP.Y = this.VANTs.get(i).yform;
		}
	}

	public void CPAtrativo() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).CPatrativo();
		}
	}

	public void CPTotal() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).CPTotal();
		}
	}

	public void DeterminaVelocidades() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).determinaVelocidade();
		}
	}

	public void DeterminaHeading() {

		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).determinaHeading();
		}
	}

	public void AtualizaPosicao() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).AtualizaPosicao();
		}
	}
	
	public void AtualizaPosicaoBola() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).AtualizaPosicaoBola();
		}
		
	}

	public void verificaDistancia() {
		for (int i = 0; i < this.numvants; i++) {
			this.VANTs.get(i).verificaDistancia();
		}
	}

	boolean verificaMissao() {
		int somaMC = 0;
		for (int i = 0; i < this.numvants; i++) {
			somaMC += this.VANTs.get(i).missaocumprida;
		}
		if (somaMC == this.numvants)
			return true;
		else
			return false;
	}

	public void calculaVelProp() {
		/*
		 * Este método calcula a distância entre a posição atual de cada VANT
		 * até o waypoint final do planejamento da missão e determina a
		 * velocidade proporcional à distância verificada, respeitando os
		 * limites máx e min. das velocidades.
		 */
		double menordist = 100000000;
		double maiordist = 0;
		double distverificada = 0;
		double dist = 0;
		double distfinal = 0;
		int indiMenor = 0, indiMaior = 0;

		for (int i = 0; i < this.numvants; i++) {
			distverificada = this.VANTs.get(i).verificaDistancia();
			distfinal = this.VANTs.get(i).distWaypFinal
					.get(this.VANTs.get(i).waypAtual).v;
			System.out.println(" Distancia Final : "
					+ this.VANTs.get(i).waypAtual + " - " + distverificada);
			dist = distfinal + distverificada;
			if (dist < menordist) {
				menordist = dist;
				indiMenor = i;
			}
			if (dist > maiordist) {
				maiordist = dist;
				indiMaior = i;
			}
			this.VANTs.get(i).velProp = dist / ((menordist + maiordist) / 2);
		}

		// JOptionPane.showMessageDialog(null,"Parada de Verificação",null,
		// JOptionPane.ERROR_MESSAGE);

		for (int i = 0; i < this.numvants; i++) {
			double Smax = 1;
			double Smin = 0.5;
			double Sm = (Smax + Smin) / 2;

			if (i == indiMenor)
				this.VANTs.get(i).s = Smin;
			else if (i == indiMaior)
				this.VANTs.get(i).s = Smax;
			else if (this.VANTs.get(i).velProp > Smin
					&& this.VANTs.get(i).velProp < Smax)
				this.VANTs.get(i).s = Sm * this.VANTs.get(i).velProp;
			else
				this.VANTs.get(i).s = Sm;

		}

		// if ((i != indiMenor) && (i != indiMaior)) {
		// this.VANTs.get(i).velProp = this.VANTs.get(i)
		// .verificaDistancia() / ((menordist + maiordist) / 2);
		// this.VANTs.get(i).s = this.VANTs.get(i).velProp;
		// } else if ((i == indiMenor)) {
		// this.VANTs.get(i).velProp = this.VANTs.get(i)
		// .verificaDistancia() / (maiordist);
		// this.VANTs.get(i).s = Smin;
		// } else if ((i == indiMaior)) {
		// this.VANTs.get(i).velProp = this.VANTs.get(i)
		// .verificaDistancia() / (menordist);
		// this.VANTs.get(i).s = Smax;

		// }
		// System.out.println("vant " + i + " vel prop: "
		// + this.VANTs.get(i).velProp);

		// if (this.VANTs.get(i).velProp >= 1.5)
		// this.VANTs.get(i).s = Smax;
		// else if (this.VANTs.get(i).velProp > Smin
		// && this.VANTs.get(i).velProp < Smax)
		// this.VANTs.get(i).s = Sm * this.VANTs.get(i).velProp;
		// else if (this.VANTs.get(i).velProp <= Smin)
		// this.VANTs.get(i).s = Smin;
		// }
	}

	public void CPrepulsivo() {
		double difcordX = 0;
		double difcordY = 0;
		double angulo = 0;
		double distancia = 0;
		Coordenadas GradRepSoma = new Coordenadas();

		for (int i = 0; i < this.numvants; i++) {
			GradRepSoma.X = 0;
			GradRepSoma.Y = 0;
			for (int j = 0; j < this.numvants; j++) {
				if (i != j) {
					difcordX = this.VANTs.get(j).VP.X - this.VANTs.get(i).VP.X;
					difcordY = this.VANTs.get(j).VP.Y - this.VANTs.get(i).VP.Y;
					angulo = Math.atan2(difcordX, difcordY);
					distancia = Math.sqrt(difcordX * difcordX + difcordY
							* difcordY);

					// ** regras para determinação do gradiente de repulsão
					if (distancia > this.R + this.S) {
						GradRepSoma.X += 0;
						GradRepSoma.Y += 0;

					}
					if (this.R <= distancia && distancia < this.S + this.R) {
						GradRepSoma.X += -this.betha
								* (this.S + this.R - distancia)
								* Math.cos(angulo);
						GradRepSoma.Y += -this.betha
								* (this.S + this.R - distancia)
								* Math.sin(angulo);

					}
					if (distancia < this.R) {
						GradRepSoma.X += -this.betha * this.R
								* Math.cos(angulo);
						GradRepSoma.Y += -this.betha * this.R
								* Math.sin(angulo);
					}

				}
			}
			this.VANTs.get(i).GradRepulsivo.X = GradRepSoma.X;
			this.VANTs.get(i).GradRepulsivo.Y = GradRepSoma.Y;
		}
	}

	public Forma(int nvants, double[][] vANTs) {
		this.numvants = nvants; // numero de aeronaves
		for (int i = 0; i < nvants; i++)
			this.VANTs.add(new Aero()); // instancia as aeronaves (VANTS)
		this.setVANTs(vANTs);
		//this.setCentroide();
		//this.CalculaDistanPCentroide();
		System.out.println("numwayps " + this.numwayps);
		System.out.println("numvants " + this.numvants);
		//System.out.println("centroide " + this.centroide.X + " "
		//		+ this.centroide.Y);
	}

	public void SalvaArquivoVP() throws IOException {
		// salva no arquivo Voo a posição de cada VANT
		FileWriter voo = new FileWriter(
				"Voo.txt", true);
		PrintWriter gravarVoo = new PrintWriter(voo);

		for (int i = 0; i < this.numvants; i++) {
			gravarVoo.print(this.VANTs.get(i).VP.X + " "
					+ this.VANTs.get(i).VP.Y + " ");
		}
		gravarVoo.println();
		voo.close();
	}
	
	public void SalvaArquivoBP() throws IOException {
		// salva no arquivo Voo a posição de cada VANT
		FileWriter bola = new FileWriter(
				"Bola.txt", true);
		PrintWriter gravarBola = new PrintWriter(bola);

		for (int i = 0; i < this.numvants; i++) {
			gravarBola.print(this.VANTs.get(i).Bola.X + " "
					+ this.VANTs.get(i).Bola.Y + " ");
		}
		gravarBola.println();
		bola.close();
	}

	public void SalvaArquivoWaypt() throws IOException {
		// salva no arquivo os waypoints individuais de cada VANT
		FileWriter Waypt = new FileWriter(
				"Waypoints.txt", true);
		PrintWriter gravarWPT = new PrintWriter(Waypt);

		for (int j = 0; j < this.numwayps; j++) {
			for (int i = 0; i < this.numvants; i++) {
				gravarWPT.print(this.VANTs.get(i).waypI.get(j).X + " "
						+ this.VANTs.get(i).waypI.get(j).Y + " ");
			}
			gravarWPT.println();
		}
		Waypt.close();
	}

	int numwayps;
	int numvants;
	double betha = 0.5;
	double R = 0.2; // raio de repulsão
	double S = 0.2; // area de influencia de repulsao
	Coordenadas centroide = new Coordenadas();
	ArrayList<Coordenadas> waypoints = new ArrayList<Coordenadas>(); // lista de
	Coordenadas bola = new Coordenadas();			// bola
	public ArrayList<Aero> VANTs = new ArrayList<Aero>(); // lista de aeronaves


}

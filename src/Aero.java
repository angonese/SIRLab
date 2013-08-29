/*
 * Metodo Baseado em Goodrich 2002 para combinação de campos atrativos e repulsivos
 * Autor: Alberto Torres Angonese
 * data: 29/10/2012
 */

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

//import org.jfree.chart.ChartFrame;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.ChartUtilities;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.plot.CategoryPlot;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.category.LineAndShapeRenderer;
//import org.jfree.chart.renderer.xy.XYItemRenderer;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.jfree.chart.title.TextTitle;
//import org.jfree.data.category.CategoryDataset;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.data.xy.XYDataset;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//import org.jfree.ui.HorizontalAlignment;
//import org.jfree.ui.RectangleEdge;

//import com.lowagie.text.Font;
//import src.com.panayotis.gnuplot.*;
//import com.panayotis.gnuplot.JavaPlot;
//import com.panayotis.gnuplot.swing.JPlot;
import java.util.ArrayList;

public class Aero  {
	ArrayList<Coordenadas> waypI;
	ArrayList<DDouble> distWaypFinal;
	
	int waypAtual = 0;
	double X = 2, xform=0.0; // posição x
	double Y = 5, yform=0.0; // posição y
	double dxcentro, dycentro;
	
	Coordenadas GradAtrativo = new Coordenadas();
	Coordenadas GradRepulsivo = new Coordenadas();
	Coordenadas GradTotal = new Coordenadas();
	Coordenadas VP = new Coordenadas();
	Coordenadas Bola = new Coordenadas();
	
	private double velocidade;
	public double velProp;
	private double heading;
	
	int missaocumprida = 0;
	
	
	double r = 0.5; // raio de atracao do ponto objetivo
	double s = 0.5; // area de influencia de atracao

	double theta; // direção de movimento
	double alpha = 1; // fator de força de atração
	
	double XPlAng = 0; //Angulo para o heading do XPlane
	
	double GP[] = new double[2]; // "Goal Point - Ponto objetivo"
	
	double OP[] = new double[2]; // " Obstacle Point - Ponto de Obstáculo
	int WPAtual;
	

	
	int i;
	double distancia;

	double[] GradienteTotal = new double[2];


	public Aero (){
		this.waypI = new ArrayList<Coordenadas>();
		this.distWaypFinal = new ArrayList<DDouble>();
		this.xform = 0.0;
		this.yform = 0.0;
	}
	
	public void CPatrativo() {
			
//		double difcordX = this.waypI.get(waypAtual).X - this.VP.X;
//		double difcordY = this.waypI.get(waypAtual).Y - this.VP.Y;
		double difcordX = this.Bola.X - this.VP.X;
		double difcordY = this.Bola.Y - this.VP.Y;

		double theta = Math.atan2(difcordX, difcordY);

		distancia = Math.sqrt(difcordX * difcordX)
				+ (difcordY * difcordY);

		// ** regras para determinação do gradiente de atração
		if (distancia < r) {
			this.GradAtrativo.X = 0;
			this.GradAtrativo.Y = 0;
		}
		//+ this.s
		if (r <= distancia && distancia <= r) {
			this.GradAtrativo.X = this.alpha * (distancia - r) * Math.cos(theta);
			this.GradAtrativo.Y = this.alpha * (distancia - r) * Math.sin(theta);
		}
		if (distancia > r) {
			this.GradAtrativo.X = this.alpha * this.s * Math.cos(theta);
			this.GradAtrativo.Y = this.alpha * this.s * Math.sin(theta);
		}

	}

	void CPTotal(){
		
		this.GradTotal.X = this.GradAtrativo.X + this.GradRepulsivo.X;
		this.GradTotal.Y = this.GradAtrativo.Y + this.GradRepulsivo.Y;
		
	}
	

	public void determinaVelocidade() {
		this.velocidade = Math.sqrt((this.GradTotal.X * this.GradTotal.X) + (this.GradTotal.Y * this.GradTotal.Y));

	}

	double determinaAngulo(double GradX, double GradY) {
		double angulo = Math.atan2(GradX, GradY);
		// double theta2 = Math.toDegrees(angulo);
		return angulo;
	}
	public void determinaHeading() {
		this.heading = Math.atan2(this.GradTotal.X, this.GradTotal.Y);

	}

	double[] atualizaPosicao(double X, double Y, double V, double t, double a) {
		double[] novaposicao = new double[2];
		novaposicao[0] = X + (V * Math.cos(a)) * t;
		novaposicao[1] = Y + (V * Math.sin(a)) * t;
		return novaposicao;
	}
	
	// atualiza posição dos robos no sistema real será atualizado pela camera. 
	public void AtualizaPosicao() {
		double t = 1;
		this.VP.X = this.VP.X + (this.velocidade * Math.cos(this.heading)) * t;
		this.VP.Y = this.VP.Y + (this.velocidade * Math.sin(this.heading)) * t;	
	}
// criar atualizaPosicaoBola() randomicamente na simulação e no sistema real será atualizado pela camera
	public void AtualizaPosicaoBola() {
		Random posBolax = new Random();
		Random posBolay = new Random();
		this.Bola.X += posBolax.nextFloat();
		this.Bola.Y += posBolay.nextFloat();
//		this.Bola.X = 5;
//		this.Bola.Y = 5;
		
	}
	
	double[] rotacionaFormacao(double X, double Y, double a) {
		double[] rotxy = new double[2];
//		System.out.println("X : " + X);
//		System.out.println("Y : " + Y);
		// double [] pontorotacionado = new double[2];
		rotxy[0] = X * Math.cos(a) + Y * Math.sin(a);
		rotxy[1] = -X * Math.sin(a) + Y * Math.cos(a);
		return rotxy;
		// System.out.println("rotx: "+rotxy[0]);
		// System.out.println("roty: "+rotxy[1]);

	}

	double[] calculaCentroide(int vants, double [][] PosVant) {
		double[] centroide = new double[2];
		double SomaX = 0;
		double SomaY = 0;
		for(int i = 0; i<vants; i++){
			SomaX = SomaX+PosVant[i][0];
			SomaY = SomaY+PosVant[i][1];
		}
		centroide[0] = SomaX / vants;
		centroide[1] = SomaY / vants;
		return centroide;
	}

	double[] posicionaFormacao(double centroideX, double centroideY, double a,
			double X, double Y) {
		double[] posicaoFormacao = new double[2];
		posicaoFormacao[0] = centroideX + Math.cos(a) * X;
		posicaoFormacao[0] = centroideY + Math.sin(a) * Y;
		return posicaoFormacao;
	}
	
	public double verificaDistancia() {
		double offset=0; // deslocamento para identificação do waypoint	
//		double difcordX = this.waypI.get(waypAtual).X - this.VP.X;
//		double difcordY = this.waypI.get(waypAtual).Y - this.VP.Y;
		double difcordX = this.Bola.X - this.VP.X;
		double difcordY = this.Bola.Y - this.VP.Y;
		distancia = Math.sqrt(difcordX * difcordX)
				+ (difcordY * difcordY);	
		if (distancia <= r + offset) {
//			if (waypAtual == this.waypI.size()-1){
//				this.missaocumprida = 1;
//			}else{
//				waypAtual++;
//			}
		}
		return distancia;
	}
	
	double convertXPlAng (double SimAng){
		
		if (SimAng < -110 && SimAng >= -180) 
			XPlAng = 110+SimAng+360;
		else
			XPlAng = (SimAng +110);;	
		return XPlAng;	
	}
	
	void SalvaArquivoGradiente(double Grad1X, double Grad1Y, double Grad2X,
			double Grad2Y, double Grad3X, double Grad3Y) throws Exception {

		// Gravando no arquivo
		/*
		 * ile Data = new
		 * File("C:/Users/Angonese/workspace/CampoPotencial/Data.txt");
		 * FileOutputStream fos = new FileOutputStream(Data,true); String texto
		 * = X+" "+Y+" \n"; fos.write(texto.getBytes()); fos.close();
		 */
		// arquivos com as coordenadas dos VANTs 1, 2 e 3
		FileWriter grad1 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/GRAD1.txt", true);
		PrintWriter gravarGrad1 = new PrintWriter(grad1);
		FileWriter grad2 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/GRAD2.txt", true);
		PrintWriter gravarGrad2 = new PrintWriter(grad2);
		FileWriter grad3 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/GRAD3.txt", true);
		PrintWriter gravarGrad3 = new PrintWriter(grad3);

		gravarGrad1.println(Grad1X + " " + Grad1Y + " " + Grad2X + " " + Grad2Y
				+ " " + Grad3X + " " + Grad3Y);
		gravarGrad2.println(Grad2X + " " + Grad2Y);
		gravarGrad3.println(Grad3X + " " + Grad3Y);

		grad1.close();
		grad2.close();
		grad3.close();
	}

	void SalvaEmArquivo2(double [][] posvant,double [][] gp, int nvants)throws Exception{
//		
//		
//		try {  
//	        FileWriter gravacao = new FileWriter("Voo.txt");  
//	        BufferedWriter bf = new BufferedWriter(gravacao);  
//	        for (int i = 0; i < nvants; i++){  
//	            bf.write(posvant[i][0]+" "+posvant[i][1]+" "+gp[i][0]+" "+gp[i][1]);  
//	            bf.newLine();  
//	        }  
//	        bf.flush();  
//	        bf.close();  
//	    } catch ( IOException e ) { e.printStackTrace(); }  
//		
//		

				
		FileWriter Voo = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/Voo.txt",
				true);
		PrintWriter gravarVoo = new PrintWriter(Voo);
		
		
		for ( i= 0; i<nvants;i++){	
			gravarVoo.print(posvant[i][0]+" "+posvant[i][1]+" "+gp[i][0]+" "+gp[i][1]+" ");
		}
		gravarVoo.println();
		
		Voo.close();	
	}

	void SalvaEmArquivo(double X1, double Y1, double X2, double Y2, double X3,
			double Y3, double GP1x, double GP1y, double GP2x, double GP2y,
			double GP3x, double GP3y, double centroideX, double centroideY, double angulo)
			throws Exception {
		i++;
		// Gravando no arquivo
		/*
		 * ile Data = new
		 * File("C:/Users/Angonese/workspace/CampoPotencial/Data.txt");
		 * FileOutputStream fos = new FileOutputStream(Data,true); String texto
		 * = X+" "+Y+" \n"; fos.write(texto.getBytes()); fos.close();
		 */
		// arquivos com as coordenadas dos VANTs 1, 2 e 3
		FileWriter arq1 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/VANT1.txt", true);
		PrintWriter gravarArq1 = new PrintWriter(arq1);
		FileWriter arq2 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/VANT2.txt", true);
		PrintWriter gravarArq2 = new PrintWriter(arq2);
		FileWriter arq3 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/VANT3.txt", true);
		PrintWriter gravarArq3 = new PrintWriter(arq3);

		// arquivos com as posições objetivo dos VANTs 1, 2 e 3
		FileWriter PO1 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/PO1.txt", true);
		PrintWriter gravarPO1 = new PrintWriter(PO1);
		FileWriter PO2 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/PO2.txt", true);
		PrintWriter gravarPO2 = new PrintWriter(PO2);
		FileWriter PO3 = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/PO3.txt", true);
		PrintWriter gravarPO3 = new PrintWriter(PO3);
		FileWriter Ctd = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/Centroide.txt",
				true);
		PrintWriter gravarCtd = new PrintWriter(Ctd);
		
//		FileWriter Voo = new FileWriter(
//				"C:/Users/Angonese/workspace/CampoPotencial/Voo.txt",
//				true);
//		PrintWriter gravarVoo = new PrintWriter(Voo);
		
		FileWriter Ang = new FileWriter(
				"C:/Users/Angonese/workspace/CampoPotencial/Angulo.txt",
				true);
		PrintWriter gravarAng = new PrintWriter(Ang);
		

		gravarArq1.println(X1 + " " + Y1);
		gravarArq2.println(X2 + " " + Y2);
		gravarArq3.println(X3 + " " + Y3);

		gravarPO1.println(GP1x + " " + GP1y);
		gravarPO2.println(GP2x + " " + GP2y);
		gravarPO3.println(GP3x + " " + GP3y);
		gravarCtd.println(centroideX + " " + centroideY);
		
		//gravarVoo.println(centroideX + " " + centroideY+" "+GP1x + " " + GP1y+" "+" "+GP2x + " " + GP2y+" "+" "+GP3x + " " + GP3y+" "+X1 + " " + Y1+" "+" "+X2 + " " + Y2+" "+X3 + " " + Y3);

		gravarAng.println(angulo);
		
		arq1.close();
		arq2.close();
		arq3.close();
		PO1.close();
		PO2.close();
		PO3.close();
		Ctd.close();
		//Voo.close();
		Ang.close();
	}







	
	//void PlotaGrafico(XYSeries VANT1,XYSeries VANT2, XYSeries VANT3, XYSeries PO1,XYSeries PO2,XYSeries PO3 ) {
	
}

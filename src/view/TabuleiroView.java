package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import model.Bomba;
import model.Obstaculo;
import model.Robo;
import model.RoboInteligente;
import model.Tabuleiro;

public class TabuleiroView {

	private static final int TAMANHO_CELULA = 90;
	private static final int TAMANHO_ICONE  = 58;

	private final GridPane grid;

	private final Image imgAlimento;
	private final Image imgBomba;
	private final Image imgRocha;
	private final Image imgRoboNormal;
	private final Image imgRoboInteligente;

	public TabuleiroView(GridPane grid) {
		this.grid = grid;
		configurarGrid();

		imgAlimento        = carregarImagem("resources/images/alimento.png");
		imgBomba           = carregarImagem("resources/images/bomba.png");
		imgRocha           = carregarImagem("resources/images/rocha.png");
		imgRoboNormal      = carregarImagem("resources/images/robo_normal.png");
		imgRoboInteligente = carregarImagem("resources/images/robo_inteligente.png");

		configurarGrid();
	}

	private Image carregarImagem(String caminho) {
		var stream = getClass().getResourceAsStream(caminho);
		if (stream == null) {
			System.err.println("⚠ Imagem não encontrada: " + caminho);
			return null;
		}
		return new Image(stream);
	}

	private void configurarGrid() {
		grid.setHgap(4);
		grid.setVgap(4);
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: #1A1A2E; -fx-padding: 8;");
	}

	public void renderizar(Tabuleiro tabuleiro) {
		grid.getChildren().clear();

		desenharFundo(tabuleiro);
		desenharAlimento(tabuleiro);
		desenharObstaculos(tabuleiro);
		desenharRobos(tabuleiro);
	}

	private void desenharFundo(Tabuleiro tabuleiro) {
		for (int x = 0; x < Tabuleiro.TAMANHO; x++) {
			for (int y = 0; y < Tabuleiro.TAMANHO; y++) {
				grid.add(criarCelulaVazia(x, y), x, Tabuleiro.TAMANHO - 1 - y);
			}
		}
	}

	private StackPane criarCelulaVazia(int x, int y) {
		Rectangle fundo = new Rectangle(TAMANHO_CELULA, TAMANHO_CELULA);
		fundo.setArcWidth(10);
		fundo.setArcHeight(10);
		fundo.setFill(Color.web((x + y) % 2 == 0 ? "#16213E" : "#0F3460"));

		Label coord = new Label(x + "," + y);
		coord.setFont(Font.font("Monospace", 9));
		coord.setTextFill(Color.web("#FFFFFF30"));
		StackPane.setAlignment(coord, Pos.BOTTOM_RIGHT);

		StackPane celula = new StackPane(fundo, coord);
		celula.setPrefSize(TAMANHO_CELULA, TAMANHO_CELULA);
		return celula;
	}

	private void desenharAlimento(Tabuleiro tabuleiro) {
		int ax = tabuleiro.getAlimentoX();
		int ay = tabuleiro.getAlimentoY();
		if (ax < 0 || ay < 0) return;

		// imgAlimento pode ser null se o arquivo não existir —
		// criarIcone() trata esse caso com fallback de emoji.
		StackPane icone = criarIcone(imgAlimento, "🍎", "#00FF87");
		grid.add(icone, ax, Tabuleiro.TAMANHO - 1 - ay);
	}

	private void desenharObstaculos(Tabuleiro tabuleiro) {
		for (Obstaculo obs : tabuleiro.getObstaculos()) {
			StackPane icone;
			if (obs instanceof Bomba) {
				icone = criarIcone(imgBomba, "💣", "#FF4444");
			} else {
				icone = criarIcone(imgRocha, "🪨", "#8B7355");
			}
			grid.add(icone, obs.getEixoX(), Tabuleiro.TAMANHO - 1 - obs.getEixoY());
		}
	}

	private void desenharRobos(Tabuleiro tabuleiro) {
		for (Robo robo : tabuleiro.getRobos()) {
			if (robo.getEixoX() == -1) continue; // destruído

			boolean inteligente = robo instanceof RoboInteligente;
			Image img = inteligente ? imgRoboInteligente : imgRoboNormal;
			StackPane icone = criarIconeRobo(img, robo.getCor(), inteligente);
			grid.add(icone, robo.getEixoX(), Tabuleiro.TAMANHO - 1 - robo.getEixoY());
		}
	}

	private StackPane criarIcone(Image imagem, String emojiFallback, String corFundo) {
		StackPane pane = new StackPane();
		pane.setPrefSize(TAMANHO_CELULA, TAMANHO_CELULA);

		if (imagem != null) {
			// Imagem encontrada — usa ela
			ImageView iv = new ImageView(imagem);
			iv.setFitWidth(TAMANHO_ICONE);
			iv.setFitHeight(TAMANHO_ICONE);
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			pane.getChildren().add(iv);
		} else {
			Label label = new Label(emojiFallback);
			label.setFont(Font.font(28));
			pane.getChildren().add(label);
		}

		return pane;
	}

	private StackPane criarIconeRobo(Image imagem, String cor, boolean inteligente) {
		StackPane pane = new StackPane();
		pane.setPrefSize(TAMANHO_CELULA, TAMANHO_CELULA);

		if (imagem != null) {
			ImageView iv = new ImageView(imagem);
			iv.setFitWidth(TAMANHO_ICONE);
			iv.setFitHeight(TAMANHO_ICONE);
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			pane.getChildren().add(iv);
		} else {
			Label label = new Label(inteligente ? "🤖" : "🦾");
			label.setFont(Font.font(28));
			pane.getChildren().add(label);
		}

		Rectangle borda = new Rectangle(TAMANHO_ICONE, TAMANHO_ICONE);
		borda.setFill(Color.TRANSPARENT);
		borda.setArcWidth(8);
		borda.setArcHeight(8);
		borda.setStrokeWidth(inteligente ? 3 : 2);
		borda.setStroke(inteligente
				? Color.GOLD
				: Color.web(cor, 0.8) // cor do robô com 80% de opacidade
		);
		pane.getChildren().add(borda);

		return pane;
	}
}
package eternal.fire;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Draw extends Application {
    private XY origin;
    private XY rot;
    private XY scale;

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        lexer.lexerTest();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawDot(context, 250, 250);
        root.getChildren().add(canvas);
        primaryStage.setTitle("Draw");
        primaryStage.getIcons().add(new Image(Draw.class.getResourceAsStream("/draw.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawDot(GraphicsContext context, double x, double y) {
        context.setFill(Color.GREEN);
        context.setLineWidth(5);
        context.fillOval(x, y, 5, 5);
    }
}

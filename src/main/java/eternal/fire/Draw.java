package eternal.fire;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Draw extends Application {
    private XY origin;
    private XY rot;
    private XY scale;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Draw");
        Group root = new Group();
        Canvas canvas = new Canvas(200, 200);
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawDot(context);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawDot(GraphicsContext context) {
        context.setFill(Color.GREEN);
        context.setLineWidth(5);
        context.fillOval(10, 60, 5, 5);
    }
}

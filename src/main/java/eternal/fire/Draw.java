package eternal.fire;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class Draw extends Application {
    double start;
    double end;
    double step;
    ExprNode horizontalT;
    ExprNode verticalT;
    GraphicsContext context;
    Logger logger = LoggerFactory.getLogger(Draw.class);
    private Lexer lexer;
    private Token token;
    private double originX = 0;
    private double originY = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double rotAngle = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("WWW");
        logger.info("WTF");
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        context = canvas.getGraphicsContext2D();
//        drawDot(context, 250, 250);
//        drawDots();
        root.getChildren().add(canvas);
        primaryStage.setTitle("Draw");
        primaryStage.getIcons().add(new Image(Draw.class.getResourceAsStream("/draw.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Parser parser = new Parser(context);
        parser.parse();
    }


}

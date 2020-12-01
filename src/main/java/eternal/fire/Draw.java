package eternal.fire;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Draw extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        var context = canvas.getGraphicsContext2D();
        Parser parser = new Parser(context);
        parser.parse();
        root.getChildren().add(canvas);
        primaryStage.setTitle("Draw");
        primaryStage.getIcons().add(new Image(Draw.class.getResourceAsStream("/draw.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

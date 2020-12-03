package eternal.fire;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import eternal.fire.syntax.Parser;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Draw extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Draw.class);
    private static final String BLOG_URL = "https://linjinming.gitee.io/2020/11/24/%E5%87%BD%E6%95%B0%E7%BB%98%E5%9B%BE%E8%AF%AD%E8%A8%80%E4%B9%8B%E8%A7%A3%E9%87%8A%E5%99%A8/";
    private static final String GITHUB_URL = "https://github.com/carpediemtal/Drawlang-Interpreter";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 根布局
        SplitPane root = new SplitPane();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // 画板
        Group group = new Group();
        Canvas canvas = new Canvas(500, 500);
        var context = canvas.getGraphicsContext2D();
        context.setFill(Color.GREEN);
        Parser parser = new Parser(context);
        parser.parse();

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Label optionsLabel = new Label("Options");
        optionsLabel.setFont(Font.font("Consolas", 30));

        // 拾色器
        ColorPicker colorPicker = new ColorPicker(Color.GREEN);
        colorPicker.setOnAction(event -> {
            logger.info("画笔颜色改变为{}", colorPicker.getValue());
            context.clearRect(0, 0, 500, 500);
            context.setFill(colorPicker.getValue());
            parser.getLexer().reload();
            parser.parse();
        });

        // 滑动按钮
        JFXSlider slider = new JFXSlider(0, 10, 0.5);
        slider.setValue(5);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("画笔宽度改变为{}", newValue.doubleValue());
            context.clearRect(0, 0, 500, 500);
            parser.setDotSize(newValue.doubleValue());
            parser.getLexer().reload();
            parser.parse();
        });

        // Help
        JFXButton helpBtn = new JFXButton("Help");
        helpBtn.setMaxWidth(90);
        helpBtn.getStyleClass().add("button-raised");
        helpBtn.setStyle("-fx-background-color: #0F9D58");
        /*var helpIcon = new ImageView(new Image(Draw.class.getResourceAsStream("/help.png")));
        helpIcon.setFitWidth(25);
        helpIcon.setFitHeight(25);
        helpBtn.setGraphic(helpIcon);*/
        helpBtn.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(BLOG_URL));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        // Github
        JFXButton githubBtn = new JFXButton("Github");
        githubBtn.setMaxWidth(90);
        githubBtn.getStyleClass().add("button-raised");

        var githubIcon = new ImageView(new Image(Draw.class.getResourceAsStream("/github.png")));
        githubIcon.setFitWidth(20);
        githubIcon.setFitHeight(20);
        githubBtn.setGraphic(githubIcon);
        githubBtn.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(GITHUB_URL));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // 右侧面板布局
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);

        // 层次
        group.getChildren().add(canvas);
        vBox.getChildren().addAll(optionsLabel, colorPicker, slider, helpBtn, githubBtn);
        root.getItems().addAll(group, vBox);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Scene scene = new Scene(root);
        scene.getStylesheets().add(Draw.class.getResource("/css/jfoenix-components.css").toExternalForm());

        primaryStage.setTitle("Draw");
        primaryStage.getIcons().add(new Image(Draw.class.getResourceAsStream("/draw.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

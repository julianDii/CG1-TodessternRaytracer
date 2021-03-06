/*
 * MIT License
 *
 * Copyright (c) 2016 Julian Dobrot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ourWindow.zusatzaufgabeGui.windows.geometrieWindows;

import camera.OrthographicCamera;
import camera.PerspectiveCamera;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import camera.Camera;
import matVecLib.Point3;
import matVecLib.Vector3;
import ourWindow.zusatzaufgabeGui.windows.NumberField;
import ourWindow.zusatzaufgabeGui.windows.TodessternGUI;
import sampling.SamplingPattern;


/**
 * This class represents a gui to select and build new cameras.
 * Created by Juliand on 25.11.15.
 */

public class CameraWindow extends Stage {

    private final  Button add = new Button("ADD");
    private final Button can = new Button("CANCEL");
    private ToggleGroup tog;
    private final RadioButton togOrto = new RadioButton("Orthographic");
    private final RadioButton togPer = new RadioButton("Perspective");
    private final NumberField punktx = new NumberField("0");
    private final NumberField punkty = new NumberField("0");
    private final NumberField punktz = new NumberField("0");
    private final NumberField directionx = new NumberField("0");
    private final NumberField directiony = new NumberField("0");
    private final NumberField directionz = new NumberField("-1");
    private final NumberField rotationx = new NumberField("0");
    private final NumberField rotationy = new NumberField("1");
    private final NumberField rotationz = new NumberField("0");
    private  NumberField sampling = new NumberField("1");
    private final NumberField scaling = new NumberField("4");
    private final GridPane root = new GridPane();
    private final BorderPane border = new BorderPane();

    /**
     * This constructor builds a new camera window.
     */
    public CameraWindow(){

        Scene sceneCamera = new Scene(border);
        initRoot();
        this.setWidth(300);
        this.setHeight(120);
        this.setTitle("Cam Menu");
        this.setScene(sceneCamera);
        this.initModality(Modality.APPLICATION_MODAL);
        this.showAndWait();
    }

    /**
     * This method creates the layout of the camera window.
     */
    private void initRoot() {

        //----------------------------
        border.setCenter(root);

        root.setPadding(new Insets(3,10,10,30));
        root.setVgap(5);
        root.setHgap(5);


        add.setMaxSize(100, 10);
        can.setMaxSize(100, 10);


        tog = new ToggleGroup();
        togOrto.setToggleGroup(tog);
        togPer.setToggleGroup(tog);
        HBox togBox = new HBox();
        togBox.setPadding(new Insets(15, 3, 3, 30));
        HBox btnBox = new HBox();
        btnBox.setPadding(new Insets(3,3,15,115));
        togBox.setSpacing(20);
        togBox.getChildren().addAll(togOrto,togPer);
        btnBox.setSpacing(5);
        btnBox.getChildren().addAll(can);
        border.setTop(togBox);
        border.setBottom(btnBox);


        // Events
        add.setOnAction(e->addCamera());
        can.setOnAction(e->this.close());
        //------------------------------------------------------------------------------------//

        tog.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (tog.getSelectedToggle() == togOrto) {
                this.setHeight(280);

                root.getChildren().clear();
                btnBox.getChildren().clear();
                btnBox.getChildren().addAll(add,can);
                btnBox.setPadding(new Insets(3,3,15,90));

                Label e = new Label("POSITION");

                root.add(e, 0, 3);
                root.add(punktx, 1, 3);
                root.add(punkty, 2, 3);
                root.add(punktz, 3, 3);

                Label g = new Label("DIRECTION");

                root.add(g, 0, 4);
                root.add(directionx, 1, 4);
                root.add(directiony, 2, 4);
                root.add(directionz, 3, 4);

                Label t = new Label("ROTATION");

                root.add(t, 0, 5);
                root.add(rotationx, 1, 5);
                root.add(rotationy, 2, 5);
                root.add(rotationz, 3, 5);

                Label sampl = new Label("Sampling");

                root.add(sampl, 0, 6);
                root.add(sampling, 1, 6);

                Label s = new Label("Scaling");

                root.add(s, 0, 7);
                root.add(scaling, 1, 7);

                border.setBottom(btnBox);

            } else {

                if (tog.getSelectedToggle() == togPer) {

                    this.setHeight(280);

                    root.getChildren().clear();
                    btnBox.getChildren().clear();
                    btnBox.getChildren().addAll(add,can);
                    btnBox.setPadding(new Insets(3,3,15,90));

                    Label e = new Label("POSITION");

                    root.add(e, 0, 3);
                    root.add(punktx, 1, 3);
                    root.add(punkty, 2, 3);
                    root.add(punktz, 3, 3);

                    Label g = new Label("DIRECTION");

                    root.add(g, 0, 4);
                    root.add(directionx, 1, 4);
                    root.add(directiony, 2, 4);
                    root.add(directionz, 3, 4);

                    Label t = new Label("ROTATION");

                    root.add(t, 0, 5);
                    root.add(rotationx, 1, 5);
                    root.add(rotationy, 2, 5);
                    root.add(rotationz, 3, 5);

                    Label sampl = new Label("Sampling");

                    root.add(sampl, 0, 6);
                    root.add(sampling, 1, 6);

                    Label s = new Label("Angle");

                    root.add(s, 0, 7);
                    root.add(scaling, 1, 7);

                    border.setBottom(btnBox);
                }
            }
        });
    }

    /**
     * This method calls the createCmera method eith the selected toggle.
     */
    private void addCamera () {

        createCamera(tog.getSelectedToggle());
        this.close();

    }

    /**
     * This method creates a new camera with the given parameters from the gui.
     * @param selectedToggle
     * @return The new camera.
     */
    private Camera createCamera (Toggle selectedToggle) {

        Point3 point = new Point3(punktx.getNumber(), punkty.getNumber(), punktz.getNumber());
        Vector3 direction = new Vector3(directionx.getNumber(), directiony.getNumber(), directionz.getNumber());
        Vector3 rotation = new Vector3(rotationx.getNumber(), rotationy.getNumber(), rotationz.getNumber());

        Integer sa = (int)sampling.getNumber();
        SamplingPattern samplingpattern = new SamplingPattern(sa);

        Double sca = new Double(scaling.getNumber());

        if (selectedToggle == togOrto) {

            OrthographicCamera orthoCam = new OrthographicCamera(point, direction, rotation, samplingpattern, sca);
            TodessternGUI.cam=orthoCam;

            return orthoCam;
        }

        if (selectedToggle == togPer){
            PerspectiveCamera perCam = new PerspectiveCamera(point, direction, rotation, samplingpattern, Math.PI/sca);
            TodessternGUI.cam=perCam;
            return perCam;
        }
        return null;
    }

}



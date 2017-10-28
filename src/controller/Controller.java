package controller;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.CGraph;
import model.Edge;
import model.Vertex;
import view.CustomSwingNode;

import java.awt.*;

public class Controller {

    @FXML
    private Label labelNumberOfVertex;

    @FXML
    private Slider sliderNumberOfVertex;

    @FXML
    private Pane paneGraph;

    @FXML
    private ToggleGroup toggleGroupAlgorythms;

    @FXML
    private HBox hBoxSelectVertices;

    @FXML
    private RadioButton radioButtonLevit;

    @FXML
    private ChoiceBox choiceBoxVertex1;

    @FXML
    private ChoiceBox choiceBoxVertex2;

    private CustomSwingNode graphPanel;

    @FXML
    private void initialize() {
        labelNumberOfVertex.textProperty().bind(Bindings.format("%.0f", sliderNumberOfVertex.valueProperty()));
        toggleGroupAlgorythms.selectedToggleProperty().addListener((ov, old_val, new_val) -> {
            hBoxSelectVertices.setVisible(radioButtonLevit.isSelected());
            hBoxSelectVertices.setMinHeight(radioButtonLevit.isSelected() ? HBox.USE_COMPUTED_SIZE : 0);
            hBoxSelectVertices.setPrefHeight(radioButtonLevit.isSelected() ? HBox.USE_COMPUTED_SIZE : 0);
        });

        graphPanel = new CustomSwingNode();
        paneGraph.getChildren().add(graphPanel);
        paneGraph.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            graphPanel.resize(newValue.getWidth(), newValue.getHeight());
        });
    }

    BasicVisualizationServer<Vertex, Edge> vv;

    public void buildGraph(ActionEvent actionEvent) {
        if (labelNumberOfVertex.getText().equals("0")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка при построении графа");
            alert.setHeaderText(null);
            alert.setContentText("Количество вершин должно быть больше 0");

            alert.showAndWait();
            return;
        }


        Bounds bounds = paneGraph.getBoundsInParent();

        CGraph cg = CGraph.getInstance().create(Integer.parseInt(labelNumberOfVertex.getText()));
        Layout<Vertex, Edge> layout = new ISOMLayout<>(cg.getCurrentGraph());
        layout.setSize(new Dimension((int)bounds.getWidth()-25, (int)bounds.getHeight()-25));
        vv = new BasicVisualizationServer<Vertex, Edge>(layout);
        vv.setSize(new Dimension((int)bounds.getWidth(), (int)bounds.getHeight()));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexFillPaintTransformer(vertex -> Color.ORANGE);

        graphPanel.setContent(vv);
        System.out.println(graphPanel.getBoundsInLocal());

        ObservableList vertices = cg.getVertices();

        choiceBoxVertex1.setItems(vertices);
        choiceBoxVertex1.setValue(vertices.get(0));

        choiceBoxVertex2.setItems(vertices);
        choiceBoxVertex2.setValue(vertices.get(vertices.size()-1));


    }

    public void testPressed(ActionEvent actionEvent) {
        vv.getRenderContext().setVertexFillPaintTransformer(vertex -> {
            if(vertex.equals(new Vertex(1)))return Color.CYAN;
            return Color.ORANGE;
        });
        vv.getRenderContext().setEdgeDrawPaintTransformer(edge ->{
            if(edge.getId().equals("1-2"))return Color.RED;
            return Color.BLACK;
        });
        vv.updateUI();
    }
}

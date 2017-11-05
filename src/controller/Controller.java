package controller;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.CGraph;
import model.Edge;
import model.Vertex;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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

    @FXML
    private VBox vBoxAlgorithms;

    @FXML
    private Button buttonStartAlgorithm;

    private SwingNode graphPanel;

    @FXML
    private void initialize() {
        labelNumberOfVertex.textProperty().bind(Bindings.format("%.0f", sliderNumberOfVertex.valueProperty()));
        toggleGroupAlgorythms.selectedToggleProperty().addListener((ov, old_val, new_val) -> {
            hBoxSelectVertices.setVisible(radioButtonLevit.isSelected());
            hBoxSelectVertices.setMinHeight(radioButtonLevit.isSelected() ? HBox.USE_COMPUTED_SIZE : 0);
            CGraph.setCurrentALgorithm(radioButtonLevit.isSelected() ? CGraph.LEVIT : CGraph.KRUSKAL);

            buttonStartAlgorithm.setDisable(false);

            vv.updateUI();
        });

        graphPanel = new SwingNode() {
            @Override
            public boolean isResizable() {
                return false;
            }
        };
        paneGraph.getChildren().add(graphPanel);
        paneGraph.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            graphPanel.resize(newValue.getWidth(), newValue.getHeight());
        });

        choiceBoxVertex1.setItems(CGraph.getVertices());
        choiceBoxVertex1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) ((Vertex) oldValue).setState(Vertex.NORMAL);
            if (newValue != null) ((Vertex) newValue).setState(Vertex.START);
            vv.updateUI();
        });
        choiceBoxVertex2.setItems(CGraph.getVertices());
        choiceBoxVertex2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) ((Vertex) oldValue).setState(Vertex.NORMAL);
            if (newValue != null) ((Vertex) newValue).setState(Vertex.END);
            vv.updateUI();
        });

    }

    BasicVisualizationServer<Vertex, Edge> vv;

    public void buildGraph(ActionEvent actionEvent) {
        if (labelNumberOfVertex.getText().equals("0")) {
            createAlert().showAndWait();
            return;
        }

        buttonStartAlgorithm.setText("Запуск");

        Bounds bounds = paneGraph.getBoundsInParent();

        Graph<Vertex, Edge> graph = CGraph.create(Integer.parseInt(labelNumberOfVertex.getText()));

        Layout<Vertex, Edge> layout = new ISOMLayout<>(graph);
        layout.setSize(new Dimension((int) bounds.getWidth() - 25, (int) bounds.getHeight() - 25));

        vv = new BasicVisualizationServer<>(layout);
        vv.setSize(new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));
        setViewVisualization();

        graphPanel.setContent(vv);

        ObservableList<Vertex> vertices = CGraph.getVertices();

        choiceBoxVertex1.setValue(vertices.get(0));
        choiceBoxVertex2.setValue(vertices.get(vertices.size() - 1));

        vBoxAlgorithms.setDisable(false);
    }

    private Alert createAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ошибка при построении графа");
        alert.setHeaderText(null);
        alert.setContentText("Количество вершин должно быть больше 0");
        return alert;
    }

    private void setViewVisualization() {
        vv.setBackground(new Color(0x2a2a2a));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.setForeground(new Color(0xd1d1d1));
        RenderContext<Vertex, Edge> context = vv.getRenderContext();
        context.setEdgeShapeTransformer(new EdgeShape.Line<>());
//        context.setEdgeLabelTransformer(edge -> {
//            if(edge.getState() != Edge.HIDE) return edge.toString();
//            return "";
//        });
        context.setEdgeDrawPaintTransformer(edge -> {
            switch (edge.getState()) {
                case Edge.HIDE:
                    return new Color(0, true);
                case Edge.PATH:
                    return Color.RED;
            }
            return new Color(0xd1d1d1);
        });
        context.setVertexLabelTransformer(new ToStringLabeller<>());
        context.setVertexShapeTransformer(vertex -> new Ellipse2D.Double(-15, -15, 30, 30));
        context.setVertexDrawPaintTransformer(vertex -> new Color(0xd1d1d1));
        context.setVertexFillPaintTransformer(vertex -> {
            if (radioButtonLevit.isSelected() && (vertex.getState() == Vertex.START || vertex.getState() == Vertex.END)) return new Color(0x5a5a5a);
            return new Color(0x2a2a2a);
        });
    }

    public void startAlgorithm(ActionEvent actionEvent) {
        if (buttonStartAlgorithm.getText().equals("Запуск")) {
            CGraph.startAlgorithm();
            buttonStartAlgorithm.setText("Сброс");
            vBoxAlgorithms.setDisable(true);
        } else {
            CGraph.resetResult();
            buttonStartAlgorithm.setText("Запуск");
            vBoxAlgorithms.setDisable(false);
        }
        vv.updateUI();
    }
}

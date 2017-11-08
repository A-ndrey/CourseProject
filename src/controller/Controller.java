package controller;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
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
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

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
    private ComboBox comboBoxVertex1;

    @FXML
    private ComboBox comboBoxVertex2;

    @FXML
    private VBox vBoxAlgorithms;

    @FXML
    private Button buttonStartAlgorithm;

    private SwingNode graphPanel;

    private VisualizationViewer<Vertex, Edge> vv;

    @FXML
    private void initialize() {
        labelNumberOfVertex.textProperty().bind(Bindings.format("%.0f", sliderNumberOfVertex.valueProperty()));
        toggleGroupAlgorythms.selectedToggleProperty().addListener((ov, old_val, new_val) -> {
            hBoxSelectVertices.setVisible(radioButtonLevit.isSelected());
            hBoxSelectVertices.setMinHeight(radioButtonLevit.isSelected() ? HBox.USE_COMPUTED_SIZE : 0);
            CGraph.setCurrentAlgorithm(radioButtonLevit.isSelected() ? CGraph.LEVIT : CGraph.KRUSKAL);

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

        comboBoxVertex1.setItems(CGraph.getVertices());
        comboBoxVertex1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) ((Vertex) oldValue).setSource(false);
            if (newValue != null) ((Vertex) newValue).setSource(true);
            vv.updateUI();
        });
        comboBoxVertex2.setItems(CGraph.getVertices());
        comboBoxVertex2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) ((Vertex) oldValue).setTarget(false);
            if (newValue != null) ((Vertex) newValue).setTarget(true);
            vv.updateUI();
        });

    }


    public void buildGraph(ActionEvent actionEvent) {
        if (labelNumberOfVertex.getText().equals("0")) {
            createAlert("Ошибка при построении графа",
                    "Количество вершин должно быть больше 0",
                    Alert.AlertType.INFORMATION).showAndWait();
            return;
        }

        buttonStartAlgorithm.setText("Запуск");

        Bounds bounds = paneGraph.getBoundsInParent();

        Graph<Vertex, Edge> graph = CGraph.create(Integer.parseInt(labelNumberOfVertex.getText()));

        Layout<Vertex, Edge> layout = new ISOMLayout<>(graph);
        layout.setSize(new Dimension((int) bounds.getWidth() - 25, (int) bounds.getHeight() - 25));

        vv = new VisualizationViewer<Vertex, Edge>(layout);
        vv.setSize(new Dimension((int) bounds.getWidth(), (int) bounds.getHeight()));
        setGraphVisualization();

        PluggableGraphMouse gm = new PluggableGraphMouse();
        PickingGraphMousePlugin<Vertex, Edge> pgmp = new PickingGraphMousePlugin<>();
        pgmp.setLensColor(new Color(0xd1d1d1));
        gm.add(pgmp);
        gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
        vv.setGraphMouse(gm);

        graphPanel.setContent(vv);

        ObservableList<Vertex> vertices = CGraph.getVertices();

        comboBoxVertex1.setValue(vertices.get(0));
        comboBoxVertex2.setValue(vertices.get(vertices.size() - 1));

        vBoxAlgorithms.setDisable(false);
    }

    private Alert createAlert(String title, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        return alert;
    }

    private void setGraphVisualization() {
        vv.setBackground(new Color(0x2a2a2a));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.setForeground(new Color(0xd1d1d1));
        RenderContext<Vertex, Edge> context = vv.getRenderContext();
        context.setEdgeShapeTransformer(new EdgeShape.Line<>());
        context.setEdgeStrokeTransformer(edge -> new BasicStroke(2));
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
        context.setVertexLabelRenderer(new DefaultVertexLabelRenderer(new Color(0xd1d1d1)));
        context.setVertexLabelTransformer(new ToStringLabeller<>());
        context.setVertexStrokeTransformer(vertex -> new BasicStroke(2));
        context.setVertexShapeTransformer(vertex -> new Ellipse2D.Double(-15, -15, 30, 30));
        context.setVertexDrawPaintTransformer(vertex -> new Color(0xd1d1d1));
        context.setVertexFillPaintTransformer(vertex -> {
            if (radioButtonLevit.isSelected() && (vertex.isSource() || vertex.isTaret())) return new Color(0x5a5a5a);
            return new Color(0x2a2a2a);
        });
    }

    public void startAlgorithm(ActionEvent actionEvent) {
        if (buttonStartAlgorithm.getText().equals("Запуск")) {
            try {
                CGraph.startAlgorithm();
            } catch (IOException e) {
                createAlert("Ошибка записи в файл",
                        "Не удалось записать ифнормацию о ходе работы алгоритма в файл.",
                        Alert.AlertType.ERROR).show();
            }
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

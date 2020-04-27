package ar.nex.pedido;

import ar.nex.entity.equipo.Pedido;
import ar.nex.entity.equipo.Repuesto;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class PedidoTableUtils {

    private static final NumberFormat numberFormatter = NumberFormat.getNumberInstance();
    private static final DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Install the keyboard handler: + CTRL + C = copy to clipboard + CTRL + V =
     * paste to clipboard
     *
     * @param table
     */
    public static void installCopyPasteHandler(TableView<?> table) {
        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());
    }

    /**
     * Copy/Paste keyboard event handler. The handler uses the keyEvent's source
     * for the clipboard data. The source must be of type TableView.
     */
    public static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        KeyCodeCombination pasteKeyCodeCompination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_ANY);

        @Override
        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {
                if (keyEvent.getSource() instanceof TableView) {
                    // copy to clipboard
                    copySelectionToClipboard((TableView<Pedido>) keyEvent.getSource());
                    // event is handled, consume it
                    keyEvent.consume();
                }
            } else if (pasteKeyCodeCompination.match(keyEvent)) {
                if (keyEvent.getSource() instanceof TableView) {
                    // copy to clipboard
                    pasteFromClipboard((TableView<?>) keyEvent.getSource());
                    // event is handled, consume it
                    keyEvent.consume();
                }
            }
        }
    }

    /**
     * Get table selection and copy it to the clipboard.
     *
     * @param table
     */
    public static void copySelectionToClipboard(TableView<Pedido> table) {

        ObservableList<TablePosition> posList = table.getSelectionModel().getSelectedCells();
        int old_r = -1;
        StringBuilder clipboardString = new StringBuilder();
        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();

            Object cell = table.getColumns().get(c).getCellData(r);
            if (cell == null) {
                cell = "";

            } else if (cell instanceof Date) {
                cell = fd.format(cell);

            } else if (cell instanceof Repuesto) {
                cell = ((Repuesto) cell).getCodigo() + '\t' + ((Repuesto) cell).getDescripcion();
            }

            if (old_r == r) {
                clipboardString.append('\t');

            } else if (old_r != -1) {
                clipboardString.append('\n');
            }
            System.out.println("ar.nex.pedido.PedidoTableUtils.copySelectionToClipboard(): " + cell.getClass());
            clipboardString.append(cell);
            old_r = r;
        }
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     *
     * @param table
     */
    public static void pasteFromClipboard(TableView<?> table) {

        // abort if there's not cell selected to start with
        if (table.getSelectionModel().getSelectedCells().size() == 0) {
            return;
        }

        // get the cell position to start with
        TablePosition pasteCellPosition = table.getSelectionModel().getSelectedCells().get(0);

        System.out.println("Pasting into cell " + pasteCellPosition);

        String pasteString = Clipboard.getSystemClipboard().getString();

        System.out.println(pasteString);

        int rowClipboard = -1;

        StringTokenizer rowTokenizer = new StringTokenizer(pasteString, "\n");
        while (rowTokenizer.hasMoreTokens()) {

            rowClipboard++;

            String rowString = rowTokenizer.nextToken();

            StringTokenizer columnTokenizer = new StringTokenizer(rowString, "\t");

            int colClipboard = -1;

            while (columnTokenizer.hasMoreTokens()) {

                colClipboard++;

                // get next cell data from clipboard
                String clipboardCellContent = columnTokenizer.nextToken();

                // calculate the position in the table cell
                int rowTable = pasteCellPosition.getRow() + rowClipboard;
                int colTable = pasteCellPosition.getColumn() + colClipboard;

                // skip if we reached the end of the table
                if (rowTable >= table.getItems().size()) {
                    continue;
                }
                if (colTable >= table.getColumns().size()) {
                    continue;
                }

                // System.out.println( rowClipboard + "/" + colClipboard + ": " + cell);
                // get cell
                TableColumn tableColumn = table.getColumns().get(colTable);
                ObservableValue observableValue = tableColumn.getCellObservableValue(rowTable);

                System.out.println(rowTable + "/" + colTable + ": " + observableValue);

                // TODO: handle boolean, etc
                if (observableValue instanceof DoubleProperty) {
                    try {
                        double value = numberFormatter.parse(clipboardCellContent).doubleValue();
                        ((DoubleProperty) observableValue).set(value);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else if (observableValue instanceof IntegerProperty) {
                    try {

                        int value = NumberFormat.getInstance().parse(clipboardCellContent).intValue();
                        ((IntegerProperty) observableValue).set(value);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else if (observableValue instanceof StringProperty) {

                    ((StringProperty) observableValue).set(clipboardCellContent);

                } else {

                    System.out.println("Unsupported observable value: " + observableValue);

                }

                System.out.println(rowTable + "/" + colTable);
            }

        }

    }

}

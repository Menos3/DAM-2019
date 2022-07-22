package dam2.controls.limitedtextfield;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;

import java.util.Objects;

public class LimitedTextField extends TextField {

    private final IntegerProperty maxLength;

    public LimitedTextField() {
        super();
        this.maxLength = new SimpleIntegerProperty(-1);
    }

    public IntegerProperty maxLengthProperty() {
        return this.maxLength;
    }

    public final Integer getMaxLength() {
        return this.maxLength.getValue();
    }

    public final void setMaxLength(Integer maxLength) {
        Objects.requireNonNull(maxLength, "Max length no pot ser null, -1 indica sense límit");
        this.maxLength.setValue(maxLength);
    }

    @Override
    public void replaceText(int start, int end, String insertedText) {
        if (this.getMaxLength() <= 0) {
            // Comportament per defecte, en cas que max length no estigui posat
            super.replaceText(start, end, insertedText);
        }
        else {
            // Obtenir el que conté el TextField abans que l'usuaru introdueixi alguna cosa
            String currentText = this.getText() == null ? "" : this.getText();

            // Calcular el text que hauria d'estar al textfield ara mateix
            String finalText = currentText.substring(0, start) + insertedText + currentText.substring(end);

            // Si max length no és superat
            int numberOfexceedingCharacters = finalText.length() - this.getMaxLength();
            if (numberOfexceedingCharacters <= 0) {
                // Comportament normal
                super.replaceText(start, end, insertedText);
            }
            else {
                // En altre cas, tallem el text que ha de ser inserit
                String cutInsertedText = insertedText.substring(
                        0,
                        insertedText.length() - numberOfexceedingCharacters
                );

                // I el sobreescrivim al textfield
                super.replaceText(start, end, cutInsertedText);
            }
        }
    }
}
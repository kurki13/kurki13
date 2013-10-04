/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.util;

/**
 *
 * @author mkctammi
 */
public interface Table {

    public String getTableName();

    public Column[] getColumns();

    public <T extends Table> T getNewInstance();

    public <T> T get(Column sarake, Class<T> type);

    public void set(Column sarake, Object value);

    public static class Helper {
        public static void checkGetType(Table caller, Column sarake, Class providedType) {
            if (sarake.getType() != providedType) {
                throw new IllegalArgumentException("Taulun " + caller.getTableName()
                        + " Sarakkeen " + sarake.getColumnName()
                        + " Tyypiksi on määritelty " + sarake.getType().getCanonicalName()
                        + ". Argumenttina saatiin " + providedType.getCanonicalName());
            }
        }
        public static void checkSetType(Table caller, Column sarake, Object providedValue) {
            if (providedValue == null)
                return;
            if (sarake.getType() != providedValue.getClass()) {
                throw new IllegalArgumentException("Taulun " + caller.getTableName()
                        + " Sarakkeen " + sarake.getColumnName()
                        + " Tyypiksi on määritelty " + sarake.getType().getCanonicalName()
                        + ". Argumenttina saatiin muuttuja, jonka tyyppi oli" + 
                        providedValue.getClass().getCanonicalName());
            }
        }
    }
}

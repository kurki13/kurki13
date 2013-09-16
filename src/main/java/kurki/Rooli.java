package kurki;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mkctammi
 */
public enum Rooli {
        OUTSIDER(1),
        TUTOR(2),
        PRIVILEGED(3),
        SUPER(4);
        public final int KOODI;
        public final int NO_OF_ROLES = 4;
        private Rooli(int koodi) {
            this.KOODI = koodi;
        }
}

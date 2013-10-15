/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debug.model.osasuoritukset;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mkctammi
 */
public class OsasuoritusIterator implements Iterator<Osasuoritus> {

    int index = 0;
    int aktiivisia;
    List<Osasuoritus> osasuoritukset;

    public OsasuoritusIterator(List<Osasuoritus> osasuoritukset, int koko) {
        this.osasuoritukset = osasuoritukset;
        this.aktiivisia = koko;
    }

    @Override
    public boolean hasNext() {
        //Ei anneta käyttäjälle kuin aktiivinen osa laskareista käsiteltäväksi
        return index < aktiivisia;
    }

    @Override
    public Osasuoritus next() {
        index++;
        return osasuoritukset.get(index - 1);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Osasuorituksia ei voi poistaa, vain muuttaa tyhjiksi");
    }
}

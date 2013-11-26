package model.osasuoritus_rajat;

import java.util.Iterator;
import java.util.List;

public class OsasuoritusRajaIterator implements Iterator<OsasuoritusRaja> {

    private int index = 0;
    private int aktiivisia;
    public List<OsasuoritusRaja> osasuoritusrajat;

    @Override
    public boolean hasNext() {
        return index < aktiivisia;
    }

    @Override
    public OsasuoritusRaja next() {
        index++;
        return osasuoritusrajat.get(index-1);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("OsasuoritusRajoja ei voi poistaa");
    }

    public OsasuoritusRajaIterator(List<OsasuoritusRaja> osasuoritusrajat,int aktiivisia) {
        this.aktiivisia = aktiivisia;
        this.osasuoritusrajat = osasuoritusrajat;
    }
    
}
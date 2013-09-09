create or replace function jaadytys (kkdi char, lvuosi integer,
    lkausi char, knro integer, ktyyppi char, 
    rnro integer) return char is

  koodi integer;
  cursor ck is
   select * from kurssi
   where kurssikoodi=kkdi and lukuvuosi=lvuosi and
      lukukausi=lkausi and knro=kurssi_nro and 
      tyyppi=ktyyppi and
      suoritus_pvm is not null 
 -- tila <>'J'
   for update of tila, siirto_pvm;

  cursor co1 is 
   select o.hetu hetu, sukunimi, etunimi,
      ilmo_jnro, ryhma_nro,
      LASKARISUORITUKSET, HARJOITUSTYOPISTEET, KOEPISTEET ,
      LASKARI_LASNAOLO_LKM, LASKARISUORITUKSET_SUMMA, LASKARIHYVITYS,
      HARJOITUSTYO_LASNAOLO_LKM, HARJOITUSTYO_SUMMA, HARJOITUSTYOHYVITYS,
      KOEPISTEET_SUMMA, ARVOSANA,
      kommentti_1, kommentti_2,jaassa,
      viimeinen_kasittely_pvm, opnro
   from opiskelija o, osallistuminen s
   where kurssikoodi=kkdi and lukuvuosi=lvuosi and
         lukukausi=lkausi and knro=kurssi_nro and
         tyyppi=ktyyppi and o.hetu=s.hetu and
         voimassa='K' and 
         (jaassa is null or jaassa<>'J')
   for update of jaassa;
 
  recK    ck%rowtype;
  recOp   co1%rowtype;

begin

  koodi:= (lvuosi-2000)*100000;
  if lkausi='V' then 
     koodi:= koodi+20000;
  elsif lkausi='S' then
     koodi:= koodi+ 40000;
  end if;
  koodi:= koodi+ knro*1000;
  if ktyyppi='L' then 
     koodi:=koodi+500;
  end if;
  koodi:= koodi+ rnro;

  open ck;
  fetch ck into recK;
  if ck%notfound then
    close ck;
    return 'KURSSIA EI L÷YDY';
  end if;
  

if recK.tila<>'J' then  
  -- ei ole viel‰ j‰‰dytetty 
    insert into tk_siirto.valmis_ku( kurssiavain, kkoodi,
      LUKUKAUSI, LUKUVUOSI,  TYYPPI, KURSSI_NRO, KIELIKOODI,
      KNIMI, KOPVIIKOT, VASTUUHLO, 
      LHLKM, HTLKM, ARLKM,  
      LHMIN,  LHMAX, 
      ARMIN, ARMAX, 
      HTMIN, HTMAX, 
      LHLPMAX, LPALARAJA, 
      ASKELKOKO, LHPAK,
      LPRAJAT, LHHYVRAJA, 
      HTLPMAX, HTLPALARAJA,
      HTLPASKEL, TMINLKM, 
      HTLPRAJAT, HTHYVRAJA,
      KMINLKM, KOEMIN, 
      YHTMIN, ARVASKEL, 
      ARVRAJAT,
      ARVOSTELLAANKO, 
      SUORPVM, ARVPVM, 
      SIIRTOPVM, LASTDATE,
      laskentakaava) 
   values (koodi,kkdi, lkausi, lvuosi, ktyyppi,knro, recK.kielikoodi,
      recK.nimi, reck.opintoviikot, recK.omistaja, 
      reck.laskarikerta_lkm, reck.harjoitustyo_lkm, reck.valikokeet_lkm, 
      reck.HYVAKSYTTY_LASKARILASNAOLO, reck.laskaritehtava_lkm,
      reck.MIN_KOEPISTEET, reck.MAX_KOEPISTEET,
      reck.MIN_HARJOITUSTYOPISTEET, reck.MAX_HARJOITUSTYOPISTEET,
      reck.MAX_LASKARIPISTEET, reck.LISAPISTEALARAJA,
      reck.LISAPISTEIDEN_ASKELKOKO, reck.PAKOLLISET_LASKARIKERTA_LKM, 
      reck.LISAPISTERAJAT, reck.PAKOLLISET_LASKARITEHTAVA_LKM,
      reck.HARJOITUSTYOPISTEET, reck.HT_LISAPISTEALARAJA,
      reck.HARJOITUSTOIDEN_ASKELKOKO, reck.PAKOLLISET_HARJOITUSTYO_LKM,
      reck.HARJOITUSTYON_PISTERAJAT, reck.MIN_HARJOITUSTYOPISTEET_SUMMA,
      reck.PAKOLLISET_KOE_LKM, reck.MIN_KOEPISTEET_SUMMA, 
      reck.MIN_YHTEISPISTEET, reck.ARVOSTELUN_ASKELKOKO,
      reck.ARVOSANARAJAT, reck.ARVOSTELLAANKO, 
      reck.SUORITUS_PVM, reck.ARVOSTELU_PVM, 
      NULL, SYSDATE, reck.laskentakaava);
else
-- on jo j‰‰ss‰
      update tk_siirto.valmis_ku
      set lastdate=sysdate,
          arvpvm=recK.arvostelu_pvm
      where kkoodi=kkdi and
       lukukausi=lkausi and
       lukuvuosi=lvuosi and
       tyyppi=ktyyppi and
       kurssi_nro=knro;
end if;


  update kurssi
    set siirto_pvm=sysdate,
       TILA='J'
    where current of ck;


  for op in co1 loop
  if op.jaassa is null then
    -- ei aiemmin j‰‰dytetty
    insert into tk_siirto.valmis_os (
     HETU,  KURSSIVIITE,
     KKOODI, LUKUKAUSI,  LUKUVUOSI,
     TYYPPI,
     KURSSI_NRO, LHRYHMA, 
     LHPIST , HTSUOR,ARV, 
     LHLKM, LHSUMMA, LHHYVITYS,
     HTLKM, HTSUMMA, HTHYVITYS,
     ARLKM, ARSUMMA, ARVOSANA,
     ETUNIMI, SUKUNIMI,
     KOMMENTTI,LASTDATE, OPNRO)
   values (op.hetu, koodi, 
     kkdi, lkausi, lvuosi,
     ktyyppi, 
     knro, op.ilmo_jnro, 
     op.LASKARISUORITUKSET, op.HARJOITUSTYOPISTEET, op.KOEPISTEET , 
     op.LASKARI_LASNAOLO_LKM, op.LASKARISUORITUKSET_SUMMA, op.LASKARIHYVITYS,
     op.HARJOITUSTYO_LASNAOLO_LKM, op.HARJOITUSTYO_SUMMA, op.HARJOITUSTYOHYVITYS,
     null, op.KOEPISTEET_SUMMA, op.ARVOSANA,                               
     op.etunimi,op.sukunimi,
     op.kommentti_1||'*2*'||op.kommentti_2,
     op.viimeinen_kasittely_pvm, op.opnro
     );
  else
   -- on sulatettu
   update tk_siirto.valmis_os
     set LHPIST=    op.LASKARISUORITUKSET,
         HTSUOR=    op.HARJOITUSTYOPISTEET,
         ARV=       op.KOEPISTEET ,
         LHLKM=     op.LASKARI_LASNAOLO_LKM, 
         LHSUMMA=   op.LASKARISUORITUKSET_SUMMA, 
         LHHYVITYS= op.LASKARIHYVITYS,
         HTLKM =    op.HARJOITUSTYO_LASNAOLO_LKM, 
         HTSUMMA=   op.HARJOITUSTYO_SUMMA,
         HTHYVITYS= op.HARJOITUSTYOHYVITYS,
         ARSUMMA=   op.KOEPISTEET_SUMMA, 
         ARVOSANA=  op.ARVOSANA,
         KOMMENTTI= KOMMENTTI||' korj. '|| to_char(sysdate) 
    where hetu=op.hetu and kkoodi=kkdi and kurssiviite=koodi;
  end if;
  -- p‰ivitet‰‰n osallistuminen
   update osallistuminen
     set jaassa='J' 
     where kurssikoodi= kkdi and
     lukukausi=lkausi and lukuvuosi=lvuosi and
     kurssi_nro=knro and tyyppi=ktyyppi and
     hetu=op.hetu and ryhma_nro=op.ryhma_nro and
     voimassa='K';

  end loop;
  commit;
  return 'OK';

exception
  when others then
    rollback;
    return sqlerrm;
  end;
/
show errors
exit


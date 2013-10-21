CREATE TABLE OPISKELIJA
(
 PERSONID                        VARCHAR(11)  -- hetu, voi puuttua
,ETUNIMI                         VARCHAR(25) NOT NULL
,SUKUNIMI                        VARCHAR(40) NOT NULL
,ENTINEN_SUKUNIMI                VARCHAR(80)
,OSOITE                          VARCHAR(50)
,PUHELIN                         VARCHAR(40)
,SAHKOPOSTIOSOITE                VARCHAR(50)
,PAA_AINE                        VARCHAR(3)
,ALOITUSVUOSI                    INT
,KAYTTO_PVM                      DATE
,OPNRO                           VARCHAR(15) -- opiskelijanumero, ei käytössä
,LUPA                            VARCHAR(12) 
,VINKKI                          VARCHAR(40) -- ei käytössä
,VARMENNE                        VARCHAR(20) -- ei käytössä
,HETU                            VARCHAR(9) PRIMARY KEY  -- opiskelijanumero
);

CREATE TABLE KURSSI(
 KURSSIKOODI                     VARCHAR(15) NOT NULL
,LUKUKAUSI                       VARCHAR(1) NOT NULL
,LUKUVUOSI                       INT NOT NULL
,TYYPPI                          VARCHAR(1) NOT NULL
,KURSSI_NRO                      INT NOT NULL
,KIELIKOODI                      VARCHAR(1) NOT NULL
,NIMI                            VARCHAR(80) NOT NULL
,OPINTOVIIKOT                    INT NOT NULL
,LUENTOTUNNIT                    INT
,LUENTOKERTA_LKM                 INT
,HARJOITUSTUNNIT                 INT
,LASKARIKERTA_LKM                INT
,LYHENNE                         VARCHAR(12)  -- ei käytössä
,SALASANA                        VARCHAR(30)  -- ei käytössä
,SUORITUS_PVM                    DATE
,TILA                            VARCHAR(1)
,ALKAMIS_PVM                     DATE
,PAATTYMIS_PVM                   DATE
,PAIVITYS_PVM                    DATE
,MAX_OSALLISTUJA_LKM             INT
,LASKARITEHTAVA_LKM              VARCHAR(54) -- piilotaulukko[18]: 99,99,.....,99
,PAKOLLISET_LASKARIKERTA_LKM     INT
,PAKOLLISET_LASKARITEHTAVA_LKM   INT
,MAX_LASKARIPISTEET              INT
,HYVAKSYTTY_LASKARILASNAOLO      VARCHAR(54)
,LISAPISTEALARAJA                INT
,LISAPISTERAJAT                  VARCHAR(240) -- piilotaulukko[60]: 999,999,... 
,LISAPISTEIDEN_ASKELKOKO         INT
,HARJOITUSTYO_LKM                INT
,PAKOLLISET_HARJOITUSTYO_LKM     INT
,HARJOITUSTYOPISTEET             INT
,MAX_HARJOITUSTYOPISTEET         VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,MIN_HARJOITUSTYOPISTEET         VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,MIN_HARJOITUSTYOPISTEET_SUMMA   INT
,HARJOITUSTYON_PISTERAJAT        VARCHAR(240)  -- piilotaulukko[60]: 999,999,... 
,HARJOITUSTOIDEN_ASKELKOKO       INT
,VALIKOKEET_LKM                  INT
,PAKOLLISET_KOE_LKM              INT
,MAX_KOEPISTEET                  VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,MIN_KOEPISTEET                  VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,MIN_KOEPISTEET_SUMMA            INT
,MIN_YHTEISPISTEET               INT
,ARVOSTELUN_ASKELKOKO            INT
,ARVOSANARAJAT                   VARCHAR(80)  -- piilotaulukko[5]: 999,999,... 
,ARVOSTELLAANKO                  VARCHAR(1)
,KOKONAISTIEDOT                  VARCHAR(200)
,KUVAUSTIETO1                    VARCHAR(800)
,KUVAUSTIETO2                    VARCHAR(800)
,KUVAUSTIETO3                    VARCHAR(800)
,HAKUKYSYMYKSET                  VARCHAR(1)
,SUUNNITTELUKOMMENTTI            VARCHAR(240)
,OMISTAJA                        VARCHAR(30)
,PERUTTAVISSA                    CHAR
,LASKENTAKAAVA                   INT
,ARVOSTELU_PVM                   DATE
,SIIRTO_PVM                      DATE
,HT_LISAPISTEALARAJA             INT
,ARVOSTELIJA                     VARCHAR(30)
,OPINTOVIIKOT_YLARAJA            INT
,OPINTOPISTEET_YLARAJA           INT
,OPINTOPISTEET                   INT
,PERIODI                         INT
,KOTISIVU                        VARCHAR(120)
,PERIODI2                        INT
,PRIMARY KEY (KURSSIKOODI,LUKUKAUSI,LUKUVUOSI,TYYPPI,KURSSI_NRO)
);

CREATE TABLE OSALLISTUMINEN(
 PERSONID                        VARCHAR(11) -- hetu, voi puuttua
,KURSSIKOODI                     VARCHAR(15) NOT NULL
,LUKUKAUSI                       VARCHAR(1) NOT NULL
,LUKUVUOSI                       INT NOT NULL
,TYYPPI                          VARCHAR(1) NOT NULL
,KURSSI_NRO                      INT NOT NULL
,RYHMA_NRO                       INT NOT NULL
,KOMMENTTI_1                     VARCHAR(240)
,KOMMENTTI_2                     VARCHAR(240)
,LASKARI_LASNAOLO_LKM            INT
,LASKARISUORITUKSET              VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,LASKARISUORITUKSET_SUMMA        INT
,LASKARIHYVITYS                  INT
,HARJOITUSTYO_LASNAOLO_LKM       INT
,HARJOITUSTYOPISTEET             VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,HARJOITUSTYO_SUMMA              INT
,HARJOITUSTYOHYVITYS             INT
,KOEPISTEET                      VARCHAR(54)  -- piilotaulukko[18]: 99,99,.....,99
,KOEPISTEET_SUMMA                INT
,YHTEISPISTEET                   INT
,ARVOSANA                        VARCHAR(2)
,ILMOITTAUTUMIS_PVM              DATE
,VOIMASSA                        VARCHAR(1)
,VIIMEINEN_KASITTELY_PVM         DATE
,ILMO_JNRO                       INT
,JAASSA                          CHAR(1)
,LAAJUUS_OV                      INT
,LAAJUUS_OP                      INT
,HETU                            VARCHAR(9) NOT NULL PRIMARY KEY
,KYPSYYS_PVM                     DATE    -- jotain?
,TENTTIJA                        VARCHAR(200) -- kommentteja
,KIELIKOODI                      VARCHAR(2) -- suorituksen kieli
); 

CREATE TABLE OPETUS(
 KURSSIKOODI                     VARCHAR(15) NOT NULL
,LUKUKAUSI                       VARCHAR(1) NOT NULL
,LUKUVUOSI                       INT NOT NULL
,TYYPPI                          VARCHAR(1) NOT NULL
,KURSSI_NRO                      INT NOT NULL
,RYHMA_NRO                       INT NOT NULL
,ILMO_JNRO                       INT
,ILMO                            VARCHAR(1)
,OPETUSTEHTAVA                   VARCHAR(3)
,ALKAMISAIKA                     DATE
,PAATTYMISAIKA                   DATE
,ALKAMIS_PVM                     DATE
,PAATTYMIS_PVM                   DATE
,MAX_OSALLISTUJA_LKM             INT
,ILMOITTAUTUNEIDEN_LKM           INT
,KUVAUSTIETO                     VARCHAR(70)
,PRIMARY KEY (KURSSIKOODI,LUKUKAUSI,LUKUVUOSI,TYYPPI,KURSSI_NRO,RYHMA_NRO)
);
 
 
 CREATE TABLE HENKILO(
 HTUNNUS                         VARCHAR(12) NOT NULL
,ETUNIMET                        VARCHAR(80) NOT NULL
,SUKUNIMI                        VARCHAR(80) NOT NULL
,KUTSUMANIMI                     VARCHAR(15)
,AKTIIVISUUS                     VARCHAR(1)
,HUONE_NRO                       VARCHAR(5)
,HETU                            VARCHAR(11)
,OPPIARVO                        VARCHAR(5)
,TITTELI                         VARCHAR(80)
,PUHELIN_TYO                     VARCHAR(20)
,PUHELIN_KOTI                    VARCHAR(20)
,KATUOSOITE                      VARCHAR(40)
,POSTINRO                        VARCHAR(5)
,POSTITOIMIPAIKKA                VARCHAR(20)
,VALVONTASALDO                   INT
,SAHKOPOSTIOSOITE                VARCHAR(80)
,HALLINNOLLINEN_KOMMENTTI        VARCHAR(80)
,OPISKELIJA_KOMMENTTI            VARCHAR(80)
,KTUNNUS                         VARCHAR(12)
,PRIMARY KEY (HTUNNUS)
);



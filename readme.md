# Backend a FoxComm leak aktiválási folyamatához
## Tudnivalók
Ez a program demonstrációs célból készült, a FoxComm aktiválásához szükséges backend logikát tartalmazza. A cél az volt, hogy az eredeti, érintetlen foxengine.exe fájllal működjön, ezért van szükség a hosts fájl manipulálására és a fake tanúsítvány telepítésére.

Alapértelmezett adatok az aktiváló ablak kitöltéséhez:
- **Vásárlói azonosító:** ```user```
- **Termékkulcs:** ```serial```
- **Azonosító token:** ```token```

Amennyiben egyéni licensz adatokkal szeretnél aktiválni, ezek hozzáadására itt van lehetőség: https://localhost/swagger-ui/index.html#/licensing-controller/addLicense

Ezután a Fox újraindul, és már a "preloader" fog fogadni, amely folyamat végén hibát fog kiírni, mert a fájl, amit keres, sajnos nem került ki.

Ha végigmentél a procedúrán, de szeretnéd újra átélni az aktiválás élményét, a következő lehetőségek állnak rendelkezésre:

- a) A Fox főablakában a Crtl + Shift + F8 billentyűkombinációval töröld a licensz adatokat.
- b) Indítsd újra a backendet (Útmutató 3. pont). A leállítással minden felvitt adat elvész!

## Útmutató
#### 1. A ```C:\Windows\System32\drivers\etc\hosts``` fájlhoz add hozzá a következő sort:
```
127.0.0.1 dimitris.masbate.hu
```
#### 2. Telepítsd a ```hsp.cer``` tanúsítványfájlt.
#### 3. Indítsd el a backendet:
```
./mvnw spring-boot:run
```

#### 4. Indítsd el az eredeti ```foxengine.exe``` programot. Jó szórakozást!
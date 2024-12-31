# Mock backend a FoxComm leak aktiválási folyamathoz
## Tudnivalók
Ez a program demonstrációs célból készült, a FoxComm sikeres aktiválásához szükséges minimális backend kódot tartalmazza. A cél az volt, hogy az eredeti, érintetlen foxengine.exe fájllal működjön, ezért van szükség a hosts fájl manipulálására és a fake tanúsítvány telepítésére.

Az aktiváló ablak kitöltése:
- **Vásárlói azonosító:** ```user```
- **Termékkulcs:** *tetszőleges, de nem üres*
- **Azonosító token:** *tetszőleges, de nem üres*

Ezután a Fox újraindul, és már a "preloader" fog fogadni, amely folyamat végén hibát fog kiírni, mert a fájl, amit keres, sajnos nem került ki.

Ha végigmentél a folyamaton, de szeretnéd újra átélni az aktiválás élményét, a backendet újra kell indítani (Útmutató 3. pont).
## Útmutató
#### 1. A ```C:\Windows\System32\drivers\etc\hosts``` fájlhoz add hozzá a következő sort:
```
127.0.0.1 dimitris.masbate.hu
```
#### 2. Telepítsd a ```hsp.cer``` tanúsítványfájlt
#### 3. Indítsd el a backendet:
```
./mvnw spring-boot:run
```

#### 4. Indítsd el az eredeti ```foxengine.exe``` programot. Jó szórakozást!
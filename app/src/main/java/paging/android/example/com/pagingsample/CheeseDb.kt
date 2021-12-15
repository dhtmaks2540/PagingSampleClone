/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package paging.android.example.com.pagingsample

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.*
import android.content.Context

/**
 * Singleton database object. Note that for a real app, you should probably use a Dependency
 * Injection framework or Service Locator to create the singleton database.
 */

/**
 * 데이터 베이스 객체는 싱글턴으로 존재. 실제 앱에서는 Dependency Injection(DI) 프레임워크를 사용하던가
 * Service Locator를 사용하여 싱글턴 데이터베이스를 사용해야한다.
 */


// DAO를 반환하는 클래스
// Entity와 버전을 지정
@Database(entities = [Cheese::class], version = 1)
abstract class CheeseDb : RoomDatabase() {
    // Dao를 반환하는 메소드
    abstract fun cheeseDao(): CheeseDao

    // companion object를 사용하여 static으로 생성
    companion object {
        private var instance: CheeseDb? = null

        @Synchronized
        // 이 메서드는 멀티 스레드에 의한 동시성으로 실행되는 것으로부터 인스턴스의 감시로 인해 보호받습니다.
        fun get(context: Context): CheeseDb {
            if (instance == null) {
                // Database 객체 생성
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CheeseDb::class.java, "CheeseDatabase"
                )
                    // 이 데이터베이스에 RoomDatabase.Callback을 추가
                    .addCallback(object : RoomDatabase.Callback() { // RoomDatabase를 위한 콜백
                        // 데이터베이스가 처음으로 만들어졌을 때 호출되는데, 모든 테이블이 만들어진 후 호출됩니다.
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            fillInDb(context.applicationContext)
                        }
                    }).build()
            }

            return instance!!
        }

        /**
         * 치즈 리스트로 데이터베이스를 채웁니다
         */
        private fun fillInDb(context: Context) {
            // inserts in Room are executed on the current thread, so we insert in the background
            // 현재의 스레드에서 룸으로 inserts를 실행합니다. 따라서 백그라운드에서 insert를 진행
            ioThread {
                get(context).cheeseDao().insert(
                    // autoIncrement의 속성으로 0값을 주면 자동으로 증가하는 시스템
                    // map 함수를 사용해 컬렉션 객체에 람다식 적용
                    // 새로운 리스트가 만들어져서 insert을 통해 데이터베이스 삽입
                    CHEESE_DATA.map { Cheese(id = 0, name = it) })
            }
        }
    }
}

// String List
private val CHEESE_DATA = arrayListOf(
    "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
    "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
    "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
    "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
    "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
    "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
    "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
    "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
    "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
    "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
    "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
    "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
    "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",
    "Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon", "Boule Du Roves",
    "Boulette d'Avesnes", "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",
    "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
    "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",
    "Brin", "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",
    "Briquette de Brebis", "Briquette du Forez", "Broccio", "Broccio Demi-Affine",
    "Brousse du Rove", "Bruder Basil", "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
    "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase", "Button (Innes)",
    "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta",
    "Caerphilly", "Cairnsmore", "Calenzana", "Cambazola", "Camembert de Normandie",
    "Canadian Cheddar", "Canestrato", "Cantal", "Caprice des Dieux", "Capricorn Goat",
    "Capriole Banon", "Carre de l'Est", "Casciotta di Urbino", "Cashel Blue", "Castellano",
    "Castelleno", "Castelmagno", "Castelo Branco", "Castigliano", "Cathelain",
    "Celtic Promise", "Cendre d'Olivet", "Cerney", "Chabichou", "Chabichou du Poitou",
    "Chabis de Gatine", "Chaource", "Charolais", "Chaumes", "Cheddar",
    "Cheddar Clothbound", "Cheshire", "Chevres", "Chevrotin des Aravis", "Chontaleno",
    "Civray", "Coeur de Camembert au Calvados", "Coeur de Chevre", "Colby", "Cold Pack",
    "Comte", "Coolea", "Cooleney", "Coquetdale", "Corleggy", "Cornish Pepper",
    "Cotherstone", "Cotija", "Cottage Cheese", "Cottage Cheese (Australian)",
    "Cougar Gold", "Coulommiers", "Coverdale", "Crayeux de Roncq", "Cream Cheese",
    "Cream Havarti", "Crema Agria", "Crema Mexicana", "Creme Fraiche", "Crescenza",
    "Croghan", "Crottin de Chavignol", "Crottin du Chavignol", "Crowdie", "Crowley",
    "Cuajada", "Curd", "Cure Nantais", "Curworthy", "Cwmtawe Pecorino",
    "Cypress Grove Chevre", "Danablu (Danish Blue)", "Danbo", "Danish Fontina",
    "Daralagjazsky", "Dauphin", "Delice des Fiouves", "Denhany Dorset Drum", "Derby",
    "Dessertnyj Belyj", "Devon Blue", "Devon Garland", "Dolcelatte", "Doolin",
    "Doppelrhamstufel", "Dorset Blue Vinney", "Double Gloucester", "Double Worcester",
    "Dreux a la Feuille", "Dry Jack", "Duddleswell", "Dunbarra", "Dunlop", "Dunsyre Blue",
    "Duroblando", "Durrus", "Dutch Mimolette (Commissiekaas)", "Edam", "Edelpilz",
    "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne", "Esbareich",
    "Esrom", "Etorki", "Evansdale Farmhouse Brie", "Evora De L'Alentejo", "Exmoor Blue",
    "Explorateur", "Feta", "Feta (Australian)", "Figue", "Filetta", "Fin-de-Siecle",
    "Finlandia Swiss", "Finn", "Fiore Sardo", "Fleur du Maquis", "Flor de Guia",
    "Flower Marie", "Folded", "Folded cheese with mint", "Fondant de Brebis",
    "Fontainebleau", "Fontal", "Fontina Val d'Aosta", "Formaggio di capra", "Fougerus",
    "Four Herb Gouda", "Fourme d' Ambert", "Fourme de Haute Loire", "Fourme de Montbrison",
    "Fresh Jack", "Fresh Mozzarella", "Fresh Ricotta", "Fresh Truffles", "Fribourgeois",
    "Friesekaas", "Friesian", "Friesla", "Frinault", "Fromage a Raclette", "Fromage Corse",
    "Fromage de Montagne de Savoie", "Fromage Frais", "Fruit Cream Cheese",
    "Frying Cheese", "Fynbo", "Gabriel", "Galette du Paludier", "Galette Lyonnaise",
    "Galloway Goat's Milk Gems", "Gammelost", "Gaperon a l'Ail", "Garrotxa", "Gastanberra",
    "Geitost", "Gippsland Blue", "Gjetost", "Gloucester", "Golden Cross", "Gorgonzola",
    "Gornyaltajski", "Gospel Green", "Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",
    "Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",
    "Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh", "Greve",
    "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny", "Halloumi",
    "Halloumy (Australian)", "Haloumi-Style Cheese", "Harbourne Blue", "Havarti",
    "Heidi Gruyere", "Hereford Hop", "Herrgardsost", "Herriot Farmhouse", "Herve",
    "Hipi Iti", "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
    "Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu", "Isle of Mull", "Jarlsberg",
    "Jermi Tortes", "Jibneh Arabieh", "Jindi Brie", "Jubilee Blue", "Juustoleipa",
    "Kadchgall", "Kaseri", "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
    "Kikorangi", "King Island Cape Wickham Brie", "King River Gold", "Klosterkaese",
    "Knockalara", "Kugelkase", "L'Aveyronnais", "L'Ecir de l'Aubrac", "La Taupiniere",
    "La Vache Qui Rit", "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",
    "Langres", "Lappi", "Laruns", "Lavistown", "Le Brin", "Le Fium Orbo", "Le Lacandou",
    "Le Roule", "Leafield", "Lebbene", "Leerdammer", "Leicester", "Leyden", "Limburger",
    "Lincolnshire Poacher", "Lingot Saint Bousquet d'Orb", "Liptauer", "Little Rydings",
    "Livarot", "Llanboidy", "Llanglofan Farmhouse", "Loch Arthur Farmhouse",
    "Loddiswell Avondale", "Longhorn", "Lou Palou", "Lou Pevre", "Lyonnais", "Maasdam",
    "Macconais", "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",
    "Manouri", "Manur", "Marble Cheddar", "Marbled Cheeses", "Maredsous", "Margotin",
    "Maribo", "Maroilles", "Mascares", "Mascarpone", "Mascarpone (Australian)",
    "Mascarpone Torta", "Matocq", "Maytag Blue", "Meira", "Menallack Farmhouse",
    "Menonita", "Meredith Blue", "Mesost", "Metton (Cancoillotte)", "Meyer Vintage Gouda",
    "Mihalic Peynir", "Milleens", "Mimolette", "Mine-Gabhar", "Mini Baby Bells", "Mixte",
    "Molbo", "Monastery Cheeses", "Mondseer", "Mont D'or Lyonnais", "Montasio",
    "Monterey Jack", "Monterey Jack Dry", "Morbier", "Morbier Cru de Montagne",
    "Mothais a la Feuille", "Mozzarella", "Mozzarella (Australian)",
    "Mozzarella di Bufala", "Mozzarella Fresh, in water", "Mozzarella Rolls", "Munster",
    "Murol", "Mycella", "Myzithra", "Naboulsi", "Nantais", "Neufchatel",
    "Neufchatel (Australian)", "Niolo", "Nokkelost", "Northumberland", "Oaxaca",
    "Olde York", "Olivet au Foin", "Olivet Bleu", "Olivet Cendre",
    "Orkney Extra Mature Cheddar", "Orla", "Oschtjepka", "Ossau Fermier", "Ossau-Iraty",
    "Oszczypek", "Oxford Blue", "P'tit Berrichon", "Palet de Babligny", "Paneer", "Panela",
    "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)", "Parmigiano Reggiano",
    "Pas de l'Escalette", "Passendale", "Pasteurized Processed", "Pate de Fromage",
    "Patefine Fort", "Pave d'Affinois", "Pave d'Auge", "Pave de Chirac", "Pave du Berry",
    "Pecorino", "Pecorino in Walnut Leaves", "Pecorino Romano", "Peekskill Pyramid",
    "Pelardon des Cevennes", "Pelardon des Corbieres", "Penamellera", "Penbryn",
    "Pencarreg", "Perail de Brebis", "Petit Morin", "Petit Pardou", "Petit-Suisse",
    "Picodon de Chevre", "Picos de Europa", "Piora", "Pithtviers au Foin",
    "Plateau de Herve", "Plymouth Cheese", "Podhalanski", "Poivre d'Ane", "Polkolbin",
    "Pont l'Eveque", "Port Nicholson", "Port-Salut", "Postel", "Pouligny-Saint-Pierre",
    "Pourly", "Prastost", "Pressato", "Prince-Jean", "Processed Cheddar", "Provolone",
    "Provolone (Australian)", "Pyengana Cheddar", "Pyramide", "Quark",
    "Quark (Australian)", "Quartirolo Lombardo", "Quatre-Vents", "Quercy Petit",
    "Queso Blanco", "Queso Blanco con Frutas --Pina y Mango", "Queso de Murcia",
    "Queso del Montsec", "Queso del Tietar", "Queso Fresco", "Queso Fresco (Adobera)",
    "Queso Iberico", "Queso Jalapeno", "Queso Majorero", "Queso Media Luna",
    "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette", "Ragusano", "Raschera",
    "Reblochon", "Red Leicester", "Regal de la Dombes", "Reggianito", "Remedou",
    "Requeson", "Richelieu", "Ricotta", "Ricotta (Australian)", "Ricotta Salata", "Ridder",
    "Rigotte", "Rocamadour", "Rollot", "Romano", "Romans Part Dieu", "Roncal", "Roquefort",
    "Roule", "Rouleau De Beaulieu", "Royalp Tilsit", "Rubens", "Rustinu", "Saaland Pfarr",
    "Saanenkaese", "Saga", "Sage Derby", "Sainte Maure", "Saint-Marcellin",
    "Saint-Nectaire", "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
    "Sap Sago", "Sardo", "Sardo Egyptian", "Sbrinz", "Scamorza", "Schabzieger", "Schloss",
    "Selles sur Cher", "Selva", "Serat", "Seriously Strong Cheddar", "Serra da Estrela",
    "Sharpam", "Shelburne Cheddar", "Shropshire Blue", "Siraz", "Sirene", "Smoked Gouda",
    "Somerset Brie", "Sonoma Jack", "Sottocenare al Tartufo", "Soumaintrain",
    "Sourire Lozerien", "Spenwood", "Sraffordshire Organic", "St. Agur Blue Cheese",
    "Stilton", "Stinking Bishop", "String", "Sussex Slipcote", "Sveciaost", "Swaledale",
    "Sweet Style Swiss", "Swiss", "Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",
    "Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea", "Testouri",
    "Tete de Moine", "Tetilla", "Texas Goat Cheese", "Tibet", "Tillamook Cheddar",
    "Tilsit", "Timboon Brie", "Toma", "Tomme Brulee", "Tomme d'Abondance",
    "Tomme de Chevre", "Tomme de Romans", "Tomme de Savoie", "Tomme des Chouans", "Tommes",
    "Torta del Casar", "Toscanello", "Touree de L'Aubier", "Tourmalet",
    "Trappe (Veritable)", "Trois Cornes De Vendee", "Tronchon", "Trou du Cru", "Truffe",
    "Tupi", "Turunmaa", "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",
    "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco", "Vendomois",
    "Vieux Corse", "Vignotte", "Vulscombe", "Waimata Farmhouse Blue",
    "Washed Rind Cheese (Australian)", "Waterloo", "Weichkaese", "Wellington",
    "Wensleydale", "White Stilton", "Whitestone Farmhouse", "Wigmore", "Woodside Cabecou",
    "Xanadu", "Xynotyro", "Yarg Cornish", "Yarra Valley Pyramid", "Yorkshire Blue",
    "Zamorano", "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"
)

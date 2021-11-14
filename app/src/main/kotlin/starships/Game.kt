package starships

import com.sun.tools.javac.Main
import edu.austral.dissis.starships.file.FileLoader
import edu.austral.dissis.starships.game.*
import edu.austral.dissis.starships.vector.Vector2
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.SerializersModule
import starships.data.GameData
import starships.factorys.*
import starships.serialized.MapToSerializable
import starships.spaceItems.SerializedGameData
import starships.spaceItems.Starship
import java.io.File
import java.io.IOException
import java.nio.file.FileStore

class Game: GameApplication() {
    override fun setupWindow(): WindowSettings {
       return WindowSettings.fromTitle("Starships!")
    }

    override fun initRoot(context: GameContext): Parent {
        return GameManager(this, context).init()
    }
}

class GameManager(private val rootSetter: RootSetter, private val context: GameContext) {
    private var gameState = "MENU"
    private var gameData:GameData? = null
    private val fileManager = FileLoader()
    private val mapper = MapToSerializable()

    fun init(): Parent {
        return when (gameState) {
            "MENU" -> loadIntro()
            "START" -> startGame(gameData!!)
            "PAUSE" -> pauseGame(gameData!!)
            else -> return Pane()
        }
    }

    private fun loadIntro(): Parent {
        val vbox = VBox();
        vbox.padding = Insets(10.0)
        vbox.spacing= 8.0;
        val title = Text("Menu")
        vbox.children.add(title);

        val options = ArrayList<Button>()
        val newGameButton = Button("New game")
        newGameButton.onAction = EventHandler { _ ->
            newGame()
        }
        options.add(newGameButton)

        val loadGameButton = Button("LoadGame")
        loadGameButton.onAction = EventHandler { _ ->
            loadGame()
        }
        options.add(loadGameButton)

        options.map{
            VBox.setMargin(it, Insets(0.0, 0.0, 0.0, 8.0))
            vbox.children.add(it)
        }

        return vbox
    }

    private fun loadGame(){
        val fileName = "starship_save.txt"
        val file = File(fileName)
        val data = file.readText()
        val serializedGameData = Json.decodeFromString(SerializedGameData.serializer(), data)
        gameData = mapper.serializedGameDataToGameData(serializedGameData)
        gameState = "START"
        rootSetter.setRoot(init())
    }

    private fun newGame(){
        val starShipFactory = StarShipFactory(90.0, "starship.png")
        val starship1 = starShipFactory.createStarShip(Vector2.vector(200.0,200.0))
        val starships = ArrayList<Starship>()
        starships.add(starship1)

        val spawn1 = AsteroidSpawn(20.0, 560.0, Vector2.vector(40.0, 0.0),
            Math.toRadians(20.0), Math.toRadians(160.0), 120.0, 2.0)
        val spawns = ArrayList<AsteroidSpawn>()
        spawns.add(spawn1)

        val pickUpSpawn = PickUpSpawn(480.0,600.0, Vector2.vector(0.0,0.0))

        val player1 = Player(StarshipMover(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.SPACE), starship1)
        val players = ArrayList<Player>()
        players.add(player1)

        gameData = GameData(players, spawns, pickUpSpawn, ArrayList(), ArrayList(), ArrayList())
        gameState = "START"

        rootSetter.setRoot(init())
    }

    private fun pauseGame(gameData: GameData): Parent{
        val vbox = VBox();
        vbox.padding = Insets(10.0)
        vbox.spacing= 8.0;
        val title = Text("Game is paused")
        vbox.children.add(title);

        val options = ArrayList<Button>()
        val saveGameButton = Button("Save game")
        saveGameButton.onAction = EventHandler { _ ->
            val serializedData = mapper.gameDataToSerializable(gameData)
            val file = File("starship_save.txt")

            val module = SerializersModule {

            }
            file.writeText(Json.encodeToString(serializedData))
        }
        options.add(saveGameButton)

        options.map{
            VBox.setMargin(it, Insets(0.0, 0.0, 0.0, 8.0))
            vbox.children.add(it)
        }

        vbox.setOnKeyPressed { event -> if(event.code == KeyCode.P){
            gameState = "START"
            rootSetter.setRoot(init())
        } }

        vbox.isFocusTraversable = true
        vbox.requestFocus()

        return vbox
    }

    private fun startGame(gameData: GameData): Parent {
        val pane = Pane()
        pane.isFocusTraversable = true
        pane.requestFocus()
        pane.onMouseClicked

        val starships = ArrayList<Starship>()
        gameData.players.map {
            starships.add(it.starship)
        }

        val space = Space(starships, gameData.asteroidSpawns, gameData.pickUpSpawn, gameData.laser, gameData.pickups, gameData.asteroids, pane)

        val timer = MainTimer(space, gameData.players, KeyCode.ESCAPE, context.keyTracker)
        timer.start()

        pane.onKeyPressed = EventHandler<KeyEvent> { event ->
            if(event.code == KeyCode.P){
                timer.stop()
                this.gameData = getGameData(space, gameData.players as ArrayList<Player>)
                gameState = "PAUSE"
                rootSetter.setRoot(init())
            }
        }
        return pane
    }

    private fun getGameData(space:Space, players:ArrayList<Player>):GameData{
        val spaceData = space.getSpaceData()
        return GameData(players, spaceData.asteroidSpawns, spaceData.pickUpSpawn, spaceData.asteroids, spaceData.laser, spaceData.pickups)
    }
}

class MainTimer(private val space: Space, private val players: List<Player>, private val pauseKey: KeyCode,
                private val keyTracker: KeyTracker) : GameTimer() {

    private val spawnHandler = SpawnHandler(space, 50, 200)
    private val pauseHandler = PauseHandler()
    private var currentFrame = 0
    private var gameHasEnded = false
    private var gameIsPaused = false

    override fun nextFrame(secondsSinceLastFrame: Double) {
        space.pane.requestFocus()
        if(!gameHasEnded){
            spawnHandler.passFrame(currentFrame)
            updatePosition(secondsSinceLastFrame)
            space.checkFire();
            space.moveAsteroids();
            space.moveLasers();
            space.checkCollisions();
            space.deleteImagesFromDestroyedItems();
            currentFrame += 1
            checkIfPlayersAlive()
            if(gameHasEnded){
                space.showEndScreen()
            }
        }
    }

    //TODO separate side effects
    private fun checkIfPlayersAlive(){
        var onePlayerAlive = false
        players.map {
            val starship = it.starship
            if(starship.hasLost() && !it.hasLost) {
                space.removeStarship(starship)
                it.lose()
            } else if(!it.hasLost){
                onePlayerAlive=true
            }
        }
        gameHasEnded = !onePlayerAlive
    }

    private fun updatePosition(secondsSinceLastFrame: Double) {
        players.map { it.handleKeyPress(keyTracker.keySet,secondsSinceLastFrame) }
    }
}
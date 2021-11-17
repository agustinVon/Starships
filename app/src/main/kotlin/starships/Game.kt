package starships

import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.game.*
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.text.Text
import javafx.stage.Screen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import starships.data.GameData
import starships.serialized.MapToSerializable
import starships.spaceItems.SerializedGameData
import starships.spaceItems.Starship
import java.io.File

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
        val newGameButton = Button("New single player game")
        newGameButton.onAction = EventHandler { _ ->
            newSingleGame()
        }
        options.add(newGameButton)

        val newMultiGameButton = Button("New multiplayer game")
        newMultiGameButton.onAction = EventHandler { _ ->
            newMultiGame()
        }
        options.add(newMultiGameButton)

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

    private fun newSingleGame(){
        val fileName = "game.json"
        val file = File(fileName)
        val data = file.readText()
        val serializedGameData = Json.decodeFromString(SerializedGameData.serializer(), data)
        gameData = mapper.serializedGameDataToGameData(serializedGameData)
        gameState = "START"
        rootSetter.setRoot(init())
    }

    private fun newMultiGame(){
        val fileName = "game2.json"
        val file = File(fileName)
        val data = file.readText()
        val serializedGameData = Json.decodeFromString(SerializedGameData.serializer(), data)
        gameData = mapper.serializedGameDataToGameData(serializedGameData)
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

            val module = SerializersModule {}
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
        val loader = ImageLoader()
        val pane = Pane()
        val image = loader.loadFromResources("background.jpg", Screen.getPrimary().visualBounds.width, Screen.getPrimary().visualBounds.height)
        pane.background = Background(BackgroundImage(image,BackgroundRepeat.REPEAT ,BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT))
        pane.isFocusTraversable = true
        pane.requestFocus()
        pane.onMouseClicked

        val starships = ArrayList<Starship>()
        gameData.players.map {
            starships.add(it.starship)
        }

        val space = Space(starships, gameData.asteroidSpawns, gameData.pickUpSpawn, gameData.laser, gameData.pickups, gameData.asteroids, pane)

        val timer = MainTimer(space, gameData.players, context.keyTracker)
        timer.start()

        pane.onKeyPressed = EventHandler<KeyEvent> { event ->
            if(event.code == KeyCode.P){
                timer.stop()
                this.gameData = getGameData(space, gameData.players as ArrayList<Player>)
                gameState = "PAUSE"
                rootSetter.setRoot(init())
            } else if(event.code == KeyCode.R){
                gameState= "MENU"
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

class MainTimer(private val space: Space, private val players: List<Player>,
                private val keyTracker: KeyTracker) : GameTimer() {

    private val spawnHandler = SpawnHandler(space, 40, 200)
    private var currentFrame = 0
    private var gameHasEnded = false

    override fun nextFrame(secondsSinceLastFrame: Double) {
        space.pane.requestFocus()
        if(!gameHasEnded){
            spawnHandler.passFrame(currentFrame)
            updatePosition(secondsSinceLastFrame)
            updateScoreboard()
            space.checkFire();
            space.moveAsteroids();
            space.moveLasers();
            space.checkCollisions();
            space.deleteImagesFromDestroyedItems();
            currentFrame += 1
            checkIfPlayersAlive()
            space.updateSpaceViews()
            if(gameHasEnded){
                space.showEndScreen()
            }
        }
    }

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

    private fun updateScoreboard(){
        val playerScore = HashMap<String, Int>()
        val playerLives = HashMap<String, Int>()
        players.map {
            it.updateScore()
            playerScore[it.name] = it.score
            playerLives.put(it.name, it.getLives())
        }
        space.updateScoreBoard(playerScore, playerLives)
    }
}
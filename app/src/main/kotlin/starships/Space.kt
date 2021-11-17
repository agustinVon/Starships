package starships

import edu.austral.dissis.starships.collision.CollisionEngine
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import starships.colliders.SpaceCollider
import starships.data.SpaceData
import starships.factorys.AsteroidSpawn
import starships.factorys.GunIsInCoolDownException
import starships.factorys.PickUpSpawn
import starships.spaceItems.Asteroid
import starships.spaceItems.Laser
import starships.spaceItems.PickUp
import starships.spaceItems.Starship
import starships.viewManagers.*
import java.awt.Button
import javax.swing.GroupLayout

class Space(val starships: ArrayList<Starship>, private val asteroidSpawns: List<AsteroidSpawn>, private val pickUpSpawn: PickUpSpawn,
            private val lasers: ArrayList<Laser>, private val pickUps: ArrayList<PickUp>,
            private val asteroids: ArrayList<Asteroid>, val pane: Pane) {
    private val collisionEngine = CollisionEngine()
    private val scoreBoard = ScoreBoard()
    private val starshipViews = ArrayList<StarShipViewManager>()
    private val laserViews = ArrayList<LaserViewManager>()
    private val asteroidViews = ArrayList<AsteroidViewManager>()
    private val pickUpViews = ArrayList<PickUpViewManager>()
    init {
        pane.children.add(scoreBoard.vBox)
        starships.map {
            val manager = StarShipViewManager(it, it.image, it.height-20.0, it.width-30.0, Math.toDegrees(it.direction.angle) - 90)
            starshipViews.add(manager)
            pane.children.add(manager.view)
        }
        lasers.map {
            val manager = LaserViewManager(it, it.image, it.height, it.width, Math.toDegrees(it.direction.angle) - 90)
            laserViews.add(manager)
            pane.children.add(manager.view)
        }
        asteroids.map{
            val manager = AsteroidViewManager(it, it.image, it.height, it.width, Math.toDegrees(it.direction.angle) - 90)
            asteroidViews.add(manager)
            pane.children.add(manager.view)
        }
        pickUps.map {
            val manager = PickUpViewManager(it, it.image,20.0, 20.0, 0.0)
            pickUpViews.add(manager)
            pane.children.add(manager.view)
        }
    }

    fun removeStarship(starship: Starship){
        starships.remove(starship)
        starshipViews.remove(starshipViews.find { it.starship == starship })
    }

    fun deleteImagesFromDestroyedItems(){
        val views = ArrayList<ImageView>()
        val lasersToRemove = ArrayList<Laser>()
        val asteroidsToRemove = ArrayList<Asteroid>()
        val pickUpsToRemove = ArrayList<PickUp>()
        pickUps.map {
            if(it.destroyed){
                val pickUpView = pickUpViews.find { manager -> manager.pickUp == it }
                views.add(pickUpView!!.view)
                pickUpsToRemove.add(it)
            }
        }
        asteroids.map {
            if(it.destroyed){
                val asteroidViews = asteroidViews.find { manager -> manager.asteroid == it }
                views.add(asteroidViews!!.view)
                asteroidsToRemove.add(it)
            }
        }
        lasers.map {
            if(it.destroyed){
                val laserViews = laserViews.find { manager -> manager.laser == it }
                views.add(laserViews!!.view)
                lasersToRemove.add(it)
            }
        }
        pane.children.removeAll(views)
        lasers.removeAll(lasersToRemove)
        asteroids.removeAll(asteroidsToRemove)
        pickUps.removeAll(pickUpsToRemove)
    }

    fun moveAsteroids() {
        asteroids.map {
            it.move(it.position.add(it.direction.multiply(it.speed)), it.direction)
        }
    }

    fun moveLasers(){
        lasers.map{
            it.move(it.position.add(it.direction.multiply(it.speed)), it.direction)
        }
    }

    fun createAsteroid(){
        val asteroid = asteroidSpawns.random().spawnAsteroid()
        asteroids.add(asteroid)
        val viewManager = AsteroidViewManager(asteroid, asteroid.image, asteroid.height, asteroid.width, Math.toDegrees(asteroid.direction.angle) - 90)
        asteroidViews.add(viewManager)
        pane.children.add(viewManager.view)
    }

    fun checkCollisions() {
        val colliders = ArrayList<SpaceCollider>()
        val asteroidColliders = asteroidViews.map {
            it.collider
        }
        val starshipColliders = starshipViews.map {
            it.collider
        }
        val laserColliders = laserViews.map{
            it.collider
        }
        val pickupCollider = pickUpViews.map {
            it.collider
        }
        colliders.addAll(asteroidColliders)
        colliders.addAll(laserColliders)
        colliders.addAll(starshipColliders)
        colliders.addAll(pickupCollider)
        collisionEngine.checkCollisions(colliders)
    }

    fun showEndScreen() {
        pane.children.removeAll(pane.children)
        val text = Text("Game is over")
        text.textAlignment = TextAlignment.CENTER
        text.fill = Color.WHITE
        val vBox = VBox(text)
        val winner = Text(scoreBoard.getWinnerPlayerAndScore())
        winner.textAlignment = TextAlignment.CENTER
        winner.fill = Color.WHITE
        vBox.children.add(winner)
        pane.children.add(vBox)
    }

    fun checkFire() {
        val lasersToShoot = ArrayList<Laser>()
        starships.map{
            if(it.hasFired){
                try{
                    val laserList = it.shootGun()
                    lasersToShoot.addAll(laserList)
                } catch (e:GunIsInCoolDownException){ }
            }
        }
        lasersToShoot.map{
            lasers.add(it)
            val managerView = LaserViewManager(it, it.image, it.height, it.width, Math.toDegrees(it.direction.angle) - 90)
            laserViews.add(managerView)
            pane.children.add(managerView.view)
        }
    }

    fun createPickups() {
        if(pickUps.size <2){
            val pickUp = pickUpSpawn.spawnPickUp()
            pickUps.add(pickUp)
            val managerView = PickUpViewManager(pickUp, pickUp.image, 20.0,20.0,0.0)
            pickUpViews.add(managerView)
            pane.children.add(managerView.view)
        }
    }

    fun updateSpaceViews(){
        laserViews.map {
            it.calculateView()
        }
        starshipViews.map{
            it.calculateView()
        }
        pickUpViews.map {
            it.calculateView()
        }
        asteroidViews.map {
            it.calculateView()
        }
    }

    fun getSpaceData(): SpaceData {
        return SpaceData(asteroidSpawns, pickUpSpawn, asteroids, lasers, pickUps)
    }

    fun updateScoreBoard(playerScores:Map<String,Int>, playerLives:Map<String, Int>){
        scoreBoard.updateScoreboard(playerScores, playerLives)
    }
}
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
import java.awt.Button
import javax.swing.GroupLayout

class Space(val starships: ArrayList<Starship>, private val asteroidSpawns: List<AsteroidSpawn>, private val pickUpSpawn: PickUpSpawn,
            private val lasers: ArrayList<Laser>, private val pickUps: ArrayList<PickUp>,
            private val asteroids: ArrayList<Asteroid>, val pane: Pane) {
    private val collisionEngine = CollisionEngine()
    private val scoreBoard = ScoreBoard()
    init {
        pane.children.add(scoreBoard.vBox)
        starships.map {
            pane.children.add(it.view)
        }
        lasers.map {
            pane.children.add(it.view)
        }
        asteroids.map{
            pane.children.add(it.view)
        }
        pickUps.map {
            pane.children.add(it.imageView)
        }
    }

    fun removeStarship(starship: Starship){
        starships.remove(starship)
        pane.children.remove(starship.view)
    }

    fun deleteImagesFromDestroyedItems(){
        val views = ArrayList<ImageView>()
        val lasersToRemove = ArrayList<Laser>()
        val asteroidsToRemove = ArrayList<Asteroid>()
        val pickUpsToRemove = ArrayList<PickUp>()
        pickUps.map {
            if(it.destroyed){
                views.add(it.imageView)
                pickUpsToRemove.add(it)
            }
        }
        asteroids.map {
            if(it.destroyed){
                views.add(it.view)
                asteroidsToRemove.add(it)
            }
        }
        lasers.map {
            if(it.destroyed){
                views.add(it.view)
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
        pane.children.add(asteroid.view)
    }

    fun checkCollisions() {
        val colliders = ArrayList<SpaceCollider>()
        val asteroidColliders = asteroids.map {
            it.asteroidCollider
        }
        val starshipColliders = starships.map {
            it.starShipDamageCollider
        }
        val laserColliders = lasers.map{
            it.laserCollider
        }
        val pickupCollider = pickUps.map {
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
            pane.children.add(it.view)
        }
    }

    fun createPickups() {
        if(pickUps.size <2){
            val pickUp = pickUpSpawn.spawnPickUp()
            pickUps.add(pickUp)
            pane.children.add(pickUp.imageView)
        }
    }

    fun getSpaceData(): SpaceData {
        return SpaceData(asteroidSpawns, pickUpSpawn, asteroids, lasers, pickUps)
    }

    fun updateScoreBoard(playerScores:Map<String,Int>, playerLives:Map<String, Int>){
        scoreBoard.updateScoreboard(playerScores, playerLives)
    }
}
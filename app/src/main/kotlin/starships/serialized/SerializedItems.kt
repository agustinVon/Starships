package starships.spaceItems

import com.soywiz.klock.TimeSpan
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable
import starships.Player
import starships.StarshipMover
import starships.factorys.AsteroidSpawn
import starships.factorys.FastGun
import starships.factorys.Gun
import starships.factorys.PickUpSpawn

@Serializable
data class SerializedVector(val x:Double, val y:Double, val rot:Double)

@Serializable
data class SerializedPlayer(val name:String, val shipMover: StarshipMover, val starship: SerializedStarShip)

@Serializable
data class SerializedStarShip(val position: SerializedVector, val direction: SerializedVector, val speed: Double,
                    var lives: Int, val height: Double, val width: Double,val gunSpeed: Double, val id: Int, val score: Int)

@Serializable
data class SerializedAsteroid(val position: SerializedVector, val direction: SerializedVector, val height: Double, val width: Double, val speed: Double)

@Serializable
data class SerializedLaser(val position:SerializedVector, val direction: SerializedVector, val speed: Double,
                  val height: Double, val width: Double, val starship: Int)

@Serializable
data class SerializedPickUp(val gun: Gun, val cooldownMilis:Int , val position: SerializedVector, val source:String)

@Serializable
data class SerializedAsteroidSpawn(val height: Double, val width: Double, val position: SerializedVector,
                                   val minAngle: Double, val maxAngle: Double,
                                   val maxSize: Double, val speed: Double)

@Serializable
data class  SerializedPickupSpawn(val height: Double, val width: Double, val position: SerializedVector)

@Serializable
data class SerializedGameData(val players: List<SerializedPlayer>, val asteroidSpawns: List<SerializedAsteroidSpawn>, val pickUpSpawn: SerializedPickupSpawn,
                              val asteroids: List<SerializedAsteroid>, val laser: List<SerializedLaser>, val pickups: List<SerializedPickUp>)
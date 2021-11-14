package starships.factorys

import com.soywiz.klock.seconds
import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.Serializable
import starships.spaceItems.PickUp
import kotlin.random.Random

class PickUpSpawn(val height: Double, val width: Double, val position: Vector2) {
    val imageLoader = ImageLoader()
    private val possiblePickups = HashMap<Gun, String>()
    init {
        possiblePickups[FastGun(10.0)] = "fast_laser.png"
        possiblePickups[TripleGun(10.0)] = "triple_laser.png"
    }
    fun spawnPickUp():PickUp{
        val gun = possiblePickups.keys.random()
        val x = Random.nextDouble(position.x, width + position.x)
        val y = Random.nextDouble(position.y, height + position.y)
        val image = imageLoader.loadFromResources(possiblePickups[gun]!!, 40.0, 40.0)
        return PickUp(gun, 5.seconds, Vector2.vector(x, y), ImageView(image), possiblePickups[gun]!!)
    }
}
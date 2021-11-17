package starships.spaceItems

import com.soywiz.klock.internal.Serializable
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.LaserCollider

class Laser(position: Vector2, direction: Vector2, speed: Double, val image: Image, val height: Double, val width: Double, val onCrash:(score: Int) -> Unit, val starshipId:Int)
    : MovableItem(position, direction, speed) {
    var destroyed = false

    fun destroy(){
        destroyed = true
    }
}
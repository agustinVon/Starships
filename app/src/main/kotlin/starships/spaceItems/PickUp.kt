package starships.spaceItems

import com.soywiz.klock.TimeSpan
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.PickUpCollider
import starships.factorys.Gun

class PickUp(val gun: Gun, val cooldown:TimeSpan, val position:Vector2, val source:String, val image:Image) {

    var destroyed = false
}
package starships.spaceItems

import com.soywiz.klock.TimeSpan
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import starships.colliders.PickUpCollider
import starships.factorys.Gun

class PickUp(val gun: Gun, val cooldown:TimeSpan, val position:Vector2, val imageView:ImageView, val source:String) {
    val collider = PickUpCollider(20.0,20.0, 0.0, this)
    init {
        collider.shape.layoutX = position.x
        collider.shape.layoutY = position.y
        imageView.layoutY = position.y
        imageView.layoutX = position.x
    }

    var destroyed = false
}
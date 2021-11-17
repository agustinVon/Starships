package starships.viewManagers

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import starships.colliders.PickUpCollider
import starships.spaceItems.PickUp

class PickUpViewManager(val pickUp:PickUp, image:Image, height: Double, width:Double, angle:Double) {
    val collider = PickUpCollider(height, width, angle, pickUp)
    val view = ImageView(image)
    init {
        collider.shape.layoutX = pickUp.position.x + 25.0
        collider.shape.layoutY = pickUp.position.y + 25.0
        view.layoutX = pickUp.position.x
        view.layoutY = pickUp.position.y
    }
    fun calculateView() {
        collider.shape.layoutX = pickUp.position.x
        collider.shape.layoutY = pickUp.position.y
        view.layoutX = pickUp.position.x
        view.layoutY = pickUp.position.y
    }
}
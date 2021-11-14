package starships.factorys

import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import starships.Player
import starships.spaceItems.Starship

class StarShipFactory(private val speed: Double, private val image: String) {
    private val imageLoader = ImageLoader()
    private var lastId = 0
    fun createStarShip(position: Vector2): Starship{
        val starshipImage = imageLoader.loadFromResources(image, 60.0, 60.0)
        lastId++
        return Starship(position, Vector2.vector(0.0,1.0), speed,
            ImageView(starshipImage), 3, 60.0, 60.0, 6.0, lastId)
    }
}
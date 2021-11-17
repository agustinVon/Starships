package starships.factorys

import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import starships.spaceItems.Asteroid
import kotlin.random.Random

class AsteroidSpawn(val height: Double, val width: Double, val position: Vector2,
                    val minAngle: Double, val maxAngle: Double,
                    val maxSize: Double, val speed: Double) {
    val imageLoader = ImageLoader()
    fun spawnAsteroid() : Asteroid {
        val size = Random.nextDouble(20.0, maxSize)
        val x = Random.nextDouble(position.x, width + position.x)
        val y = Random.nextDouble(position.y, height + position.y)
        val angle = Random.nextDouble(minAngle, maxAngle)
        val direction = Vector2.vector(1.0, 0.0).rotate(angle)
        val asteroidPosition = Vector2.vector(x, y)
        val asteroidImage = imageLoader.loadFromResources("meteorito.png", size, size)
        return Asteroid(asteroidPosition, direction, asteroidImage, speed, size, size)
    }
}

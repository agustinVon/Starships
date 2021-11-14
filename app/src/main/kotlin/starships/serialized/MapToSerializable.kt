package starships.serialized

import com.soywiz.klock.milliseconds
import edu.austral.dissis.starships.file.ImageLoader
import edu.austral.dissis.starships.vector.Vector2
import javafx.scene.image.ImageView
import kotlinx.serialization.json.Json
import starships.Player
import starships.data.GameData
import starships.factorys.AsteroidSpawn
import starships.factorys.PickUpSpawn
import starships.spaceItems.*

class MapToSerializable {
    fun gameDataToSerializable(gameData: GameData):SerializedGameData{
        val serializedPlayers = gameData.players.map{
            val position = toSerializedVector(it.starship.position)
            val direction = toSerializedVector(it.starship.direction)
            val serializedStarShip = SerializedStarShip(position, direction, it.starship.speed,
                it.starship.lives, it.starship.height, it.starship.width, it.starship.gunSpeed, it.starship.id)
            SerializedPlayer(it.name, it.shipMover, serializedStarShip)
        }
        val serializedAsteroidSpawns = gameData.asteroidSpawns.map{
            val position = toSerializedVector(it.position)
            SerializedAsteroidSpawn(it.height, it.width, position, it.minAngle, it.maxAngle, it.maxSize, it.speed)
        }
        val pickupSpawnPosition = toSerializedVector(gameData.pickUpSpawn.position)
        val serializedPickupSpawn = SerializedPickupSpawn(gameData.pickUpSpawn.height, gameData.pickUpSpawn.width, pickupSpawnPosition)
        val serializedAsteroids = gameData.asteroids.map {
            val position = toSerializedVector(it.position)
            val direction = toSerializedVector(it.direction)
            SerializedAsteroid(position, direction, it.height, it.width, it.speed)
        }
        val serializedPickUps = gameData.pickups.map {
            val position = toSerializedVector(it.position)
            SerializedPickUp(it.gun, it.cooldown.microsecondsInt, position, it.source)
        }
        val serializedLasers = gameData.laser.map {
            val position = toSerializedVector(it.position)
            val direction = toSerializedVector(it.direction)
            SerializedLaser(position, direction, it.speed, it.height, it.width, it.starshipId)
        }
        return SerializedGameData(serializedPlayers, serializedAsteroidSpawns, serializedPickupSpawn, serializedAsteroids, serializedLasers, serializedPickUps)
    }

    fun serializedGameDataToGameData(gameData: SerializedGameData):GameData{

        val starships = ArrayList<Starship>()

        val players = gameData.players.map{
            val serialStarship = it.starship
            val starshipPosition = toNormalVector(serialStarship.position)
            val starshipDirection = toNormalVector(serialStarship.direction)
            val starship = Starship(starshipPosition, starshipDirection, serialStarship.speed,
                imageToView("starship.png",60.0,60.0),serialStarship.lives,
                serialStarship.height, serialStarship.width, serialStarship.gunSpeed, serialStarship.id)
            starships.add(starship)
            Player(it.name, it.shipMover, starship)
        }
        val asteroidSpawns = gameData.asteroidSpawns.map {
            AsteroidSpawn(it.height, it.width, toNormalVector(it.position), it.minAngle, it.maxAngle, it.maxSize, it.speed)
        }
        val pickupSpawn = PickUpSpawn(gameData.pickUpSpawn.height, gameData.pickUpSpawn.width, toNormalVector(gameData.pickUpSpawn.position))
        val asteroids = gameData.asteroids.map {
            val position = toNormalVector(it.position)
            val direction = toNormalVector(it.direction)
            Asteroid(position, direction, it.speed, imageToView("meteorito.png", it.height, it.width), it.height, it.width)
        }
        val asteroidsList = ArrayList<Asteroid>(asteroids)
        val pickUps = gameData.pickups.map{
            val position = toNormalVector(it.position)
            PickUp(it.gun, it.cooldownMilis.milliseconds, position, imageToView(it.source, 40.0, 40.0), it.source)
        }
        val pickUpsList = ArrayList<PickUp>(pickUps)
        val lasers = gameData.laser.map {
            val position = toNormalVector(it.position)
            val direction = toNormalVector(it.direction)
            val lasersStarship = starships.find { starship ->
                it.starship == starship.id
            }
            val starship = lasersStarship!!
            Laser(position,direction, it.speed, imageToView("laser.png", 30.0, 30.0), it.height, it.width,
                starship::addScore, it.starship)
        }
        val laserList = ArrayList<Laser>(lasers)
        return GameData(players, asteroidSpawns, pickupSpawn, asteroidsList, laserList, pickUpsList)
    }

    private fun imageToView(source:String, height:Double, width:Double):ImageView{
        val imageLoader = ImageLoader()
        val loaded = imageLoader.loadFromResources(source, width, height)
        return ImageView(loaded)
    }

    private fun toNormalVector(vector:SerializedVector):Vector2{
        val normalVector = Vector2.vector(vector.x, vector.y)
        normalVector.rotate(vector.rot)
        return normalVector
    }

    private fun toSerializedVector(vector:Vector2):SerializedVector{
        return SerializedVector(vector.x, vector.y, vector.angle)
    }
}